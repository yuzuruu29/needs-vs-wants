// Needs vs Wants - unit tests for the PayPal plan verification tool helpers
// Run: deno test supabase/tools/verify_paypal_plans.test.ts
import {
  assertEquals,
} from "https://deno.land/std@0.224.0/assert/mod.ts";
import {
  assessPlan,
  KNOWN_MONTHLY_PLANS,
  MAX_ANNUAL_EXPECTATION,
  parsePlanArgs,
  PRO_ANNUAL_EXPECTATION,
  summarizePlan,
} from "./verify_paypal_plans.ts";

const PRO_MONTHLY_ID = "P-701099249D7315939NJ3BQHQ";
const MAX_MONTHLY_ID = "P-2GK5612954300654GNJ3BSBQ";

// Realistic GET /v1/billing/plans/{id} response shape (trial + regular).
const PRO_PLAN_JSON = {
  id: PRO_MONTHLY_ID,
  product_id: "PROD-123",
  name: "Needs vs Wants Pro",
  status: "ACTIVE",
  billing_cycles: [
    {
      frequency: { interval_unit: "DAY", interval_count: 3 },
      tenure_type: "TRIAL",
      sequence: 1,
      total_cycles: 1,
    },
    {
      pricing_scheme: {
        version: 2,
        fixed_price: { value: "49.0", currency_code: "PHP" },
      },
      frequency: { interval_unit: "MONTH", interval_count: 1 },
      tenure_type: "REGULAR",
      sequence: 2,
      total_cycles: 0,
    },
  ],
};

// ---------------------------------------------------------------------------
// parsePlanArgs
// ---------------------------------------------------------------------------

Deno.test("parsePlanArgs: no args checks the two known monthly plans", () => {
  const checks = parsePlanArgs([]);
  assertEquals(checks.length, 2);
  assertEquals(checks[0].planId, PRO_MONTHLY_ID);
  assertEquals(checks[0].expectation, KNOWN_MONTHLY_PLANS[PRO_MONTHLY_ID]);
  assertEquals(checks[1].planId, MAX_MONTHLY_ID);
});

Deno.test("parsePlanArgs: annual flags attach the 490/990 YEAR expectations", () => {
  const checks = parsePlanArgs([
    "--pro-annual=P-PROANNUAL1",
    "--max-annual=P-MAXANNUAL1",
  ]);
  assertEquals(checks.length, 4); // 2 monthly + 2 annual
  assertEquals(checks[2], {
    planId: "P-PROANNUAL1",
    expectation: PRO_ANNUAL_EXPECTATION,
  });
  assertEquals(checks[3], {
    planId: "P-MAXANNUAL1",
    expectation: MAX_ANNUAL_EXPECTATION,
  });
});

Deno.test("parsePlanArgs: bare plan ids are checked without expectations", () => {
  const checks = parsePlanArgs(["--skip-monthly", "P-SOMEOTHER"]);
  assertEquals(checks, [{ planId: "P-SOMEOTHER", expectation: null }]);
});

Deno.test("parsePlanArgs: --skip-monthly drops the defaults", () => {
  assertEquals(parsePlanArgs(["--skip-monthly"]), []);
});

// ---------------------------------------------------------------------------
// summarizePlan
// ---------------------------------------------------------------------------

Deno.test("summarizePlan: maps a realistic plan response", () => {
  const summary = summarizePlan(PRO_PLAN_JSON);
  assertEquals(summary, {
    id: PRO_MONTHLY_ID,
    name: "Needs vs Wants Pro",
    status: "ACTIVE",
    product_id: "PROD-123",
    cycles: [
      {
        sequence: 1,
        tenure_type: "TRIAL",
        interval_unit: "DAY",
        interval_count: 3,
        total_cycles: 1,
        price: null,
        currency: null,
      },
      {
        sequence: 2,
        tenure_type: "REGULAR",
        interval_unit: "MONTH",
        interval_count: 1,
        total_cycles: 0,
        price: "49.0",
        currency: "PHP",
      },
    ],
  });
});

Deno.test("summarizePlan: malformed responses yield null", () => {
  assertEquals(summarizePlan(null), null);
  assertEquals(summarizePlan("junk"), null);
  assertEquals(summarizePlan({}), null);
  assertEquals(summarizePlan({ name: "no id" }), null);
});

// ---------------------------------------------------------------------------
// assessPlan
// ---------------------------------------------------------------------------

const PRO_EXPECTATION = KNOWN_MONTHLY_PLANS[PRO_MONTHLY_ID];

Deno.test("assessPlan: correct price/currency/interval/status passes ('49.0' == '49')", () => {
  const summary = summarizePlan(PRO_PLAN_JSON)!;
  assertEquals(assessPlan(summary, PRO_EXPECTATION), {
    pass: true,
    problems: [],
  });
});

Deno.test("assessPlan: stale price fails with a price problem", () => {
  const stale = summarizePlan({
    ...PRO_PLAN_JSON,
    billing_cycles: [
      {
        pricing_scheme: {
          fixed_price: { value: "199.0", currency_code: "PHP" },
        },
        frequency: { interval_unit: "MONTH", interval_count: 1 },
        tenure_type: "REGULAR",
        sequence: 1,
        total_cycles: 0,
      },
    ],
  })!;
  const assessment = assessPlan(stale, PRO_EXPECTATION);
  assertEquals(assessment.pass, false);
  assertEquals(assessment.problems.length, 1);
  assertEquals(assessment.problems[0].includes("199"), true);
  assertEquals(assessment.problems[0].includes("expected PHP 49"), true);
});

Deno.test("assessPlan: wrong interval fails (annual expectation vs MONTH plan)", () => {
  const summary = summarizePlan(PRO_PLAN_JSON)!;
  const assessment = assessPlan(summary, PRO_ANNUAL_EXPECTATION);
  assertEquals(assessment.pass, false);
  assertEquals(
    assessment.problems.some((p) => p.includes("interval is MONTH")),
    true,
  );
});

Deno.test("assessPlan: inactive plan or missing regular cycle fails", () => {
  const inactive = summarizePlan({ ...PRO_PLAN_JSON, status: "INACTIVE" })!;
  const inactiveAssessment = assessPlan(inactive, PRO_EXPECTATION);
  assertEquals(inactiveAssessment.pass, false);
  assertEquals(
    inactiveAssessment.problems.some((p) => p.includes("INACTIVE")),
    true,
  );

  const trialOnly = summarizePlan({
    ...PRO_PLAN_JSON,
    billing_cycles: [
      {
        frequency: { interval_unit: "DAY", interval_count: 3 },
        tenure_type: "TRIAL",
        sequence: 1,
        total_cycles: 1,
      },
    ],
  })!;
  const noRegular = assessPlan(trialOnly, PRO_EXPECTATION);
  assertEquals(noRegular.pass, false);
  assertEquals(
    noRegular.problems.some((p) => p.includes("no REGULAR billing cycle")),
    true,
  );
});
