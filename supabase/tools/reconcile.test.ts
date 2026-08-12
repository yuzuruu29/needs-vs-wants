// Needs vs Wants - unit tests for the reconciliation tool helpers
// Run: deno test supabase/tools/reconcile.test.ts
import {
  assertEquals,
} from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  type EntitlementReconRow,
  extractProviderRows,
  isPaidStatus,
  type LedgerRow,
  parseCsv,
  reconcile,
} from "./reconcile.ts";

// ---------------------------------------------------------------------------
// parseCsv
// ---------------------------------------------------------------------------

Deno.test("parseCsv: plain rows, CRLF, and trailing newline", () => {
  assertEquals(parseCsv("a,b,c\r\n1,2,3\n"), [
    ["a", "b", "c"],
    ["1", "2", "3"],
  ]);
});

Deno.test("parseCsv: quoted fields with commas, quotes, and newlines", () => {
  const text = 'id,desc\npay_1,"hello, ""world""\nsecond line"\n';
  assertEquals(parseCsv(text), [
    ["id", "desc"],
    ["pay_1", 'hello, "world"\nsecond line'],
  ]);
});

Deno.test("parseCsv: empty input and blank lines yield no rows", () => {
  assertEquals(parseCsv(""), []);
  assertEquals(parseCsv("\n\n"), []);
});

// ---------------------------------------------------------------------------
// extractProviderRows
// ---------------------------------------------------------------------------

Deno.test("extractProviderRows: finds paymongo pay_ ids in any column", () => {
  const csv = parseCsv(
    "Created,Reference,Payment ID,Status\n" +
      "2026-08-10,nvw_abc,pay_abc123,Paid\n" +
      "2026-08-11,nvw_def,pay_def456,Refunded\n",
  );
  assertEquals(extractProviderRows(csv, "paymongo"), [
    { id: "pay_abc123", status: "Paid" },
    { id: "pay_def456", status: "Refunded" },
  ]);
});

Deno.test("extractProviderRows: finds paypal I- subscription ids and dedupes", () => {
  const csv = parseCsv(
    "Subscription ID,Plan,State\n" +
      "I-ABC123XYZ,Pro monthly,ACTIVE\n" +
      "I-ABC123XYZ,Pro monthly,ACTIVE\n" +
      "I-DEF456UVW,Max monthly,CANCELLED\n",
  );
  assertEquals(extractProviderRows(csv, "paypal"), [
    { id: "I-ABC123XYZ", status: "ACTIVE" },
    { id: "I-DEF456UVW", status: "CANCELLED" },
  ]);
});

Deno.test("extractProviderRows: no status column falls back to empty status", () => {
  const csv = parseCsv("col_a,col_b\nx,pay_only1\n");
  assertEquals(extractProviderRows(csv, "paymongo"), [
    { id: "pay_only1", status: "" },
  ]);
});

Deno.test("isPaidStatus: excludes refunds/failures/pending, keeps paid/unknown", () => {
  assertEquals(isPaidStatus("Paid"), true);
  assertEquals(isPaidStatus("succeeded"), true);
  assertEquals(isPaidStatus("ACTIVE"), true);
  assertEquals(isPaidStatus(""), true); // unknown -> surface for review
  assertEquals(isPaidStatus("Refunded"), false);
  assertEquals(isPaidStatus("failed"), false);
  assertEquals(isPaidStatus("Pending"), false);
  assertEquals(isPaidStatus("CANCELLED"), false);
  assertEquals(isPaidStatus("expired"), false);
});

// ---------------------------------------------------------------------------
// reconcile
// ---------------------------------------------------------------------------

const NOW = "2026-08-13T00:00:00.000Z";
const FUTURE = "2026-09-13T00:00:00.000Z";
const PAST = "2026-07-01T00:00:00.000Z";

function ledgerRow(partial: Partial<LedgerRow>): LedgerRow {
  return {
    id: "pay_x",
    user_id: "user-1",
    provider: "paymongo",
    checkout_session_id: null,
    status: "paid",
    created_at: PAST,
    ...partial,
  };
}

function entRow(partial: Partial<EntitlementReconRow>): EntitlementReconRow {
  return {
    user_id: "user-1",
    is_pro: true,
    tier: "pro",
    paid_until: FUTURE,
    trial_ends_at: null,
    provider: "paymongo",
    status: "paid",
    ...partial,
  };
}

