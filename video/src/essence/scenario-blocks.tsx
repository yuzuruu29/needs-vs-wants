import React from "react";
import { AbsoluteFill, Sequence, interpolate, useCurrentFrame } from "remotion";
import { COLORS, EASE_EDIT, EASE_SEAL, FONT_STACK, SERIF_STACK } from "../theme";
import { CONTENT } from "../layout";
import { LogScreen } from "../ui/LogScreen";
import { SummaryScreen } from "../ui/SummaryScreen";
import { PremiumDialog } from "../ui/primitives";
import { LedgerRowData } from "../ui/ledger";
import { FadeUp, prog } from "./blocks";

/**
 * Shared primitives for the real-life scenario cuts (Sweldo Day, Petsa de
 * Peligro). Same editorial system as the campaign; the phone always shows
 * honest 2.0.14 behavior.
 */

export const peso = (cents: number) =>
  "₱" +
  (cents / 100).toLocaleString("en-PH", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });

/** Scene shell: Sequence + soft fade from/to the paper void. */
export const ScenarioScene: React.FC<
  React.PropsWithChildren<{ from: number; duration: number; fadeIn?: number; fadeOut?: number }>
> = ({ from, duration, fadeIn = 10, fadeOut = 12, children }) => (
  <Sequence from={from} durationInFrames={duration}>
    <ScenarioSceneFade duration={duration} fadeIn={fadeIn} fadeOut={fadeOut}>
      {children}
    </ScenarioSceneFade>
  </Sequence>
);

const ScenarioSceneFade: React.FC<
  React.PropsWithChildren<{ duration: number; fadeIn: number; fadeOut: number }>
> = ({ duration, fadeIn, fadeOut, children }) => {
  const f = useCurrentFrame();
  const opacity = prog(f, 0, fadeIn) * (1 - prog(f, duration - fadeOut, duration - 2));
  return <AbsoluteFill style={{ opacity }}>{children}</AbsoluteFill>;
};

/* ────────────────────────────────────────────────────────────────────────── */
/* Header chrome                                                              */
/* ────────────────────────────────────────────────────────────────────────── */

/** Small gilt pill for a time of day or a day count. */
export const TimeChip: React.FC<{ label: string; accent?: string }> = ({
  label,
  accent = COLORS.goldDeep,
}) => (
  <div
    style={{
      display: "inline-flex",
      alignItems: "center",
      gap: 12,
      padding: "12px 26px",
      borderRadius: 999,
      background: COLORS.card,
      border: "1.5px solid rgba(232,169,42,0.5)",
      boxShadow: "0 10px 26px -16px rgba(26,26,26,0.35)",
    }}
  >
    <div style={{ width: 9, height: 9, borderRadius: 5, background: accent }} />
    <span
      style={{
        fontFamily: FONT_STACK,
        fontSize: 28,
        fontWeight: 600,
        letterSpacing: 3,
        color: COLORS.ink,
        textTransform: "uppercase",
        fontVariantNumeric: "tabular-nums",
      }}
    >
      {label}
    </span>
  </div>
);

/** Top block for a scenario beat: time chip, serif line, moment dots. */
export const MomentHeader: React.FC<{
  chip: string;
  line: string;
  accent?: string;
  dots?: { count: number; active: number } | null;
  lineDelay?: number;
}> = ({ chip, line, accent, dots = null, lineDelay = 14 }) => (
  <div
    style={{
      position: "absolute",
      top: 148,
      left: 0,
      right: 0,
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      textAlign: "center",
      padding: "0 80px",
      gap: 26,
    }}
  >
    <FadeUp at={4}>
      <TimeChip label={chip} accent={accent} />
    </FadeUp>
    <FadeUp at={lineDelay}>
      <div
        style={{
          fontFamily: SERIF_STACK,
          fontSize: 58,
          fontWeight: 700,
          lineHeight: 1.12,
          color: COLORS.ink,
          maxWidth: 900,
        }}
      >
        {line}
      </div>
    </FadeUp>
    {dots ? (
      <FadeUp at={lineDelay + 10}>
        <div style={{ display: "flex", gap: 10, justifyContent: "center" }}>
          {Array.from({ length: dots.count }, (_, i) => (
            <div
              key={i}
              style={{
                width: i === dots.active ? 44 : 16,
                height: 5,
                borderRadius: 3,
                background: i === dots.active ? COLORS.gold : COLORS.dividerStrong,
              }}
            />
          ))}
        </div>
      </FadeUp>
    ) : null}
  </div>
);

