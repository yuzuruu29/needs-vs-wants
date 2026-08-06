import React from "react";
import { COLORS, FONT_STACK } from "../theme";
import { Eyebrow } from "./primitives";

export const BudgetMeter: React.FC<{
  spent: string;
  budget: string;
  fillPct: number; // 0..1
  over?: boolean;
  remainLabel: string;
  style?: React.CSSProperties;
}> = ({ spent, budget, fillPct, over = false, remainLabel, style }) => {
  const fillColor = over ? COLORS.crimson : fillPct > 0.78 ? COLORS.goldDeep : COLORS.green;
  const fillWidth = Math.min(1, Math.max(0, fillPct));
  return (
    <div
      style={{
        width: "100%",
        background: COLORS.card,
        border: `1px solid ${over ? "rgba(200,16,46,0.45)" : COLORS.divider}`,
        borderRadius: 24,
        padding: "20px 22px 22px",
        ...style,
      }}
    >
      <Eyebrow text="DAILY BUDGET" color={over ? COLORS.crimson : COLORS.goldDeep} size={24} />
      <div
        style={{
          marginTop: 10,
          display: "flex",
          justifyContent: "space-between",
          alignItems: "baseline",
          fontFamily: FONT_STACK,
        }}
      >
        <span style={{ fontSize: 40, fontWeight: 700, color: over ? COLORS.crimson : COLORS.ink, fontVariantNumeric: "tabular-nums" }}>
          {spent}
        </span>
        <span style={{ fontSize: 28, color: COLORS.muted, fontVariantNumeric: "tabular-nums" }}>of {budget}</span>
      </div>
      <div
        style={{
          marginTop: 14,
          width: "100%",
          height: 12,
          borderRadius: 6,
          background: COLORS.raised,
          overflow: "hidden",
        }}
      >
        <div
          style={{
            width: `${fillWidth * 100}%`,
            height: "100%",
            background: `linear-gradient(90deg, ${fillColor}, ${fillColor})`,
            borderRadius: 6,
          }}
        />
      </div>
      <div
        style={{
          marginTop: 12,
          fontSize: 28,
          color: over ? COLORS.crimson : COLORS.sub,
          fontWeight: over ? 600 : 400,
          fontFamily: FONT_STACK,
        }}
      >
        {remainLabel}
      </div>
    </div>
  );
};