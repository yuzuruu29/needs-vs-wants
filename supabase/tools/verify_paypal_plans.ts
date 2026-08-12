// Needs vs Wants - PayPal plan price verification tool (manual, read-only)
//
// The D148 caveat: the dashboard prices behind PAYPAL_PLAN_PRO / PAYPAL_PLAN_MAX
// could not be verified from this machine after the PHP 49/99 price cut, and
// the annual plans (PHP 490/990, YEAR interval) may not exist yet. This tool
// fetches each plan from the PayPal Billing Plans API and prints its name,
// status, billing cycles, and pricing so the owner can verify.
//
// Usage (PowerShell):
//   $env:PAYPAL_CLIENT_ID = "..."
//   $env:PAYPAL_CLIENT_SECRET = "..."
//   $env:PAYPAL_ENV = "live"        # or "sandbox"; default live
//   deno run --allow-net --allow-env supabase/tools/verify_paypal_plans.ts
//
//   # extra plan ids (e.g. new annual plans) as bare args or labeled flags:
//   deno run --allow-net --allow-env supabase/tools/verify_paypal_plans.ts `
//     --pro-annual=P-XXXX --max-annual=P-YYYY
//
// Auth: POST /v1/oauth2/token (client credentials), then
// GET /v1/billing/plans/{id} per plan. Read-only; never mutates plans.

// ---------------------------------------------------------------------------
// Expectations (server-authoritative prices, D146/D147)
// ---------------------------------------------------------------------------

export interface PlanExpectation {
  label: string;
  price: string;
  currency: string;
  interval_unit: string;
}

/** Live monthly plan ids currently wired into the app (from local.properties). */
export const KNOWN_MONTHLY_PLANS: Record<string, PlanExpectation> = {
  "P-701099249D7315939NJ3BQHQ": {
    label: "Pro monthly",
    price: "49",
    currency: "PHP",
    interval_unit: "MONTH",
  },
  "P-2GK5612954300654GNJ3BSBQ": {
    label: "Max monthly",
    price: "99",
    currency: "PHP",
    interval_unit: "MONTH",
  },
};

export const PRO_ANNUAL_EXPECTATION: PlanExpectation = {
  label: "Pro annual",
  price: "490",
  currency: "PHP",
  interval_unit: "YEAR",
};

export const MAX_ANNUAL_EXPECTATION: PlanExpectation = {
  label: "Max annual",
  price: "990",
  currency: "PHP",
  interval_unit: "YEAR",
};

// ---------------------------------------------------------------------------
// Pure helpers (unit-tested; no network)
// ---------------------------------------------------------------------------

export interface PlanCheck {
  planId: string;
  expectation: PlanExpectation | null;
}

/**
 * CLI args -> plan checks. Bare args are extra plan ids (printed without
 * pass/fail assessment); --pro-annual=P-x / --max-annual=P-y attach the
 * annual price expectations. The two known monthly plans are always checked
 * first unless --skip-monthly is passed.
 */
export function parsePlanArgs(args: string[]): PlanCheck[] {
  const checks: PlanCheck[] = [];
  if (!args.includes("--skip-monthly")) {
    for (const [planId, expectation] of Object.entries(KNOWN_MONTHLY_PLANS)) {
      checks.push({ planId, expectation });
    }
  }
  for (const arg of args) {
    if (arg === "--skip-monthly") continue;
    if (arg.startsWith("--pro-annual=")) {
      checks.push({
        planId: arg.slice("--pro-annual=".length),
        expectation: PRO_ANNUAL_EXPECTATION,
      });
    } else if (arg.startsWith("--max-annual=")) {
      checks.push({
        planId: arg.slice("--max-annual=".length),
        expectation: MAX_ANNUAL_EXPECTATION,
      });
    } else if (arg.startsWith("P-")) {
      checks.push({
        planId: arg,
        expectation: KNOWN_MONTHLY_PLANS[arg] ?? null,
      });
    }
  }
  return checks.filter((c) => c.planId.length > 0);
}