/* ────────────────────────────────────────────────────────────────────────── */
/* Log moment demo                                                            */
/* ────────────────────────────────────────────────────────────────────────── */

export type Budget = {
  budgetCents: number;
  spentBeforeCents: number;
};

export type MomentSpec = {
  item: string;
  costDigits: string; // what gets typed, e.g. "180"
  costCents: number;
  type: "NEED" | "WANT";
  time: string; // ledger time, e.g. "10:12"
  dateLabel: string;
  priorRows: LedgerRowData[];
  sheetBefore: number;
  budget?: Budget | null;
  /** Local frame the type chip is pressed. Seal lands ~14f later. */
  chipAt?: number;
};

/**
 * One purchase, sealed on the real Log UI: fast type, one honest call,
 * row lands in the ledger, counters and (optionally) the budget meter move.
 */
export const MomentLog: React.FC<{ spec: MomentSpec }> = ({ spec }) => {
  const f = useCurrentFrame();
  const chipAt = spec.chipAt ?? 62;
  const sealAt = chipAt + 16;
  const typeItemStart = 12;
  const typeCostStart = typeItemStart + Math.ceil(spec.item.length * 2.3) + 8;

  const itemChars = Math.max(
    0,
    Math.min(spec.item.length, Math.floor((f - typeItemStart) / 2.3)),
  );
  const costChars = Math.max(
    0,
    Math.min(spec.costDigits.length, Math.floor((f - typeCostStart) / 3.4)),
  );

  const sealed = f >= sealAt;
  const sealP = prog(f, sealAt, sealAt + 14, EASE_SEAL);

  const rows: Array<{ row: LedgerRowData; style?: React.CSSProperties }> = [
    ...(sealed
      ? [
          {
            row: {
              id: "moment",
              time: spec.time,
              item: spec.item,
              cost: peso(spec.costCents),
              type: spec.type,
            },
            style: {
              opacity: Math.min(1, sealP * 1.7),
              transform: `translateY(${(1 - sealP) * -20}px) scale(${1.1 - sealP * 0.1})`,
              borderColor:
                spec.type === "WANT"
                  ? `rgba(200,16,46,${0.42 * (1 - sealP) + 0.08})`
                  : `rgba(11,107,58,${0.42 * (1 - sealP) + 0.1})`,
            },
          },
        ]
      : []),
    ...spec.priorRows.map((row) => ({ row })),
  ];

  const budget = spec.budget
    ? (() => {
        const spentCents = sealed
          ? Math.round(
              interpolate(sealP, [0, 1], [
                spec.budget!.spentBeforeCents,
                spec.budget!.spentBeforeCents + spec.costCents,
              ]),
            )
          : spec.budget!.spentBeforeCents;
        const fillPct = spentCents / spec.budget!.budgetCents;
        const leftCents = spec.budget!.budgetCents - spentCents;
        return {
          spent: peso(spentCents),
          budget: peso(spec.budget!.budgetCents),
          fillPct,
          over: leftCents < 0,
          remainLabel:
            leftCents >= 0
              ? `${peso(leftCents)} left today`
              : `${peso(-leftCents)} over today`,
        };
      })()
    : null;

  return (
    <LogScreen
      width={CONTENT.W}
      height={CONTENT.H}
      data={{
        dateLabel: spec.dateLabel,
        filledLabel: `${spec.sheetBefore + (sealed ? 1 : 0)} / 20`,
        ledgerTimeW: 92,
        budget,
        itemValue: sealed ? "" : spec.item.slice(0, itemChars),
        costValue: sealed ? "" : spec.costDigits.slice(0, costChars),
        focusedField: sealed
          ? null
          : f >= typeCostStart - 3
            ? "cost"
            : f >= typeItemStart - 3
              ? "item"
              : null,
        needSelected: !sealed && spec.type === "NEED" && f >= chipAt,
        wantSelected: !sealed && spec.type === "WANT" && f >= chipAt,
        rows,
      }}
    />
  );
};

