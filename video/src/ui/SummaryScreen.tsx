import React from "react";
import { COLORS, FONT_STACK, SERIF_STACK } from "../theme";
import { Eyebrow, GiltRule, GiltButton } from "./primitives";
import { Donut } from "./donut";
import { BudgetMeter } from "./budget";

function moneySize(text: string, base: number): number {
  const len = text.length;
  if (len <= 9) return base;
  if (len <= 12) return base * 0.82;
  if (len <= 15) return base * 0.68;
  return base * 0.56;
}

export type SummaryScreenData = {
  needPct: number; // 0..100
  needsMoney: string;
  wantsMoney: string;
  totalMoney: string;
  period?: "Day" | "Week" | "All (35d)";
  streakDays?: number;
  ctaLabel?: string;
  ctaPulse?: number; // 0..1 pulse intensity
  budget?: {
    spent: string;
    budget: string;
    fillPct: number;
    over?: boolean;
    remainLabel: string;
  } | null;
  rangeLabel?: string;
};

export const SummaryScreen: React.FC<{
  data: SummaryScreenData;
  width: number;
  height: number;
}> = ({ data, width, height }) => {
  const pad = 20;

  const needSweep = (data.needPct / 100) * 360;

  return (
    <div
      style={{
        width,
        height,
        background: "linear-gradient(180deg, #FAFAF7 0%, #FAFAF7 55%, #F7F4EC 100%)",
        padding: `26px ${pad}px 30px`,
        display: "flex",
        flexDirection: "column",
        overflow: "hidden",
        fontFamily: FONT_STACK,
      }}
    >
      {/* Header: editorial serif title + tools */}
      <div style={{ display: "flex", justifyContent: "space-between", alignItems: "flex-start" }}>
        <div style={{ flex: 1, minWidth: 0 }}>
          <Eyebrow text="A 35-Day Trainer" size={24} />
          <div
            style={{
              marginTop: 10,
              fontFamily: SERIF_STACK,
              fontSize: 64,
              lineHeight: 1.02,
              letterSpacing: 1,
              color: COLORS.ink,
              fontWeight: 700,
            }}
          >
            NEEDS
            <br />
            vs WANTS
          </div>
          <GiltRule width={44} />
          <div
            style={{
              marginTop: 8,
              color: COLORS.crimson,
              fontFamily: FONT_STACK,
              fontSize: 32,
              fontWeight: 600,
              letterSpacing: 0.5,
            }}
          >
            Expense Tracker
          </div>
        </div>
        {/* tools */}
        <div style={{ display: "flex", gap: 6, paddingTop: 6 }}>
          <svg width="44" height="44" viewBox="0 0 24 24" fill="none" stroke={COLORS.crimson} strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
            <circle cx="12" cy="12" r="9" />
            <path d="M12 11v5M12 8h.01" />
          </svg>
          <svg width="44" height="44" viewBox="0 0 24 24" fill="none" stroke={COLORS.crimson} strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
            <circle cx="18" cy="5" r="3" />
            <circle cx="6" cy="12" r="3" />
            <circle cx="18" cy="19" r="3" />
            <path d="M8.6 13.5l6.8 4M15.4 6.5l-6.8 4" />
          </svg>
        </div>
      </div>

      {/* Period rotor */}
      <div
        style={{
          marginTop: 22,
          display: "flex",
          gap: 6,
          background: COLORS.card,
          border: `1px solid ${COLORS.divider}`,
          borderRadius: 18,
          padding: 6,
        }}
      >
        {(["Day", "Week", "All (35d)"] as const).map((p) => {
          const sel = p === (data.period ?? "Day");
          return (
            <div
              key={p}
              style={{
                flex: 1,
                height: 58,
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                borderRadius: 12,
                background: sel ? COLORS.crimson : "transparent",
                color: sel ? COLORS.card : COLORS.sub,
                fontSize: 28,
                fontWeight: sel ? 600 : 500,
                letterSpacing: sel ? 0.6 : 0.2,
                fontVariantNumeric: "tabular-nums",
              }}
            >
              {p}
            </div>
          );
        })}
      </div>

      {/* Optional budget meter */}
      {data.budget ? (
        <div style={{ marginTop: 20 }}>
          <BudgetMeter
            spent={data.budget.spent}
            budget={data.budget.budget}
            fillPct={data.budget.fillPct}
            over={data.budget.over}
            remainLabel={data.budget.remainLabel}
          />
        </div>
      ) : null}

      {/* Donut hero */}
      <div
        style={{
          marginTop: 26,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          flex: 1,
          justifyContent: "center",
        }}
      >
        <Donut
          needSweep={needSweep}
          size={430}
          ringWidth={48}
          centerEyebrow="TOTAL"
          centerMoney={data.totalMoney}
          centerMoneySize={moneySize(data.totalMoney, 58)}
        />
        <div style={{ marginTop: 18, display: "flex", alignItems: "center", gap: 22 }}>
          <LegendDot color={COLORS.green} label={`Need ${data.needPct}%`} />
          <div style={{ width: 3, height: 30, background: COLORS.divider }} />
          <LegendDot color={COLORS.crimson} label={`Want ${100 - data.needPct}%`} />
        </div>
      </div>

      {/* Stat cards */}
      <div style={{ marginTop: 8, display: "flex", gap: 12 }}>
        <StatCard label="NEEDS" value={data.needsMoney} accent={COLORS.green} pct={data.needPct} />
        <StatCard label="WANTS" value={data.wantsMoney} accent={COLORS.crimson} pct={100 - data.needPct} />
        <StatCard label="NEED %" value={`${data.needPct}%`} accent={COLORS.goldDeep} pct={data.needPct} />
      </div>

      {/* CTA */}
      <div style={{ marginTop: 18 }}>
        <GiltButton
          label={data.ctaLabel ?? "Log an expense"}
          height={68}
          style={{ transform: `scale(${1 + 0.035 * (data.ctaPulse ?? 0)})` }}
        />
      </div>
    </div>
  );
};

const LegendDot: React.FC<{ color: string; label: string }> = ({ color, label }) => (
  <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
    <div style={{ width: 18, height: 18, borderRadius: 9, background: color }} />
    <span style={{ fontSize: 30, color: COLORS.sub, letterSpacing: 0.5 }}>{label}</span>
  </div>
);

const StatCard: React.FC<{ label: string; value: string; accent: string; pct: number }> = ({
  label,
  value,
  accent,
  pct,
}) => (
  <div
    style={{
      flex: 1,
      background: COLORS.card,
      border: `1px solid ${COLORS.divider}`,
      borderRadius: 20,
      padding: "18px 16px",
    }}
  >
    <Eyebrow text={label} size={19} color={COLORS.muted} />
    <div
      style={{
        marginTop: 10,
        fontSize: moneySize(value, 36),
        fontWeight: 700,
        color: accent,
        fontVariantNumeric: "tabular-nums",
        whiteSpace: "nowrap",
      }}
    >
      {value}
    </div>
    <div style={{ marginTop: 12, width: "100%", height: 6, borderRadius: 3, background: COLORS.divider, position: "relative" }}>
      <div
        style={{
          position: "absolute",
          left: 0,
          top: 0,
          bottom: 0,
          width: `${Math.min(100, Math.max(0, pct))}%`,
          background: accent,
          borderRadius: 3,
        }}
      />
    </div>
  </div>
);