Deno.test("reconcile: everything matching yields an empty report", () => {
  const report = reconcile(
    { paymongo: [{ id: "pay_1", status: "Paid" }] },
    [ledgerRow({ id: "pay_1" })],
    [entRow({})],
    NOW,
  );
  assertEquals(report.paidNoLedger, []);
  assertEquals(report.ledgerNoEntitlement, []);
  assertEquals(report.entitledNoPayment, []);
});

Deno.test("reconcile: paid at provider but missing from ledger is flagged", () => {
  const report = reconcile(
    {
      paymongo: [
        { id: "pay_recorded", status: "Paid" },
        { id: "pay_missing", status: "Paid" },
        { id: "pay_refunded", status: "Refunded" }, // excluded: not money kept
      ],
    },
    [ledgerRow({ id: "pay_recorded" })],
    [entRow({})],
    NOW,
  );
  assertEquals(report.paidNoLedger, [
    { provider: "paymongo", id: "pay_missing", status: "Paid" },
  ]);
});

Deno.test("reconcile: paypal subscription ids match the ledger checkout_session_id", () => {
  const report = reconcile(
    {
      paypal: [
        { id: "I-KNOWN1", status: "ACTIVE" },
        { id: "I-MISSING1", status: "ACTIVE" },
      ],
    },
    [
      ledgerRow({
        id: "WH-evt-1",
        provider: "paypal",
        checkout_session_id: "I-KNOWN1",
      }),
    ],
    [entRow({ provider: "paypal" })],
    NOW,
  );
  assertEquals(report.paidNoLedger, [
    { provider: "paypal", id: "I-MISSING1", status: "ACTIVE" },
  ]);
});

Deno.test("reconcile: ledger row for a user with no entitlements row is flagged", () => {
  const report = reconcile(
    {},
    [ledgerRow({ user_id: "user-ghost" })],
    [entRow({ user_id: "user-other" })],
    NOW,
  );
  assertEquals(report.ledgerNoEntitlement.length, 1);
  assertEquals(
    report.ledgerNoEntitlement[0].note,
    "no entitlements row for this user",
  );
});

Deno.test("reconcile: ledger row for a user whose row shows no grant is flagged", () => {
  const report = reconcile(
    {},
    [ledgerRow({})],
    [entRow({ is_pro: false, paid_until: null, trial_ends_at: null })],
    NOW,
  );
  assertEquals(report.ledgerNoEntitlement.length, 1);
  assertEquals(
    report.ledgerNoEntitlement[0].note.includes("no grant"),
    true,
  );
});

Deno.test("reconcile: expired entitlement after a recorded payment is NOT flagged", () => {
  // A legitimately lapsed subscriber: payment recorded, paid window over.
  const report = reconcile(
    {},
    [ledgerRow({})],
    [entRow({ paid_until: PAST })],
    NOW,
  );
  assertEquals(report.ledgerNoEntitlement, []);
  assertEquals(report.entitledNoPayment, []);
});

Deno.test("reconcile: active paid entitlement without any ledger row is flagged", () => {
  const report = reconcile(
    {},
    [],
    [entRow({})],
    NOW,
  );
  assertEquals(report.entitledNoPayment.length, 1);
  assertEquals(
    report.entitledNoPayment[0].note,
    "no ledger row for this user",
  );
});

Deno.test("reconcile: pre-cutover paypal entitlement is flagged with the EXPECTED note", () => {
  const report = reconcile(
    {},
    [],
    [entRow({ provider: "paypal" })],
    NOW,
  );
  assertEquals(report.entitledNoPayment.length, 1);
  assertEquals(
    report.entitledNoPayment[0].note.includes("EXPECTED"),
    true,
  );
});

Deno.test("reconcile: trial-only and non-payment-provider rows are not flagged", () => {
  const report = reconcile(
    {},
    [],
    [
      entRow({ paid_until: null, trial_ends_at: FUTURE }), // active trial
      entRow({ user_id: "user-2", provider: "google" }), // Play grant: no ledger by design
      entRow({ user_id: "user-3", is_pro: false, paid_until: null }), // free row
    ],
    NOW,
  );
  assertEquals(report.entitledNoPayment, []);
});

Deno.test("reconcile: lifetime (null paid_until, no trial) paid row without ledger is flagged", () => {
  const report = reconcile(
    {},
    [],
    [entRow({ paid_until: null, trial_ends_at: null })],
    NOW,
  );
  assertEquals(report.entitledNoPayment.length, 1);
});
