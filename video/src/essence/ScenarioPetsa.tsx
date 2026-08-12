import React from "react";
import { AbsoluteFill, interpolate, useCurrentFrame } from "remotion";
import { COLORS, EASE_EDIT, FONT_STACK, SERIF_STACK } from "../theme";
import { PaperBackground } from "../fx/PaperBackground";
import { GoldDust } from "../fx/GoldDust";
import { CONTENT } from "../layout";
import { LogScreen } from "../ui/LogScreen";
import { LedgerRowData } from "../ui/ledger";
import { PETSA } from "./timing";
import { CanvasPhone, CloseLockup, FadeUp, StampWord, prog } from "./blocks";
import {
  CancelGuardDemo,
  InsightCard,
  MomentHeader,
  MomentLog,
  ScenarioScene,
  ScenarioSummaryDemo,
  TimeChip,
} from "./scenario-blocks";
import { ScenarioPetsaAudio } from "./audio";

const W = 1080;
const H = 1920;
const PHONE = { x: W / 2, y: 1176, height: 1180 };

const DAY3_ROWS: LedgerRowData[] = [
  { id: "pandesal", time: "12:05", item: "Pandesal", cost: "₱38.00", type: "NEED" },
  { id: "jeep", time: "7:58", item: "Jeep fare", cost: "₱26.00", type: "NEED" },
];

/**
 * "Petsa de Peligro" — the five tight days before payday, survived on a
 * P100 daily line. Day 1 stays under; day 3 refuses the usual milk tea at
 * the Log anyway? gate; day 5 arrives still standing.
 */
