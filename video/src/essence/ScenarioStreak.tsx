import React from "react";
import { AbsoluteFill, interpolate, useCurrentFrame } from "remotion";
import { COLORS, EASE_EDIT, FONT_STACK, SERIF_STACK } from "../theme";
import { PaperBackground } from "../fx/PaperBackground";
import { GoldDust } from "../fx/GoldDust";
import { LedgerRow, LedgerRowData } from "../ui/ledger";
import { STREAK } from "./timing";
import { CanvasPhone, CloseLockup, FadeUp, Kicker, StampWord, prog } from "./blocks";
import { InsightCard, MomentHeader, ScenarioScene, ScenarioSummaryDemo } from "./scenario-blocks";
import { BurstDust, Odometer, PulseRing, TiltIn } from "./flash";
import { ScenarioStreakAudio } from "./audio2";

const W = 1080;
const H = 1920;
const PHONE = { x: W / 2, y: 1176, height: 1180 };

const MONTAGE_ROWS: Array<{ row: LedgerRowData; at: number }> = [
  { row: { id: "m1", time: "7:12", item: "Rice (5kg)", cost: "₱280.00", type: "NEED" }, at: 34 },
  { row: { id: "m2", time: "8:05", item: "Jeep fare", cost: "₱26.00", type: "NEED" }, at: 66 },
  { row: { id: "m3", time: "12:38", item: "Lunch", cost: "₱95.00", type: "NEED" }, at: 98 },
  { row: { id: "m4", time: "3:20", item: "Milk tea", cost: "₱120.00", type: "WANT" }, at: 130 },
  { row: { id: "m5", time: "6:47", item: "Groceries", cost: "₱485.00", type: "NEED" }, at: 162 },
  { row: { id: "m6", time: "8:15", item: "Movie night", cost: "₱380.00", type: "WANT" }, at: 194 },
];

/** Milestone frames for day 7 / 14 / 21 / 30 on the linear counter. */
const DAY_AT = (d: number) => 10 + ((d - 1) / 29) * 230;

/**
 * "30 Honest Days" — the habit story: a rolling day counter, a rain of
 * sealed rows in rhythm, milestone rings, and the month resolved on one
 * donut. Flashy in tempo, quiet in palette.
 */
