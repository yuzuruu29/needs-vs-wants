// Needs vs Wants - payments vs entitlements reconciliation (manual, read-only)
//
// Cross-checks provider dashboard exports (PayMongo payments CSV and/or
// PayPal subscriptions CSV) against the Supabase payment_events ledger and
// entitlements table, and lists mismatches:
//
//   1. paid-no-ledger:        provider says paid, but no payment_events row
//                             (missed/failed webhook - user may have paid
//                             without receiving Pro).
//   2. ledger-no-entitlement: payment recorded in the ledger, but the user's
//                             entitlements row is missing or shows no grant.
//   3. entitled-no-payment:   active PAID paymongo/paypal entitlement with no
//                             ledger row at all for that user. NOTE: PayPal
//                             subscribers granted BEFORE the ledger cutover
//                             (2026-08-13) legitimately appear here until
//                             their first post-cutover renewal webhook.
//
// Usage (PowerShell):
//   $env:SUPABASE_URL = "https://xpwcrloarciomikfudln.supabase.co"
//   $env:SUPABASE_SERVICE_ROLE_KEY = "..."   # service role; never ship this
//   deno run --allow-net --allow-env --allow-read supabase/tools/reconcile.ts `
//     --paymongo=paymongo_payments.csv --paypal=paypal_subscriptions.csv
//
// Either CSV may be omitted. Read-only: this tool never writes to Supabase.
//
// CSV handling is deliberately layout-agnostic: dashboard export columns
// change, so provider ids are recognized by VALUE pattern anywhere in the row
// (PayMongo payment ids `pay_...`, PayPal subscription ids `I-...`) and the
// status column by header name when one exists.

// ---------------------------------------------------------------------------
// Pure helpers (unit-tested; no network / no filesystem)
// ---------------------------------------------------------------------------

/** Minimal RFC-4180-ish CSV parser: quoted fields, embedded commas/quotes/newlines. */
export function parseCsv(text: string): string[][] {
  const rows: string[][] = [];
  let row: string[] = [];
  let field = "";
  let inQuotes = false;

  for (let i = 0; i < text.length; i++) {
    const ch = text[i];
    if (inQuotes) {
      if (ch === '"') {
        if (text[i + 1] === '"') {
          field += '"';
          i++;
        } else {
          inQuotes = false;
        }
      } else {
        field += ch;
      }
    } else if (ch === '"') {
      inQuotes = true;
    } else if (ch === ",") {
      row.push(field);
      field = "";
    } else if (ch === "\n" || ch === "\r") {
      if (ch === "\r" && text[i + 1] === "\n") i++;
      row.push(field);
      field = "";
      rows.push(row);
      row = [];
    } else {
      field += ch;
    }
  }
  if (field.length > 0 || row.length > 0) {
    row.push(field);
    rows.push(row);
  }
  // Drop fully-empty trailing rows.
  return rows.filter((r) => r.some((cell) => cell.trim().length > 0));
}

export type Provider = "paymongo" | "paypal";

const ID_PATTERNS: Record<Provider, RegExp> = {
  paymongo: /^pay_[A-Za-z0-9]+$/,
  paypal: /^I-[A-Z0-9]+$/,
};

// Rows whose status clearly did not collect money are excluded from the
// "paid but missing" check. Unknown/absent statuses are treated as paid so
// mismatches surface for manual review rather than being silently dropped.
const NON_PAID_STATUS =
  /fail|refund|void|cancel|pending|unpaid|expired|declin|chargeback/i;

export interface ProviderRow {
  id: string;
  status: string;
}

/**
 * Extract provider rows from a dashboard CSV export. The id is matched by
 * value pattern in any cell (column layouts vary between export versions);
 * the status comes from a `status`-named column when present.
 */
export function extractProviderRows(
  csv: string[][],
  provider: Provider,
): ProviderRow[] {
  if (csv.length === 0) return [];
  const header = csv[0].map((h) => h.trim().toLowerCase());
  const statusIdx = header.findIndex((h) => /status|state/.test(h));
  const pattern = ID_PATTERNS[provider];

  const rows: ProviderRow[] = [];
  const seen = new Set<string>();
  for (const row of csv.slice(1)) {
    const id = row.map((cell) => cell.trim()).find((cell) =>
      pattern.test(cell)
    );
    if (!id || seen.has(id)) continue;
    seen.add(id);
    rows.push({
      id,
      status: statusIdx >= 0 ? (row[statusIdx] ?? "").trim() : "",
    });
  }
  return rows;
}