export const ScenarioPetsa: React.FC = () => {
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
        <ScenarioScene from={PETSA.hook} duration={PETSA.set - PETSA.hook} fadeIn={4}>
          <PetsaHook />
        </ScenarioScene>

        <ScenarioScene from={PETSA.set} duration={PETSA.day1 - PETSA.set}>
          <MomentHeader chip="Day 1 · Morning" line="Set the daily line." accent={COLORS.goldDeep} />
          <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
            <SetLineDemo />
          </CanvasPhone>
        </ScenarioScene>

        <ScenarioScene from={PETSA.day1} duration={PETSA.day3 - PETSA.day1}>
          <MomentHeader
            chip="Day 1 · 7:35 AM"
            line="Cheap breakfast, honest label."
            accent={COLORS.green}
            dots={{ count: 5, active: 0 }}
          />
          <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
            <MomentLog
              spec={{
                item: "Lugaw",
                costDigits: "45",
                costCents: 4500,
                type: "NEED",
                time: "7:35",
                dateLabel: "DAY 1 OF 5 · AUG 10",
                priorRows: [],
                sheetBefore: 0,
                budget: { budgetCents: 10000, spentBeforeCents: 0 },
              }}
            />
          </CanvasPhone>
        </ScenarioScene>

        <ScenarioScene from={PETSA.day3} duration={PETSA.day5 - PETSA.day3}>
          <Day3Header />
          <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
            <CancelGuardDemo priorRows={DAY3_ROWS} dateLabel="DAY 3 OF 5 · AUG 12" />
          </CanvasPhone>
        </ScenarioScene>

        <ScenarioScene from={PETSA.day5} duration={PETSA.insight - PETSA.day5}>
          <MomentHeader
            chip="Day 5 · Sweldo eve"
            line="Still standing."
            accent={COLORS.green}
            dots={{ count: 5, active: 4 }}
          />
          <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
            <ScenarioSummaryDemo
              needPctTo={88}
              totalCents={47300}
              needsMoney="₱416.00"
              wantsMoney="₱57.00"
              period="Week"
            />
          </CanvasPhone>
        </ScenarioScene>

        <ScenarioScene from={PETSA.insight} duration={PETSA.close - PETSA.insight}>
          <div
            style={{
              position: "absolute",
              top: 620,
              left: 0,
              right: 0,
              display: "flex",
              justifyContent: "center",
            }}
          >
            <InsightCard
              eyebrow="Petsa de peligro, survived"
              headline="Five days. Still standing."
              sub="You did not earn more. You leaked less."
              stats={[
                { label: "Spent", value: "₱473.00", accent: COLORS.green },
                { label: "Refused", value: "₱120.00", accent: COLORS.crimson },
                { label: "Left over", value: "₱27.00", accent: COLORS.goldDeep },
              ]}
            />
          </div>
        </ScenarioScene>

        <ScenarioScene from={PETSA.close} duration={PETSA.total - PETSA.close} fadeOut={10}>
          <GoldDust frame={frame} count={20} area={{ x: 60, y: 300, w: 960, h: 1300 }} opacity={0.8} scale={1.1} />
          <div style={{ position: "absolute", top: 636, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
            <CloseLockup sealAt={8} scale={0.9} tagline="Make the danger dates boring." />
          </div>
        </ScenarioScene>
      </AbsoluteFill>

      <ScenarioPetsaAudio />
    </AbsoluteFill>
  );
};

/* ────────────────────────────────────────────────────────────────────────── */

const PetsaHook: React.FC = () => {
  return (
    <AbsoluteFill>
      <div
        style={{
          position: "absolute",
          top: 590,
          left: 0,
          right: 0,
          display: "flex",
          justifyContent: "center",
        }}
      >
        <StampWord word="FIVE DAYS." color={COLORS.ink} at={8} size={112} tilt={-2} />
      </div>
      <FadeUp at={26} style={{ position: "absolute", top: 790, left: 0, right: 0, textAlign: "center", padding: "0 90px" }}>
        <div
          style={{
            fontFamily: FONT_STACK,
            fontSize: 48,
            fontWeight: 600,
            lineHeight: 1.35,
            color: COLORS.sub,
            fontVariantNumeric: "tabular-nums",
          }}
        >
          ₱500 between you
          <br />
          and the next payday.
        </div>
      </FadeUp>
      <FadeUp at={54} style={{ position: "absolute", top: 1080, left: 0, right: 0, textAlign: "center" }}>
        <div
          style={{
            fontFamily: SERIF_STACK,
            fontStyle: "italic",
            fontSize: 46,
            color: COLORS.goldDeep,
            letterSpacing: 1.4,
          }}
        >
          Petsa de peligro.
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};

/** Day 3 header whose line turns from temptation to refusal. */
const Day3Header: React.FC = () => {
  const f = useCurrentFrame();
  const swap = prog(f, 106, 118);
  return (
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
        gap: 26,
      }}
    >
      <FadeUp at={4}>
        <TimeChip label="Day 3 · 4:12 PM" accent={COLORS.crimson} />
      </FadeUp>
      <div style={{ position: "relative", height: 76, width: "100%" }}>
        <FadeUp
          at={14}
          style={{
            position: "absolute",
            inset: 0,
            opacity: 1 - swap,
            transform: `translateY(${swap * -12}px)`,
          }}
        >
          <div style={{ fontFamily: SERIF_STACK, fontSize: 58, fontWeight: 700, color: COLORS.ink }}>
            The usual temptation.
          </div>
        </FadeUp>
        <div
          style={{
            position: "absolute",
            inset: 0,
            opacity: swap,
            transform: `translateY(${(1 - swap) * 14}px)`,
          }}
        >
          <div
            style={{
              fontFamily: SERIF_STACK,
              fontSize: 58,
              fontWeight: 700,
              color: COLORS.green,
            }}
          >
            Not today.
          </div>
        </div>
      </div>
      <FadeUp at={24}>
        <div style={{ display: "flex", gap: 10, justifyContent: "center" }}>
          {Array.from({ length: 5 }, (_, i) => (
            <div
              key={i}
              style={{
                width: i === 2 ? 44 : 16,
                height: 5,
                borderRadius: 3,
                background: i === 2 ? COLORS.gold : COLORS.dividerStrong,
              }}
            />
          ))}
        </div>
      </FadeUp>
    </div>
  );
};

/** Day 1 morning: the P100 line appears on the Log screen. */
const SetLineDemo: React.FC = () => {
  const f = useCurrentFrame();
  const meterIn = prog(f, 16, 34);
  return (
    <div style={{ position: "relative", width: CONTENT.W, height: CONTENT.H }}>
      <LogScreen
        width={CONTENT.W}
        height={CONTENT.H}
        data={{
          dateLabel: "DAY 1 OF 5 · AUG 10",
          filledLabel: "0 / 20",
          budget: {
            spent: "₱0.00",
            budget: "₱100.00",
            fillPct: 0,
            over: false,
            remainLabel: "₱100.00 left today",
          },
          itemValue: "",
          costValue: "",
          focusedField: null,
          needSelected: false,
          wantSelected: false,
          rows: [],
        }}
      />
      {/* the budget card materializes: soft white sweep over its region */}
      <div
        style={{
          position: "absolute",
          left: 0,
          right: 0,
          top: 210,
          height: 210,
          background: COLORS.background,
          opacity: 1 - meterIn,
          pointerEvents: "none",
        }}
      />
    </div>
  );
};
