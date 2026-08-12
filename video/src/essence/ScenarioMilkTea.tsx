import React from "react";
import { AbsoluteFill, interpolate, useCurrentFrame } from "remotion";
import { COLORS, EASE_EDIT, FONT_STACK, SERIF_STACK } from "../theme";
import { PaperBackground } from "../fx/PaperBackground";
import { GoldDust } from "../fx/GoldDust";
import { MILKTEA } from "./timing";
import { CanvasPhone, CloseLockup, FadeUp, Kicker, prog } from "./blocks";
import { InsightCard, MomentHeader, MomentLog, ScenarioScene, ScenarioSummaryDemo } from "./scenario-blocks";
import { CupIcon, DrawEllipse, Odometer, StaggerGrid, SwingTag } from "./flash";
import { ScenarioMilkTeaAudio } from "./audio2";

const W = 1080;
const H = 1920;
const PHONE = { x: W / 2, y: 1176, height: 1180 };

/**
 * "The Milk Tea Math" — one small habit, compounded in front of you.
 * A cup becomes thirty, an odometer rolls the price of a year, and the app
 * moment stays kind: not banned, just seen.
 */
export const ScenarioMilkTea: React.FC = () => {
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
        <ScenarioScene from={MILKTEA.hook} duration={MILKTEA.multiply - MILKTEA.hook} fadeIn={4}>
          <TeaHook />
        </ScenarioScene>

        <ScenarioScene from={MILKTEA.multiply} duration={MILKTEA.log - MILKTEA.multiply}>
          <Multiplier />
        </ScenarioScene>

        <ScenarioScene from={MILKTEA.log} duration={MILKTEA.see - MILKTEA.log}>
          <MomentHeader chip="4:12 PM" line="Not banned. Just seen." accent={COLORS.crimson} />
          <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
            <MomentLog
              spec={{
                item: "Milk tea",
                costDigits: "120",
                costCents: 12000,
                type: "WANT",
                time: "4:12",
                dateLabel: "TODAY · AUG 13",
                priorRows: [
                  { id: "lunch", time: "12:31", item: "Lunch", cost: "₱95.00", type: "NEED" },
                  { id: "jeep", time: "7:58", item: "Jeep fare", cost: "₱26.00", type: "NEED" },
                ],
                sheetBefore: 2,
              }}
            />
          </CanvasPhone>
        </ScenarioScene>

        <ScenarioScene from={MILKTEA.see} duration={MILKTEA.insight - MILKTEA.see}>
          <MomentHeader chip="Sunday" line="The week, honestly." accent={COLORS.goldDeep} />
          <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
            <ScenarioSummaryDemo
              needPctTo={71}
              totalCents={289000}
              needsMoney="₱2,050.00"
              wantsMoney="₱840.00"
              period="Week"
            />
          </CanvasPhone>
          <WantsCircle />
        </ScenarioScene>

        <ScenarioScene from={MILKTEA.insight} duration={MILKTEA.close - MILKTEA.insight}>
          <div style={{ position: "absolute", top: 620, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
            <InsightCard
              eyebrow="The compounding cup"
              headline={
                <>
                  <span
                    style={{
                      fontFamily: FONT_STACK,
                      fontWeight: 700,
                      color: COLORS.crimson,
                      fontVariantNumeric: "tabular-nums",
                    }}
                  >
                    ₱43,200
                  </span>{" "}
                  a year.
                </>
              }
              sub="Keep the habit if you love it. Just stop calling it small."
              stats={[
                { label: "A day", value: "₱120", accent: COLORS.ink },
                { label: "A month", value: "₱3,600", accent: COLORS.crimson },
                { label: "A year", value: "₱43,200", accent: COLORS.goldDeep },
              ]}
            />
          </div>
        </ScenarioScene>

        <ScenarioScene from={MILKTEA.close} duration={MILKTEA.total - MILKTEA.close} fadeOut={10}>
          <GoldDust frame={frame} count={20} area={{ x: 60, y: 300, w: 960, h: 1300 }} opacity={0.8} scale={1.1} />
          <div style={{ position: "absolute", top: 636, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
            <CloseLockup sealAt={8} scale={0.9} tagline="Small is a story. Check the math." />
          </div>
        </ScenarioScene>
      </AbsoluteFill>

      <ScenarioMilkTeaAudio />
    </AbsoluteFill>
  );
};

/* ────────────────────────────────────────────────────────────────────────── */

const TeaHook: React.FC = () => {
  const f = useCurrentFrame();
  const cupIn = prog(f, 10, 26);
  const bob = Math.sin(f * 0.05) * 6;
  return (
    <AbsoluteFill>
      <FadeUp at={4} style={{ position: "absolute", top: 320, left: 0, right: 0, textAlign: "center" }}>
        <div style={{ fontFamily: SERIF_STACK, fontSize: 78, fontWeight: 700, color: COLORS.ink }}>
          "It's just ₱120."
        </div>
      </FadeUp>
      <div
        style={{
          position: "absolute",
          top: 620 + bob,
          left: 0,
          right: 0,
          display: "flex",
          justifyContent: "center",
          opacity: cupIn,
          transform: `scale(${0.8 + cupIn * 0.2})`,
        }}
      >
        <CupIcon size={210} />
      </div>
      <div style={{ position: "absolute", top: 560, left: 640 }}>
        <SwingTag label="₱120" at={26} color={COLORS.crimson} stringLen={96} phase={1.2} />
      </div>
      <FadeUp at={58} style={{ position: "absolute", top: 1080, left: 0, right: 0, textAlign: "center" }}>
        <div
          style={{
            fontFamily: FONT_STACK,
            fontSize: 34,
            fontWeight: 500,
            color: COLORS.sub,
          }}
        >
          Sure. Let's do the just-math.
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};

const Multiplier: React.FC = () => {
  const f = useCurrentFrame();

  // ₱120 -> ₱3,600 (a month) -> ₱43,200 (a year)
  const value =
    f < 100
      ? interpolate(f, [22, 84], [120, 3600], {
          extrapolateLeft: "clamp",
          extrapolateRight: "clamp",
          easing: EASE_EDIT,
        })
      : interpolate(f, [112, 164], [3600, 43200], {
          extrapolateLeft: "clamp",
          extrapolateRight: "clamp",
          easing: EASE_EDIT,
        });

  const monthChip = prog(f, 20, 34) * (1 - prog(f, 106, 118));
  const yearChip = prog(f, 112, 126);
  const yearTint = prog(f, 112, 150);

  return (
    <AbsoluteFill>
      <FadeUp at={4} style={{ position: "absolute", top: 190, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
        <Kicker text="The just-math" align="center" size={26} />
      </FadeUp>

      {/* 30 cups, wave-staggered */}
      <div style={{ position: "absolute", top: 320, left: 0, right: 0 }}>
        <StaggerGrid
          rows={5}
          cols={6}
          at={12}
          cellDelay={2.6}
          gap={26}
          render={(i, p) => (
            <div style={{ position: "relative" }}>
              <CupIcon size={94} tone={0.5 + (i % 3) * 0.25} />
              {/* the year beat washes the wall crimson-ward */}
              <div
                style={{
                  position: "absolute",
                  inset: 0,
                  background: COLORS.crimson,
                  opacity: yearTint * 0.1 * p,
                  borderRadius: 12,
                }}
              />
            </div>
          )}
        />
      </div>

      {/* rolling money */}
      <div style={{ position: "absolute", top: 1090, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
        <Odometer value={value} maxDigits={5} fontSize={128} prefix="₱" color={COLORS.ink} />
      </div>

      {/* period chips */}
      <div style={{ position: "absolute", top: 1290, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
        <div style={{ position: "relative", height: 70, width: 420 }}>
          <div
            style={{
              position: "absolute",
              inset: 0,
              display: "flex",
              justifyContent: "center",
              opacity: monthChip,
              transform: `translateY(${(1 - monthChip) * 14}px)`,
            }}
          >
            <PeriodChip label="× 30 days" color={COLORS.goldDeep} />
          </div>
          <div
            style={{
              position: "absolute",
              inset: 0,
              display: "flex",
              justifyContent: "center",
              opacity: yearChip,
              transform: `translateY(${(1 - yearChip) * 14}px)`,
            }}
          >
            <PeriodChip label="× 12 months" color={COLORS.crimson} />
          </div>
        </div>
      </div>

      <FadeUp at={150} style={{ position: "absolute", top: 1420, left: 0, right: 0, textAlign: "center" }}>
        <div
          style={{
            fontFamily: SERIF_STACK,
            fontStyle: "italic",
            fontSize: 44,
            color: COLORS.goldDeep,
          }}
        >
          A year of "just ₱120."
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};

const PeriodChip: React.FC<{ label: string; color: string }> = ({ label, color }) => (
  <div
    style={{
      padding: "14px 34px",
      borderRadius: 999,
      background: COLORS.card,
      border: `2px solid ${color}`,
      fontFamily: FONT_STACK,
      fontSize: 32,
      fontWeight: 700,
      color,
      letterSpacing: 1.6,
      boxShadow: "0 14px 30px -18px rgba(26,26,26,0.35)",
      fontVariantNumeric: "tabular-nums",
    }}
  >
    {label}
  </div>
);

/** Gold hand-drawn circle around the WANTS stat card on the summary. */
const WantsCircle: React.FC = () => {
  const f = useCurrentFrame();
  const p = prog(f, 64, 92);
  return (
    <div style={{ position: "absolute", left: 419, top: 1512, pointerEvents: "none" }}>
      <DrawEllipse w={250} h={170} p={p} />
    </div>
  );
};
