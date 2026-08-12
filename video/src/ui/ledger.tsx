import React from "react";
import { COLORS, FONT_STACK } from "../theme";

const TIME_W = 74;
const TYPE_W = 72;
const DEL_W = 40;
const GAP = 10;

export const LedgerHeader: React.FC<{ pad?: number; timeW?: number }> = ({
  pad = 0,
  timeW = TIME_W,
}) => {
  const c = (label: string, textAlign: React.CSSProperties["textAlign"] = "left") => (
    <span
      style={{
        fontFamily: FONT_STACK,
        fontSize: 22,
        fontWeight: 600,
        letterSpacing: 2.4,
        color: COLORS.muted,
        textAlign,
      }}
    >
      {label}
    </span>
  );
  return (
    <div style={{ display: "flex", alignItems: "center", padding: `0 ${pad}px`, width: "100%" }}>
      <span style={{ width: timeW, display: "inline-block" }}>{c("TIME")}</span>
      <span style={{ flex: 1 }}>{c("ITEM")}</span>
      <span style={{ width: 150, display: "inline-block" }}>{c("COST", "right")}</span>
      <span style={{ width: TYPE_W + GAP, display: "inline-block", textAlign: "center" }}>{c("TYPE")}</span>
      <span style={{ width: DEL_W }} />
    </div>
  );
};

export type LedgerRowData = {
  id: string;
  time: string;
  item: string;
  cost: string;
  type: "NEED" | "WANT";
};

export const LedgerRow: React.FC<{
  row: LedgerRowData;
  showDelete?: boolean;
  card?: boolean;
  timeW?: number;
  style?: React.CSSProperties;
}> = ({ row, showDelete = false, card = false, timeW = TIME_W, style }) => {
  const typeColor = row.type === "NEED" ? COLORS.green : COLORS.crimson;
  const inner = (
    <div
      style={{
        display: "flex",
        alignItems: "center",
        width: "100%",
        padding: card ? "12px 14px" : "10px 0",
        fontFamily: FONT_STACK,
      }}
    >
      <span
        style={{
          width: timeW,
          fontSize: 26,
          color: COLORS.muted,
          fontFeatureSettings: '"tnum"',
          fontVariantNumeric: "tabular-nums",
        }}
      >
        {row.time}
      </span>
      <span style={{ flex: 1, fontSize: 32, fontWeight: 500, color: COLORS.ink }}>{row.item}</span>
      <span
        style={{
          width: 150,
          textAlign: "right",
          fontSize: 32,
          fontWeight: 600,
          color: COLORS.ink,
          fontFeatureSettings: '"tnum"',
          fontVariantNumeric: "tabular-nums",
        }}
      >
        {row.cost}
      </span>
      <span style={{ width: GAP }} />
      <span style={{ width: TYPE_W, display: "flex", justifyContent: "center" }}>
        <span
          style={{
            width: 52,
            height: 52,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            borderRadius: 10,
            border: `2.5px solid ${typeColor}`,
            color: typeColor,
            fontSize: 30,
            fontWeight: 600,
          }}
        >
          {row.type === "NEED" ? "N" : "W"}
        </span>
      </span>
      <span style={{ width: DEL_W, display: "flex", justifyContent: "center" }}>
        {showDelete ? (
          <svg width="26" height="26" viewBox="0 0 24 24" fill="none" stroke="rgba(200,16,46,0.55)" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
            <path d="M4.5 6.5h15M9.5 6V4.5h5V6M7 6.5l.8 13h8.4l.8-13" />
          </svg>
        ) : null}
      </span>
    </div>
  );

  if (card) {
    return (
      <div
        style={{
          width: "100%",
          background: COLORS.card,
          border: `1px solid ${COLORS.divider}`,
          borderRadius: 16,
          ...style,
        }}
      >
        {inner}
      </div>
    );
  }
  return <div style={{ ...style }}>{inner}</div>;
};