/* ────────────────────────────────────────────────────────────────────────── */
/* Petsa day 3 — the temptation refused                                       */
/* ────────────────────────────────────────────────────────────────────────── */

/**
 * Milk tea typed, WANT selected, the over-budget dialog appears, and this
 * time the answer is Cancel: the dialog closes, the form clears, the meter
 * never moves, and no row is added.
 */
export const CancelGuardDemo: React.FC<{
  priorRows: LedgerRowData[];
  dateLabel: string;
}> = ({ priorRows, dateLabel }) => {
  const f = useCurrentFrame();

  const dialogIn = 34;
  const cancelAt = 96;
  const clearedAt = cancelAt + 10;

  const dialogP =
    prog(f, dialogIn, dialogIn + 12, EASE_SEAL) *
    (1 - prog(f, cancelAt, cancelAt + 10));
  const showDialog = f >= dialogIn && f < cancelAt + 10;
  const cleared = f >= clearedAt;

  return (
    <LogScreen
      width={CONTENT.W}
      height={CONTENT.H}
      data={{
        dateLabel,
        filledLabel: `${priorRows.length} / 20`,
        ledgerTimeW: 92,
        budget: {
          spent: "₱64.00",
          budget: "₱100.00",
          fillPct: 0.64,
          over: false,
          remainLabel: "₱36.00 left today",
        },
        itemValue: cleared ? "" : "Milk tea",
        costValue: cleared ? "" : "120",
        focusedField: null,
        needSelected: false,
        wantSelected: !cleared,
        rows: priorRows.map((row) => ({ row })),
      }}
      overlay={
        showDialog ? (
          <PremiumDialog
            eyebrow="DAILY BUDGET"
            title="Log anyway?"
            width={500}
            anim={{
              opacity: Math.min(1, dialogP * 1.4),
              scale: 0.94 + dialogP * 0.06,
            }}
            buttons={[
              { label: f >= cancelAt - 6 ? "Cancelling…" : "Cancel", danger: true },
              { label: "Log", ghost: true },
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
              <span style={{ color: COLORS.crimson, fontWeight: 600 }}>₱84.00 over</span> your
              ₱100 day.
            </div>
          </PremiumDialog>
        ) : null
      }
    />
  );
};

/* ────────────────────────────────────────────────────────────────────────── */
/* Summary + insight                                                          */
/* ────────────────────────────────────────────────────────────────────────── */

export const ScenarioSummaryDemo: React.FC<{
  needPctTo: number;
  totalCents: number;
  needsMoney: string;
  wantsMoney: string;
  period?: string;
}> = ({ needPctTo, totalCents, needsMoney, wantsMoney, period = "Day" }) => {
  const f = useCurrentFrame();
  const needPct = Math.round(
    interpolate(f, [10, 46], [100, needPctTo], {
      extrapolateLeft: "clamp",
      extrapolateRight: "clamp",
      easing: EASE_EDIT,
    }),
  );
  const shownCents = Math.round(
    interpolate(f, [10, 42], [0, totalCents], {
      extrapolateLeft: "clamp",
      extrapolateRight: "clamp",
      easing: EASE_EDIT,
    }),
  );
  return (
    <SummaryScreen
      width={CONTENT.W}
      height={CONTENT.H}
      data={{
        eyebrowText: "A 30-Day Trainer",
        periods: ["Day", "Week", "All (30d)"],
        period,
        needPct,
        needsMoney,
        wantsMoney,
        totalMoney: peso(shownCents),
        ctaLabel: "Log a Purchase",
      }}
    />
  );
};

/** Centered gilt verdict card for the scenario insight beat. */
export const InsightCard: React.FC<{
  eyebrow: string;
  headline: React.ReactNode;
  sub: string;
  stats?: Array<{ label: string; value: string; accent: string }>;
  width?: number;
}> = ({ eyebrow, headline, sub, stats, width = 860 }) => {
  const f = useCurrentFrame();
  const cardIn = prog(f, 4, 20);
  return (
    <div
      style={{
        width,
        background: COLORS.card,
        borderRadius: 34,
        border: "1.5px solid rgba(232,169,42,0.42)",
        boxShadow: "0 34px 70px -30px rgba(26,26,26,0.42)",
        padding: "58px 62px 54px",
        opacity: cardIn,
        transform: `translateY(${(1 - cardIn) * 40}px)`,
        position: "relative",
        overflow: "hidden",
        textAlign: "center",
      }}
    >
      <div
        style={{
          position: "absolute",
          inset: 0,
          background: "linear-gradient(180deg, rgba(255,255,255,0.7) 0%, rgba(255,255,255,0) 42%)",
        }}
      />
      <div style={{ position: "relative", display: "flex", flexDirection: "column", alignItems: "center" }}>
        <div
          style={{
            width: 54,
            height: 3,
            borderRadius: 2,
            background: COLORS.gold,
            boxShadow: "0 0 12px rgba(232,169,42,0.3)",
          }}
        />
        <div
          style={{
            marginTop: 18,
            fontFamily: FONT_STACK,
            fontSize: 26,
            fontWeight: 600,
            letterSpacing: 4.5,
            textTransform: "uppercase",
            color: COLORS.crimson,
          }}
        >
          {eyebrow}
        </div>
        <FadeUp at={14} style={{ marginTop: 26 }}>
          <div
            style={{
              fontFamily: SERIF_STACK,
              fontSize: 64,
              fontWeight: 700,
              lineHeight: 1.14,
              color: COLORS.ink,
            }}
          >
            {headline}
          </div>
        </FadeUp>
        <FadeUp at={26} style={{ marginTop: 22 }}>
          <div
            style={{
              fontFamily: FONT_STACK,
              fontSize: 33,
              lineHeight: 1.5,
              color: COLORS.sub,
              maxWidth: width - 180,
            }}
          >
            {sub}
          </div>
        </FadeUp>
        {stats ? (
          <FadeUp at={38} style={{ marginTop: 40 }}>
            <div style={{ display: "flex", gap: 18, justifyContent: "center" }}>
              {stats.map((s) => (
                <div
                  key={s.label}
                  style={{
                    padding: "18px 30px",
                    borderRadius: 20,
                    background: `${s.accent}0F`,
                    border: `1.5px solid ${s.accent}44`,
                    display: "flex",
                    flexDirection: "column",
                    alignItems: "center",
                    gap: 6,
                  }}
                >
                  <span
                    style={{
                      fontFamily: FONT_STACK,
                      fontSize: 21,
                      fontWeight: 600,
                      letterSpacing: 2.6,
                      color: COLORS.muted,
                      textTransform: "uppercase",
                    }}
                  >
                    {s.label}
                  </span>
                  <span
                    style={{
                      fontFamily: FONT_STACK,
                      fontSize: 37,
                      fontWeight: 700,
                      color: s.accent,
                      fontVariantNumeric: "tabular-nums",
                    }}
                  >
                    {s.value}
                  </span>
                </div>
              ))}
            </div>
          </FadeUp>
        ) : null}
      </div>
    </div>
  );
};