export interface PlanCycleSummary {
  sequence: number;
  tenure_type: string;
  interval_unit: string;
  interval_count: number;
  total_cycles: number;
  price: string | null;
  currency: string | null;
}

export interface PlanSummary {
  id: string;
  name: string;
  status: string;
  product_id: string;
  cycles: PlanCycleSummary[];
}

/** GET /v1/billing/plans/{id} JSON -> flat summary (null when malformed). */
export function summarizePlan(json: unknown): PlanSummary | null {
  if (!json || typeof json !== "object") return null;
  const plan = json as Record<string, unknown>;
  if (typeof plan.id !== "string") return null;

  const cyclesRaw = Array.isArray(plan.billing_cycles)
    ? plan.billing_cycles
    : [];
  const cycles: PlanCycleSummary[] = cyclesRaw
    .filter((c): c is Record<string, unknown> => !!c && typeof c === "object")
    .map((c) => {
      const frequency =
        c.frequency && typeof c.frequency === "object"
          ? c.frequency as Record<string, unknown>
          : {};
      const scheme =
        c.pricing_scheme && typeof c.pricing_scheme === "object"
          ? c.pricing_scheme as Record<string, unknown>
          : {};
      const fixedPrice =
        scheme.fixed_price && typeof scheme.fixed_price === "object"
          ? scheme.fixed_price as Record<string, unknown>
          : {};
      return {
        sequence: typeof c.sequence === "number" ? c.sequence : 0,
        tenure_type: typeof c.tenure_type === "string" ? c.tenure_type : "?",
        interval_unit: typeof frequency.interval_unit === "string"
          ? frequency.interval_unit
          : "?",
        interval_count: typeof frequency.interval_count === "number"
          ? frequency.interval_count
          : 1,
        total_cycles: typeof c.total_cycles === "number" ? c.total_cycles : 0,
        price: typeof fixedPrice.value === "string" ? fixedPrice.value : null,
        currency: typeof fixedPrice.currency_code === "string"
          ? fixedPrice.currency_code
          : null,
      };
    });

  return {
    id: plan.id,
    name: typeof plan.name === "string" ? plan.name : "?",
    status: typeof plan.status === "string" ? plan.status : "?",
    product_id: typeof plan.product_id === "string" ? plan.product_id : "?",
    cycles,
  };
}

export interface PlanAssessment {
  pass: boolean;
  problems: string[];
}

/**
 * Compare a plan summary against an expectation. Checks the REGULAR cycle's
 * price/currency/interval and the plan status; trial cycles are informational
 * (the 3-day Pro trial is expected on monthly Pro).
 */
export function assessPlan(
  summary: PlanSummary,
  expectation: PlanExpectation,
): PlanAssessment {
  const problems: string[] = [];

  if (summary.status !== "ACTIVE") {
    problems.push(`status is ${summary.status}, expected ACTIVE`);
  }

  const regular = summary.cycles.find((c) => c.tenure_type === "REGULAR");
  if (!regular) {
    problems.push("no REGULAR billing cycle found");
    return { pass: false, problems };
  }

  if (regular.price === null || Number(regular.price) !== Number(expectation.price)) {
    problems.push(
      `regular price is ${regular.currency ?? "?"} ${regular.price ?? "?"}, expected ${expectation.currency} ${expectation.price}`,
    );
  } else if (regular.currency !== expectation.currency) {
    problems.push(
      `currency is ${regular.currency ?? "?"}, expected ${expectation.currency}`,
    );
  }

  if (regular.interval_unit !== expectation.interval_unit) {
    problems.push(
      `interval is ${regular.interval_unit}, expected ${expectation.interval_unit}`,
    );
  }

  return { pass: problems.length === 0, problems };
}

// ---------------------------------------------------------------------------
// PayPal API (main only)
// ---------------------------------------------------------------------------

function apiBase(env: string): string {
  return env === "sandbox"
    ? "https://api-m.sandbox.paypal.com"
    : "https://api-m.paypal.com";
}