export const ScenarioStreak: React.FC = () => {
  const frame = useCurrentFrame();
  const boot = interpolate(frame, [0, 8], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });

  return (
    <AbsoluteFill style={{ width: W, height: H, background: COLORS.background }}>
      <PaperBackground />
      <GoldDust frame={frame} count={12} area={{ x: 0, y: 160, w: W, h: 1500 }} opacity={0.4} />

      <AbsoluteFill style={{ opacity: boot }}>
        <ScenarioScene from={STREAK.hook} duration={STREAK.montage - STREAK.hook} fadeIn={4}>
          <StreakHook />
        </ScenarioScene>

        <ScenarioScene from={STREAK.montage} duration={STREAK.app - STREAK.montage}>
          <Montage />
        </ScenarioScene>

        <ScenarioScene from={STREAK.app} duration={STREAK.insight - STREAK.app}>
          <MomentHeader chip="Day 30" line="The whole month, on one page." accent={COLORS.goldDeep} />
          <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
            <ScenarioSummaryDemo
              needPctTo={63}
              totalCents={1432000}
              needsMoney="₱9,020.00"
              wantsMoney="₱5,300.00"
              period="All (30d)"
            />
          </CanvasPhone>
        </ScenarioScene>

        <ScenarioScene from={STREAK.insight} duration={STREAK.close - STREAK.insight}>
          <div style={{ position: "absolute", top: 620, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
            <InsightCard
              eyebrow="The habit, proven"
              headline="The first month you can explain."
              sub="Every line has a name, a cost, and an honest label."
              stats={[
                { label: "Days", value: "30", accent: COLORS.goldDeep },
                { label: "Entries", value: "87", accent: COLORS.ink },
                { label: "Need %", value: "63%", accent: COLORS.green },
              ]}
            />
          </div>
        </ScenarioScene>

        <ScenarioScene from={STREAK.close} duration={STREAK.total - STREAK.close} fadeOut={10}>
          <GoldDust frame={frame} count={20} area={{ x: 60, y: 300, w: 960, h: 1300 }} opacity={0.8} scale={1.1} />
          <div style={{ position: "absolute", top: 636, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
            <CloseLockup sealAt={8} scale={0.9} tagline="Thirty days. Zero mysteries." />
          </div>
        </ScenarioScene>
      </AbsoluteFill>

      <ScenarioStreakAudio />
    </AbsoluteFill>
  );
};

/* ────────────────────────────────────────────────────────────────────────── */

const StreakHook: React.FC = () => {
  return (
    <AbsoluteFill>
      <FadeUp at={4} style={{ position: "absolute", top: 330, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
        <Kicker text="The habit" align="center" size={26} />
      </FadeUp>
      <FadeUp at={14} style={{ position: "absolute", top: 440, left: 0, right: 0, textAlign: "center", padding: "0 90px" }}>
        <div style={{ fontFamily: SERIF_STACK, fontSize: 68, fontWeight: 700, lineHeight: 1.15, color: COLORS.ink }}>
          Can you witness
          <br />a whole month?
        </div>
      </FadeUp>
      <div style={{ position: "absolute", top: 830, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
        <StampWord word="DAY 1." color={COLORS.green} at={44} size={120} tilt={-2.4} />
      </div>
      <FadeUp at={70} style={{ position: "absolute", top: 1120, left: 0, right: 0, textAlign: "center" }}>
        <div
          style={{
            fontFamily: FONT_STACK,
            fontSize: 30,
            fontWeight: 600,
            letterSpacing: 4,
            color: COLORS.muted,
            textTransform: "uppercase",
          }}
        >
          One honest label at a time
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};

const Montage: React.FC = () => {
  const f = useCurrentFrame();
  const day = interpolate(f, [10, 240], [1, 30], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  return (
    <AbsoluteFill>
      {/* rolling day counter with milestone rings */}
      <div style={{ position: "absolute", top: 210, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
        <div style={{ position: "relative", display: "flex", alignItems: "center", gap: 26 }}>
          <span
            style={{
              fontFamily: SERIF_STACK,
              fontSize: 62,
              fontWeight: 700,
              color: COLORS.muted,
              letterSpacing: 4,
              paddingTop: 14,
            }}
          >
            DAY
          </span>
          <Odometer value={day} maxDigits={2} fontSize={150} color={COLORS.ink} />
          {[7, 14, 21, 30].map((d) => (
            <React.Fragment key={d}>
              <PulseRing p={prog(f, DAY_AT(d), DAY_AT(d) + 24)} size={330} />
              <BurstDust p={prog(f, DAY_AT(d), DAY_AT(d) + 26)} radius={220} count={12} />
            </React.Fragment>
          ))}
        </div>
      </div>

      {/* 30 calendar dots filling as the month passes */}
      <div style={{ position: "absolute", top: 500, left: 0, right: 0, display: "flex", flexDirection: "column", alignItems: "center", gap: 16 }}>
        {[0, 1].map((rowI) => (
          <div key={rowI} style={{ display: "flex", gap: 16 }}>
            {Array.from({ length: 15 }, (_, i) => {
              const dayIndex = rowI * 15 + i + 1;
              const filled = day >= dayIndex;
              const justFilled = day >= dayIndex && day < dayIndex + 1.6;
              return (
                <div
                  key={i}
                  style={{
                    width: 26,
                    height: 26,
                    borderRadius: 14,
                    background: filled ? COLORS.gold : "transparent",
                    border: `2.5px solid ${filled ? COLORS.goldDeep : COLORS.dividerStrong}`,
                    transform: justFilled ? "scale(1.28)" : "scale(1)",
                    boxShadow: justFilled ? "0 0 14px rgba(232,169,42,0.6)" : undefined,
                  }}
                />
              );
            })}
          </div>
        ))}
      </div>

      {/* rows raining into the sheet, in rhythm */}
      <div style={{ position: "absolute", top: 660, left: 0, right: 0, display: "flex", flexDirection: "column", alignItems: "center", gap: 20 }}>
        {MONTAGE_ROWS.map(({ row, at }) => {
          const p = prog(f, at, at + 13);
          return (
            <div key={row.id} style={{ width: 720 }}>
              <TiltIn p={p} fromY={44} fromRotX={16}>
                <LedgerRow
                  card
                  row={row}
                  timeW={92}
                  style={{
                    boxShadow:
                      p < 1
                        ? `0 ${18 * (1 - p)}px ${36 * (1 - p)}px -14px rgba(26,26,26,0.4)`
                        : "0 12px 26px -18px rgba(26,26,26,0.28)",
                  }}
                />
              </TiltIn>
            </div>
          );
        })}
      </div>

      <FadeUp at={206} style={{ position: "absolute", top: 1560, left: 0, right: 0, textAlign: "center", padding: "0 100px" }}>
        <div style={{ fontFamily: SERIF_STACK, fontStyle: "italic", fontSize: 42, color: COLORS.goldDeep, lineHeight: 1.4 }}>
          Some days green. Some days red.
          <br />
          All of them honest.
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};
