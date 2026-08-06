import React from "react";
import { COLORS, FONT_STACK } from "../theme";
import { Eyebrow, GiltRule, PremiumSurface, LedgerField, TypeChip } from "./primitives";
import { LedgerHeader, LedgerRow, LedgerRowData } from "./ledger";
import { BudgetMeter } from "./budget";

export type LogScreenData = {
  dateLabel?: string;
  filledLabel?: string;
  budget?: {
    spent: string;
    budget: string;
    fillPct: number;
    over?: boolean;
    remainLabel: string;
  } | null;
  itemValue?: string;
  costValue?: string;
  focusedField?: "item" | "cost" | null;
  needSelected?: boolean;
  wantSelected?: boolean;
  rows: Array<{ row: LedgerRowData; style?: React.CSSProperties }>;
  showSheetHeader?: boolean;
};

export const LogScreen: React.FC<{
  data: LogScreenData;
  width: number;
  height: number;
  overlay?: React.ReactNode;
}> = ({ data, width, height, overlay }) => {
  const pad = 20;
  const filled = data.filledLabel ?? "0 / 20";
  return (
    <div
      style={{
        position: "relative",
        width,
        height,
        background: "linear-gradient(180deg, #FAFAF7 0%, #FAFAF7 55%, #F7F4EC 100%)",
        padding: `28px ${pad}px 0`,
        display: "flex",
        flexDirection: "column",
        overflow: "hidden",
        fontFamily: FONT_STACK,
      }}
    >
      <Eyebrow text={data.dateLabel ?? "TODAY  ·  Aug 5, 2026"} size={24} />
      <div style={{ marginTop: 12, display: "flex", justifyContent: "space-between", alignItems: "flex-end" }}>
        <div style={{ fontSize: 88, fontWeight: 700, color: COLORS.ink, fontFamily: FONT_STACK, lineHeight: 0.95, letterSpacing: 2 }}>
          LOG
        </div>
        <div style={{ textAlign: "right" }}>
          <Eyebrow text="SHEET" size={20} color={COLORS.muted} />
          <div style={{ marginTop: 4, fontSize: 34, fontWeight: 600, color: COLORS.ink }}>{filled}</div>
        </div>
      </div>
      <div style={{ marginTop: 16 }}>
        <GiltRule width={40} />
      </div>

      {data.budget ? (
        <div style={{ marginTop: 18 }}>
          <BudgetMeter
            spent={data.budget.spent}
            budget={data.budget.budget}
            fillPct={data.budget.fillPct}
            over={data.budget.over}
            remainLabel={data.budget.remainLabel}
          />
        </div>
      ) : null}

      {/* Entry form card */}
      <div style={{ marginTop: 20 }}>
        <PremiumSurface raised={false} goldEdge={false} radius={20} style={{ padding: "20px 20px 22px" }}>
          <LedgerField label="Item" value={data.itemValue ?? ""} focused={data.focusedField === "item"} />
          <div style={{ height: 16 }} />
          <div style={{ display: "flex", alignItems: "center", gap: 12 }}>
            <div style={{ flex: 1 }}>
              <LedgerField label="Cost" value={data.costValue ?? ""} focused={data.focusedField === "cost"} />
            </div>
            <TypeChip label="NEED" color={COLORS.green} selected={data.needSelected ?? false} />
            <TypeChip label="WANT" color={COLORS.crimson} selected={data.wantSelected ?? false} />
          </div>
        </PremiumSurface>
      </div>

      {/* Ledger of sealed entries */}
      {data.rows.length > 0 ? (
        <div style={{ marginTop: 22, flex: 1, overflow: "hidden" }}>
          <div style={{ padding: "0 14px" }}>
            <LedgerHeader pad={0} />
          </div>
          <div style={{ marginTop: 6, height: 2, background: COLORS.divider }} />
          <div style={{ marginTop: 10, display: "flex", flexDirection: "column", gap: 12 }}>
            {data.rows.map(({ row, style }) => (
              <div key={row.id} style={{ padding: "0 14px" }}>
                <LedgerRow row={row} card style={style} />
              </div>
            ))}
          </div>
        </div>
      ) : null}

      {overlay}
    </div>
  );
};