/** Provider row counted as money collected (see NON_PAID_STATUS). */
export function isPaidStatus(status: string): boolean {
  return !NON_PAID_STATUS.test(status);
}

export interface LedgerRow {
  id: string;
  user_id: string;
  provider: string;
  checkout_session_id: string | null;
  status: string;
  created_at: string;
}

export interface EntitlementReconRow {
  user_id: string;
  is_pro: boolean;
  tier?: string | null;
  paid_until: string | null;
  trial_ends_at: string | null;
  provider: string | null;
  status: string | null;
}

export interface ReconciliationReport {
  paidNoLedger: Array<{ provider: Provider; id: string; status: string }>;
  ledgerNoEntitlement: Array<{ ledger: LedgerRow; note: string }>;
  entitledNoPayment: Array<{ entitlement: EntitlementReconRow; note: string }>;
}

/**
 * Core reconciliation. PayMongo dashboard ids match ledger PRIMARY KEYS
 * (payment id pay_...); PayPal dashboard subscription ids match the ledger's
 * checkout_session_id column (where the PayPal webhook stores them).
 */
export function reconcile(
  providerRows: Partial<Record<Provider, ProviderRow[]>>,
  ledger: LedgerRow[],
  entitlements: EntitlementReconRow[],
  nowIso: string,
): ReconciliationReport {
  const report: ReconciliationReport = {
    paidNoLedger: [],
    ledgerNoEntitlement: [],
    entitledNoPayment: [],
  };

  const ledgerIds = new Set(ledger.map((l) => l.id));
  const ledgerSubscriptionIds = new Set(
    ledger
      .filter((l) => l.provider === "paypal" && l.checkout_session_id)
      .map((l) => l.checkout_session_id as string),
  );

  // 1. Provider says paid, ledger never saw it.
  for (const row of providerRows.paymongo ?? []) {
    if (!isPaidStatus(row.status)) continue;
    if (!ledgerIds.has(row.id)) {
      report.paidNoLedger.push({ provider: "paymongo", ...row });
    }
  }
  for (const row of providerRows.paypal ?? []) {
    if (!isPaidStatus(row.status)) continue;
    if (!ledgerSubscriptionIds.has(row.id)) {
      report.paidNoLedger.push({ provider: "paypal", ...row });
    }
  }

  // 2. Ledger row without any sign of a grant on the user's entitlement.
  const entitlementsByUser = new Map(
    entitlements.map((e) => [e.user_id, e]),
  );
  for (const l of ledger) {
    const ent = entitlementsByUser.get(l.user_id);
    if (!ent) {
      report.ledgerNoEntitlement.push({
        ledger: l,
        note: "no entitlements row for this user",
      });
    } else if (!ent.is_pro && ent.paid_until === null && ent.trial_ends_at === null) {
      report.ledgerNoEntitlement.push({
        ledger: l,
        note: "entitlements row shows no grant (is_pro=false, no windows)",
      });
    }
  }

  // 3. Active PAID paymongo/paypal entitlement without any ledger row.
  const ledgerUsers = new Set(ledger.map((l) => l.user_id));
  const nowMs = Date.parse(nowIso);
  for (const ent of entitlements) {
    if (!ent.is_pro) continue;
    if (ent.provider !== "paymongo" && ent.provider !== "paypal") continue;
    // Paid = has a future (or lifetime-null) paid window; trial-only rows
    // (null paid_until + a trial window) are expected to have no payment yet.
    const isTrialOnly = ent.paid_until === null && ent.trial_ends_at !== null;
    const paidActive = ent.paid_until === null ||
      Date.parse(ent.paid_until) > nowMs;
    if (!paidActive || isTrialOnly) continue;
    if (!ledgerUsers.has(ent.user_id)) {
      report.entitledNoPayment.push({
        entitlement: ent,
        note: ent.provider === "paypal"
          ? "no ledger row (EXPECTED for PayPal grants made before the 2026-08-13 ledger cutover)"
          : "no ledger row for this user",
      });
    }
  }

  return report;
}

// ---------------------------------------------------------------------------
// Supabase REST + CLI (main only, read-only)
// ---------------------------------------------------------------------------

