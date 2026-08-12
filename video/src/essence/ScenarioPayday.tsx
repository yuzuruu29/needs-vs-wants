import React from "react";
import { AbsoluteFill, interpolate, useCurrentFrame } from "remotion";
import { COLORS, EASE_EDIT, FONT_STACK, SERIF_STACK } from "../theme";
import { PaperBackground } from "../fx/PaperBackground";
import { GoldDust } from "../fx/GoldDust";
import { PAYDAY } from "./timing";
import { CanvasPhone, CloseLockup, FadeUp, StampWord } from "./blocks";
import {
  InsightCard,
  MomentHeader,
  MomentLog,
  ScenarioScene,
  ScenarioSummaryDemo,
} from "./scenario-blocks";
import { ScenarioPaydayAudio } from "./audio";
import { LedgerRowData } from "../ui/ledger";

const W = 1080;
const H = 1920;
const PHONE = { x: W / 2, y: 1176, height: 1180 };
const DATE = "SWELDO DAY · AUG 15, 2026";

const ROW_LATTE: LedgerRowData = {
  id: "latte",
  time: "10:12",
  item: "Iced latte",
  cost: "₱180.00",
  type: "WANT",
};
const ROW_LUNCH: LedgerRowData = {
  id: "lunch",
  time: "12:40",
  item: "Rice meal",
  cost: "₱320.00",
  type: "NEED",
};

/**
 * "Sweldo Day" — a real payday, witnessed hour by hour. Three purchases get
 * their honest label the moment they happen; the evening donut tells the
 * truth; nothing judges, everything is seen.
 */
export const ScenarioPayday: React.FC = () => {
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
        <ScenarioScene from={PAYDAY.hook} duration={PAYDAY.m1 - PAYDAY.hook} fadeIn={4}>
          <PaydayHook />
        </ScenarioScene>

        <ScenarioScene from={PAYDAY.m1} duration={PAYDAY.m2 - PAYDAY.m1}>
          <MomentHeader
            chip="10:12 AM"
            line="The payday latte."
            accent={COLORS.crimson}
            dots={{ count: 3, active: 0 }}
          />
          <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
            <MomentLog
              spec={{
                item: "Iced latte",
                costDigits: "180",
                costCents: 18000,
                type: "WANT",
                time: "10:12",
                dateLabel: DATE,
                priorRows: [],
                sheetBefore: 0,
              }}
            />
          </CanvasPhone>
        </ScenarioScene>

        <ScenarioScene from={PAYDAY.m2} duration={PAYDAY.m3 - PAYDAY.m2}>
          <MomentHeader
            chip="12:40 PM"
            line="Lunch is fuel."
            accent={COLORS.green}
            dots={{ count: 3, active: 1 }}
          />
          <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
            <MomentLog
              spec={{
                item: "Rice meal",
                costDigits: "320",
                costCents: 32000,
                type: "NEED",
                time: "12:40",
                dateLabel: DATE,
                priorRows: [ROW_LATTE],
                sheetBefore: 1,
              }}
            />
          </CanvasPhone>
        </ScenarioScene>

        <ScenarioScene from={PAYDAY.m3} duration={PAYDAY.summary - PAYDAY.m3}>
          <MomentHeader
            chip="8:47 PM"
            line="The flash sale finds you."
            accent={COLORS.crimson}
            dots={{ count: 3, active: 2 }}
          />
          <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
            <MomentLog
              spec={{
                item: "Earbuds",
                costDigits: "1299",
                costCents: 129900,
                type: "WANT",
                time: "8:47",
                dateLabel: DATE,
                priorRows: [ROW_LUNCH, ROW_LATTE],
                sheetBefore: 2,
                chipAt: 84, // a beat of hesitation before the honest call
              }}
            />
          </CanvasPhone>
        </ScenarioScene>

        <ScenarioScene from={PAYDAY.summary} duration={PAYDAY.insight - PAYDAY.summary}>
          <MomentHeader chip="10:05 PM" line="Every peso, witnessed." accent={COLORS.goldDeep} />
          <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
            <ScenarioSummaryDemo
              needPctTo={18}
              totalCents={179900}
              needsMoney="₱320.00"
              wantsMoney="₱1,479.00"
              period="Day"
            />
          </CanvasPhone>
        </ScenarioScene>

        <ScenarioScene from={PAYDAY.insight} duration={PAYDAY.close - PAYDAY.insight}>
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
              eyebrow="Sweldo day, witnessed"
              headline={
                <>
                  Wants today:{" "}
                  <span
                    style={{
                      fontFamily: FONT_STACK,
                      fontWeight: 700,
                      color: COLORS.crimson,
                      fontVariantNumeric: "tabular-nums",
                    }}
                  >
                    ₱1,479
                  </span>
                  .
                </>
              }
              sub="Nothing judged you. You just finally saw it happen."
              stats={[
                { label: "Needs", value: "₱320.00", accent: COLORS.green },
                { label: "Wants", value: "₱1,479.00", accent: COLORS.crimson },
                { label: "Need %", value: "18%", accent: COLORS.goldDeep },
              ]}
            />
          </div>
        </ScenarioScene>

        <ScenarioScene from={PAYDAY.close} duration={PAYDAY.total - PAYDAY.close} fadeOut={10}>
          <GoldDust frame={frame} count={20} area={{ x: 60, y: 300, w: 960, h: 1300 }} opacity={0.8} scale={1.1} />
          <div style={{ position: "absolute", top: 636, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
            <CloseLockup sealAt={8} scale={0.9} tagline="Next payday, watch it live." />
          </div>
        </ScenarioScene>
      </AbsoluteFill>

      <ScenarioPaydayAudio />
    </AbsoluteFill>
  );
};

const PaydayHook: React.FC = () => {
  return (
    <AbsoluteFill>
      <div
        style={{
          position: "absolute",
          top: 610,
          left: 0,
          right: 0,
          display: "flex",
          justifyContent: "center",
        }}
      >
        <StampWord word="SWELDO." color={COLORS.goldDeep} at={8} size={148} tilt={-2.4} />
      </div>
      <FadeUp at={26} style={{ position: "absolute", top: 830, left: 0, right: 0, textAlign: "center", padding: "0 90px" }}>
        <div
          style={{
            fontFamily: SERIF_STACK,
            fontSize: 56,
            fontWeight: 700,
            lineHeight: 1.2,
            color: COLORS.ink,
          }}
        >
          The most expensive day
          <br />
          of the month.
        </div>
      </FadeUp>
      <FadeUp at={52} style={{ position: "absolute", top: 1130, left: 0, right: 0, textAlign: "center" }}>
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
          One real payday, logged live
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};
