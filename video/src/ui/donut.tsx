import React from "react";
import { COLORS, FONT_STACK } from "../theme";

export const Donut: React.FC<{
  needSweep: number; // degrees 0..360 the green arc spans
  size?: number;
  ringWidth?: number;
  centerEyebrow?: string;
  centerMoney?: string;
  centerMoneySize?: number;
  showEmpty?: boolean;
  style?: React.CSSProperties;
}> = ({
  needSweep,
  size = 400,
  ringWidth = 44,
  centerEyebrow = "TOTAL",
  centerMoney = "₱0.00",
  centerMoneySize = 56,
  showEmpty = false,
  style,
}) => {
  const r = (size - ringWidth) / 2;
  const c = 2 * Math.PI * r;
  const sweep = Math.max(0, Math.min(360, needSweep));
  const needLen = (sweep / 360) * c;
  const wantLen = c - needLen;
  const rot = -90; // start at 12 o'clock

  return (
    <div style={{ position: "relative", width: size, height: size, ...style }}>
      {/* gilt glow backdrop */}
      <div
        style={{
          position: "absolute",
          inset: -40,
          background: `radial-gradient(circle, rgba(232,169,42,0.16) 0%, rgba(232,169,42,0) 62%)`,
        }}
      />
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`} style={{ position: "relative" }}>
        {/* track */}
        <circle
          cx={size / 2}
          cy={size / 2}
          r={r}
          fill="none"
          stroke={COLORS.divider}
          strokeWidth={ringWidth}
        />
        {/* need arc */}
        <circle
          cx={size / 2}
          cy={size / 2}
          r={r}
          fill="none"
          stroke={COLORS.green}
          strokeWidth={ringWidth}
          strokeLinecap="round"
          strokeDasharray={`${needLen} ${c - needLen}`}
          strokeDashoffset={0}
          transform={`rotate(${rot} ${size / 2} ${size / 2})`}
        />
        {/* want arc */}
        <circle
          cx={size / 2}
          cy={size / 2}
          r={r}
          fill="none"
          stroke={COLORS.crimson}
          strokeWidth={ringWidth}
          strokeLinecap="round"
          strokeDasharray={`${wantLen} ${c - wantLen}`}
          strokeDashoffset={-needLen}
          transform={`rotate(${rot} ${size / 2} ${size / 2})`}
        />
      </svg>
      <div
        style={{
          position: "absolute",
          inset: 0,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          fontFamily: FONT_STACK,
        }}
      >
        <span style={{ fontSize: 24, fontWeight: 600, letterSpacing: 3, color: COLORS.muted }}>
          {centerEyebrow.toUpperCase()}
        </span>
        <span
          style={{
            marginTop: 6,
            fontSize: centerMoneySize,
            fontWeight: 700,
            color: COLORS.ink,
            fontVariantNumeric: "tabular-nums",
            whiteSpace: "nowrap",
          }}
        >
          {centerMoney}
        </span>
      </div>
    </div>
  );
};