import React from "react";
import { interpolate, useCurrentFrame } from "remotion";
import { COLORS, EASE_EDIT, EASE_SEAL } from "../theme";
import { CONTENT } from "../layout";
import { LogScreen } from "../ui/LogScreen";
import { SummaryScreen } from "../ui/SummaryScreen";
import { PremiumDialog } from "../ui/primitives";
import { LedgerRowData } from "../ui/ledger";
import { prog } from "./blocks";

/**
 * Animated in-phone demos for "The Choice" campaign. Each demo runs on the
 * local Sequence frame, so cuts can start them wherever they like.
 * All copy reflects the shipped 2.0.14 app (30-day trainer, 20-item sheets).
 */

const EYEBROW = "A 30-Day Trainer";
const PERIODS = ["Day", "Week", "All (30d)"];

const peso = (cents: number) =>
  "₱" +
  (cents / 100).toLocaleString("en-PH", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });

const BASE_ROWS: LedgerRowData[] = [
  { id: "r2", time: "8:05", item: "Jeep fare", cost: "₱26.00", type: "NEED" },
  { id: "r1", time: "7:12", item: "Rice (5kg)", cost: "₱280.00", type: "NEED" },
];

/* ────────────────────────────────────────────────────────────────────────── */
/* 01 · LOG — type the item, make the call, seal the row                      */
/* ────────────────────────────────────────────────────────────────────────── */

export const LogDemo: React.FC = () => {
  const f = useCurrentFrame();

  const ITEM = "Milk tea";
  const COST = "120";
  const typeItemStart = 14;
  const typeCostStart = 52;
  const chipAt = 78;
  const sealAt = 96;

  const itemChars = Math.max(
    0,
    Math.min(ITEM.length, Math.floor((f - typeItemStart) / 3.4)),
  );
  const costChars = Math.max(
    0,
    Math.min(COST.length, Math.floor((f - typeCostStart) / 4.5)),
  );

  const sealed = f >= sealAt;
  const sealP = prog(f, sealAt, sealAt + 14, EASE_SEAL);

  const rows: Array<{ row: LedgerRowData; style?: React.CSSProperties }> = [
    ...(sealed
      ? [
          {
            row: {
              id: "new",
              time: "9:41",
              item: ITEM,
              cost: "₱120.00",
              type: "WANT" as const,
            },
            style: {
              opacity: Math.min(1, sealP * 1.7),
              transform: `translateY(${(1 - sealP) * -20}px) scale(${1.1 - sealP * 0.1})`,
              boxShadow:
                sealP < 1
                  ? `0 ${10 * (1 - sealP)}px ${26 * (1 - sealP)}px -12px rgba(200,16,46,0.4)`
                  : undefined,
              borderColor: `rgba(200,16,46,${0.42 * (1 - sealP) + 0.08})`,
            },
          },
        ]
      : []),
    ...BASE_ROWS.map((row) => ({ row })),
  ];

  return (
    <LogScreen
      width={CONTENT.W}
      height={CONTENT.H}
      data={{
        dateLabel: "TODAY  ·  Aug 12, 2026",
        filledLabel: sealed ? "3 / 20" : "2 / 20",
        itemValue: sealed ? "" : ITEM.slice(0, itemChars),
        costValue: sealed ? "" : COST.slice(0, costChars),
        focusedField: sealed
          ? null
          : f >= typeCostStart - 4
            ? "cost"
            : f >= typeItemStart - 4
              ? "item"
              : null,
        needSelected: false,
        wantSelected: !sealed && f >= chipAt,
        rows,
      }}
    />
  );
};

/* ────────────────────────────────────────────────────────────────────────── */
/* 02 · SEE — the honest split sweeps in                                      */
/* ────────────────────────────────────────────────────────────────────────── */