async function getOAuthToken(
  base: string,
  clientId: string,
  clientSecret: string,
): Promise<string> {
  const creds = btoa(`${clientId}:${clientSecret}`);
  const res = await fetch(`${base}/v1/oauth2/token`, {
    method: "POST",
    headers: {
      Authorization: `Basic ${creds}`,
      "Content-Type": "application/x-www-form-urlencoded",
    },
    body: "grant_type=client_credentials",
  });
  if (!res.ok) {
    const body = await res.text().catch(() => "");
    throw new Error(
      `PayPal OAuth failed: HTTP ${res.status} ${body.slice(0, 200)}`,
    );
  }
  const json = await res.json() as { access_token?: string };
  if (!json.access_token) throw new Error("PayPal OAuth missing access_token");
  return json.access_token;
}

function printPlan(summary: PlanSummary, expectation: PlanExpectation | null) {
  const header = expectation
    ? `${expectation.label} (${summary.id})`
    : summary.id;
  console.log(`\n=== ${header} ===`);
  console.log(`  name:       ${summary.name}`);
  console.log(`  status:     ${summary.status}`);
  console.log(`  product_id: ${summary.product_id}`);
  for (const c of summary.cycles) {
    const price = c.price !== null ? `${c.currency} ${c.price}` : "(no price)";
    console.log(
      `  cycle #${c.sequence} ${c.tenure_type}: every ${c.interval_count} ${c.interval_unit}, ` +
        `total_cycles=${c.total_cycles}, ${price}`,
    );
  }
  if (expectation) {
    const assessment = assessPlan(summary, expectation);
    if (assessment.pass) {
      console.log(
        `  PASS - regular cycle is ${expectation.currency} ${expectation.price} / ${expectation.interval_unit}, ACTIVE`,
      );
    } else {
      console.log("  FAIL:");
      for (const p of assessment.problems) console.log(`    - ${p}`);
    }
  } else {
    console.log(
      "  (no expectation attached - verify manually: annual plans should be " +
        "PHP 490 (Pro) / PHP 990 (Max) on a YEAR interval)",
    );
  }
}

if (import.meta.main) {
  const clientId = Deno.env.get("PAYPAL_CLIENT_ID");
  const clientSecret = Deno.env.get("PAYPAL_CLIENT_SECRET");
  if (!clientId || !clientSecret) {
    console.error(
      "Set PAYPAL_CLIENT_ID and PAYPAL_CLIENT_SECRET env vars first (REST API app credentials).",
    );
    Deno.exit(2);
  }
  const env = (Deno.env.get("PAYPAL_ENV") ??
    Deno.env.get("PAYPAL_ENVIRONMENT") ?? "live").toLowerCase();
  const base = apiBase(env);
  console.log(`PayPal environment: ${env} (${base})`);

  const checks = parsePlanArgs(Deno.args);
  if (checks.length === 0) {
    console.error("No plan ids to check.");
    Deno.exit(2);
  }

  const token = await getOAuthToken(base, clientId, clientSecret);
  let failures = 0;

  for (const check of checks) {
    const res = await fetch(
      `${base}/v1/billing/plans/${encodeURIComponent(check.planId)}`,
      { headers: { Authorization: `Bearer ${token}` } },
    );
    if (!res.ok) {
      const body = await res.text().catch(() => "");
      console.log(`\n=== ${check.planId} ===`);
      console.log(
        `  FAIL - HTTP ${res.status} fetching plan (wrong environment or plan id?): ${body.slice(0, 200)}`,
      );
      failures++;
      continue;
    }
    const summary = summarizePlan(await res.json());
    if (!summary) {
      console.log(`\n=== ${check.planId} ===`);
      console.log("  FAIL - unrecognized plan response shape");
      failures++;
      continue;
    }
    printPlan(summary, check.expectation);
    if (check.expectation && !assessPlan(summary, check.expectation).pass) {
      failures++;
    }
  }

  console.log(
    failures === 0
      ? "\nAll checked plans match expectations."
      : `\n${failures} plan(s) failed verification - see details above.`,
  );
  Deno.exit(failures === 0 ? 0 : 1);
}