async function fetchRest<T>(
  supabaseUrl: string,
  serviceRoleKey: string,
  pathAndQuery: string,
): Promise<T[]> {
  const res = await fetch(`${supabaseUrl}/rest/v1/${pathAndQuery}`, {
    headers: {
      apikey: serviceRoleKey,
      Authorization: `Bearer ${serviceRoleKey}`,
      // PostgREST caps at the instance max_rows; fine at this project's scale.
      Range: "0-9999",
    },
  });
  if (!res.ok) {
    const body = await res.text().catch(() => "");
    throw new Error(
      `Supabase REST ${pathAndQuery} failed: HTTP ${res.status} ${body.slice(0, 200)}`,
    );
  }
  return await res.json() as T[];
}

function argValue(name: string): string | null {
  const prefix = `--${name}=`;
  const arg = Deno.args.find((a) => a.startsWith(prefix));
  return arg ? arg.slice(prefix.length) : null;
}

if (import.meta.main) {
  const supabaseUrl = Deno.env.get("SUPABASE_URL");
  const serviceRoleKey = Deno.env.get("SUPABASE_SERVICE_ROLE_KEY");
  if (!supabaseUrl || !serviceRoleKey) {
    console.error(
      "Set SUPABASE_URL and SUPABASE_SERVICE_ROLE_KEY env vars first.",
    );
    Deno.exit(2);
  }

  const paymongoPath = argValue("paymongo");
  const paypalPath = argValue("paypal");
  if (!paymongoPath && !paypalPath) {
    console.error(
      "Pass at least one dashboard export: --paymongo=file.csv and/or --paypal=file.csv",
    );
    Deno.exit(2);
  }

  const providerRows: Partial<Record<Provider, ProviderRow[]>> = {};
  if (paymongoPath) {
    providerRows.paymongo = extractProviderRows(
      parseCsv(await Deno.readTextFile(paymongoPath)),
      "paymongo",
    );
    console.log(
      `PayMongo CSV: ${providerRows.paymongo.length} payment id(s) found`,
    );
  }
  if (paypalPath) {
    providerRows.paypal = extractProviderRows(
      parseCsv(await Deno.readTextFile(paypalPath)),
      "paypal",
    );
    console.log(
      `PayPal CSV: ${providerRows.paypal.length} subscription id(s) found`,
    );
  }

  const ledger = await fetchRest<LedgerRow>(
    supabaseUrl,
    serviceRoleKey,
    "payment_events?select=id,user_id,provider,checkout_session_id,status,created_at",
  );
  const entitlements = await fetchRest<EntitlementReconRow>(
    supabaseUrl,
    serviceRoleKey,
    "entitlements?select=user_id,is_pro,tier,paid_until,trial_ends_at,provider,status",
  );
  console.log(
    `Supabase: ${ledger.length} payment_events row(s), ${entitlements.length} entitlements row(s)`,
  );

  const report = reconcile(
    providerRows,
    ledger,
    entitlements,
    new Date().toISOString(),
  );

  console.log("\n--- 1. Paid at provider, missing from payment_events ---");
  if (report.paidNoLedger.length === 0) console.log("  none");
  for (const m of report.paidNoLedger) {
    console.log(`  [${m.provider}] ${m.id} (status: ${m.status || "?"})`);
  }

  console.log("\n--- 2. In payment_events, but user shows no grant ---");
  if (report.ledgerNoEntitlement.length === 0) console.log("  none");
  for (const m of report.ledgerNoEntitlement) {
    console.log(
      `  [${m.ledger.provider}] ${m.ledger.id} user=${m.ledger.user_id}: ${m.note}`,
    );
  }

  console.log("\n--- 3. Active paid entitlement with no payment record ---");
  if (report.entitledNoPayment.length === 0) console.log("  none");
  for (const m of report.entitledNoPayment) {
    console.log(
      `  [${m.entitlement.provider}] user=${m.entitlement.user_id} paid_until=${m.entitlement.paid_until}: ${m.note}`,
    );
  }

  const total = report.paidNoLedger.length +
    report.ledgerNoEntitlement.length +
    report.entitledNoPayment.length;
  console.log(
    total === 0
      ? "\nNo mismatches. Providers, ledger, and entitlements agree."
      : `\n${total} mismatch(es) found - review above.`,
  );
}