export const SummaryDemo: React.FC = () => {
  const f = useCurrentFrame();

  // The red creeps in: all-green ring resolves to the honest 64/36 split.
  const needPct = Math.round(
    interpolate(f, [10, 48], [100, 64], {
      extrapolateLeft: "clamp",
      extrapolateRight: "clamp",
      easing: EASE_EDIT,
    }),
  );
  const totalCents = Math.round(
    interpolate(f, [10, 44], [0, 531000], {
      extrapolateLeft: "clamp",
      extrapolateRight: "clamp",
      easing: EASE_EDIT,
    }),
  );
  const ctaPulse =
    f > 70 ? 0.5 + Math.sin((f - 70) * 0.16) * 0.5 : 0;

  return (
    <SummaryScreen
      width={CONTENT.W}
      height={CONTENT.H}
      data={{
        eyebrowText: EYEBROW,
        periods: PERIODS,
        period: "Week",
        needPct,
        needsMoney: "₱3,398.40",
        wantsMoney: "₱1,911.60",
        totalMoney: peso(totalCents),
        ctaLabel: "Log a Purchase",
        ctaPulse: ctaPulse * 0.6,
      }}
    />
  );
};

/* ────────────────────────────────────────────────────────────────────────── */
/* 03 · GUARD — the daily line and the conscious overspend                    */
/* ────────────────────────────────────────────────────────────────────────── */

export const GuardDemo: React.FC = () => {
  const f = useCurrentFrame();

  const dialogIn = 16;
  const confirmAt = 72;
  const sealedAt = 86;

  const dialogP =
    prog(f, dialogIn, dialogIn + 12, EASE_SEAL) *
    (1 - prog(f, sealedAt - 4, sealedAt + 4));
  const showDialog = f >= dialogIn && f < sealedAt + 4;
  const sealed = f >= sealedAt;
  const sealP = prog(f, sealedAt, sealedAt + 14, EASE_SEAL);

  // Meter: 77% -> pauses under the dialog -> crosses to over after Log.
  const fillPct = sealed
    ? interpolate(sealP, [0, 1], [0.775, 1])
    : interpolate(f, [0, 12], [0.62, 0.775], {
        extrapolateLeft: "clamp",
        extrapolateRight: "clamp",
        easing: EASE_EDIT,
      });
  const spentCents = sealed
    ? Math.round(interpolate(sealP, [0, 1], [38750, 50750]))
    : 38750;

  const rows: Array<{ row: LedgerRowData; style?: React.CSSProperties }> = [
    ...(sealed
      ? [
          {
            row: {
              id: "new",
              time: "9:41",
              item: "Milk tea",
              cost: "₱120.00",
              type: "WANT" as const,
            },
            style: {
              opacity: Math.min(1, sealP * 1.7),
              transform: `translateY(${(1 - sealP) * -18}px) scale(${1.08 - sealP * 0.08})`,
            },
          },
        ]
      : []),
    ...BASE_ROWS.map((row) => ({ row })),
  ];

  return (
    <LogScreen
      width={CONTENT.W}
      height={CONTENT.H}
      data={{
        dateLabel: "TODAY  ·  Aug 12, 2026",
        filledLabel: sealed ? "3 / 20" : "2 / 20",
        budget: {
          spent: peso(spentCents),
          budget: "₱500.00",
          fillPct,
          over: sealed && sealP > 0.55,
          remainLabel:
            sealed && sealP > 0.55 ? "₱7.50 over today, on purpose" : "₱112.50 left today",
        },
        itemValue: sealed ? "" : "Milk tea",
        costValue: sealed ? "" : "120",
        focusedField: null,
        needSelected: false,
        wantSelected: !sealed,
        rows,
      }}
      overlay={
        showDialog ? (
          <PremiumDialog
            eyebrow="DAILY BUDGET"
            title="Log anyway?"
            width={500}
            anim={{ opacity: Math.min(1, dialogP * 1.4), scale: 0.94 + dialogP * 0.06 }}
            buttons={[
              { label: "Cancel", ghost: true },
              { label: f >= confirmAt ? "Logging…" : "Log", danger: false },
            ]}
          >
            <div
              style={{
                fontSize: 28,
                lineHeight: 1.5,
                color: COLORS.sub,
                fontVariantNumeric: "tabular-nums",
              }}
            >
              Milk tea · ₱120.00 puts you{" "}
              <span style={{ color: COLORS.crimson, fontWeight: 600 }}>₱7.50 over</span> your
              ₱500 day.
            </div>
          </PremiumDialog>
        ) : null
      }
    />
  );
};
