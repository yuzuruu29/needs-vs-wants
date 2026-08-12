import React from "react";
import { AbsoluteFill, interpolate, useCurrentFrame } from "remotion";
import { COLORS, EASE_EDIT, FONT_STACK, SERIF_STACK } from "../theme";
import { PaperBackground } from "../fx/PaperBackground";
import { GoldDust } from "../fx/GoldDust";
import { SUBS } from "./timing";
import { CanvasPhone, CloseLockup, FadeUp, Kicker, prog } from "./blocks";
import { InsightCard, MomentHeader, MomentLog, ScenarioScene } from "./scenario-blocks";
import { Odometer, TiltIn } from "./flash";
import { ScenarioSubsAudio } from "./audio2";

const W = 1080;
const H = 1920;
const PHONE = { x: W / 2, y: 1176, height: 1180 };

type Sub = {
  name: string;
  price: string;
  monthly: number;
  tag: "W" | "N";
  tint: string;
  letter: string;
};

const SUBS_LIST: Sub[] = [
  { name: "Stream+", price: "₱399 / mo", monthly: 399, tag: "W", tint: COLORS.crimson, letter: "S" },
  { name: "MusicOne", price: "₱149 / mo", monthly: 149, tag: "W", tint: COLORS.goldDeep, letter: "M" },
  { name: "CloudBox", price: "₱299 / mo", monthly: 299, tag: "W", tint: COLORS.sub, letter: "C" },
  { name: "Data plan", price: "₱299 / mo", monthly: 299, tag: "N", tint: COLORS.green, letter: "D" },
];

const SubCard: React.FC<{ sub: Sub; width?: number; dim?: number }> = ({ sub, width = 640, dim = 0 }) => (
  <div
    style={{
      width,
      display: "flex",
      alignItems: "center",
      gap: 22,
      padding: "22px 28px",
      borderRadius: 24,
      background: COLORS.card,
      border: "1px solid rgba(232,169,42,0.3)",
      boxShadow: "0 20px 42px -22px rgba(26,26,26,0.38)",
      opacity: 1 - dim * 0.45,
    }}
  >
    <div
      style={{
        width: 72,
        height: 72,
        borderRadius: 18,
        background: `${sub.tint}14`,
        border: `2px solid ${sub.tint}66`,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        fontFamily: FONT_STACK,
        fontSize: 34,
        fontWeight: 700,
        color: sub.tint,
      }}
    >
      {sub.letter}
    </div>
    <div style={{ flex: 1 }}>
      <div style={{ fontFamily: FONT_STACK, fontSize: 33, fontWeight: 700, color: COLORS.ink }}>{sub.name}</div>
      <div style={{ marginTop: 4, fontFamily: FONT_STACK, fontSize: 25, color: COLORS.muted }}>
        renews on the 1st
      </div>
    </div>
    <div style={{ textAlign: "right", display: "flex", alignItems: "center", gap: 16 }}>
      <span
        style={{
          fontFamily: FONT_STACK,
          fontSize: 30,
          fontWeight: 700,
          color: COLORS.ink,
          fontVariantNumeric: "tabular-nums",
        }}
      >
        {sub.price}
      </span>
      <span
        style={{
          width: 46,
          height: 46,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          borderRadius: 10,
          border: `2.5px solid ${sub.tag === "N" ? COLORS.green : COLORS.crimson}`,
          color: sub.tag === "N" ? COLORS.green : COLORS.crimson,
          fontFamily: FONT_STACK,
          fontSize: 26,
          fontWeight: 700,
        }}
      >
        {sub.tag}
      </span>
    </div>
  </div>
);

/**
 * "The Quiet Leak" — subscriptions cascade in, sum on an odometer, get
 * logged honestly, and the forgotten one flips over and falls away.
 */
export const ScenarioSubs: React.FC = () => {
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
        <ScenarioScene from={SUBS.hook} duration={SUBS.stack - SUBS.hook} fadeIn={4}>
          <SubsHook />
        </ScenarioScene>

        <ScenarioScene from={SUBS.stack} duration={SUBS.log - SUBS.stack}>
          <StackSum />
        </ScenarioScene>

        <ScenarioScene from={SUBS.log} duration={SUBS.audit - SUBS.log}>
          <MomentHeader chip="The 1st · 9:05 AM" line="Once a month, tell the truth." accent={COLORS.goldDeep} />
          <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
            <MomentLog
              spec={{
                item: "Stream+ sub",
                costDigits: "399",
                costCents: 39900,
                type: "WANT",
                time: "9:05",
                dateLabel: "MONDAY · SEP 1",
                priorRows: [
                  { id: "music", time: "9:04", item: "MusicOne sub", cost: "₱149.00", type: "WANT" },
                  { id: "data", time: "9:02", item: "Data plan", cost: "₱299.00", type: "NEED" },
                ],
                sheetBefore: 2,
              }}
            />
          </CanvasPhone>
        </ScenarioScene>

        <ScenarioScene from={SUBS.audit} duration={SUBS.insight - SUBS.audit}>
          <AuditFall />
        </ScenarioScene>

        <ScenarioScene from={SUBS.insight} duration={SUBS.close - SUBS.insight}>
          <div style={{ position: "absolute", top: 620, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
            <InsightCard
              eyebrow="The quiet leak"
              headline={
                <>
                  One forgotten sub:{" "}
                  <span
                    style={{
                      fontFamily: FONT_STACK,
                      fontWeight: 700,
                      color: COLORS.crimson,
                      fontVariantNumeric: "tabular-nums",
                    }}
                  >
                    ₱3,588
                  </span>{" "}
                  a year.
                </>
              }
              sub="List them once. Label them honestly. Cut what you forgot."
              stats={[
                { label: "Kept", value: "₱847 / mo", accent: COLORS.ink },
                { label: "Cut", value: "₱299 / mo", accent: COLORS.crimson },
                { label: "A year", value: "₱3,588", accent: COLORS.goldDeep },
              ]}
            />
          </div>
        </ScenarioScene>

        <ScenarioScene from={SUBS.close} duration={SUBS.total - SUBS.close} fadeOut={10}>
          <GoldDust frame={frame} count={20} area={{ x: 60, y: 300, w: 960, h: 1300 }} opacity={0.8} scale={1.1} />
          <div style={{ position: "absolute", top: 636, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
            <CloseLockup sealAt={8} scale={0.9} tagline="Know what renews." />
          </div>
        </ScenarioScene>
      </AbsoluteFill>

      <ScenarioSubsAudio />
    </AbsoluteFill>
  );
};

/* ────────────────────────────────────────────────────────────────────────── */

const SubsHook: React.FC = () => {
  const f = useCurrentFrame();
  return (
    <AbsoluteFill>
      <FadeUp at={2} style={{ position: "absolute", top: 200, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
        <Kicker text="The 1st of the month" align="center" size={26} />
      </FadeUp>
      <FadeUp at={12} style={{ position: "absolute", top: 306, left: 0, right: 0, textAlign: "center" }}>
        <div style={{ fontFamily: SERIF_STACK, fontSize: 66, fontWeight: 700, color: COLORS.ink }}>
          They bill while you sleep.
        </div>
      </FadeUp>
      <div
        style={{
          position: "absolute",
          top: 560,
          left: 0,
          right: 0,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          gap: 26,
        }}
      >
        {SUBS_LIST.map((s, i) => {
          const p = prog(f, 26 + i * 12, 44 + i * 12);
          const lean = [-3.5, 2.5, -1.8, 3][i];
          return (
            <div key={s.name} style={{ transform: `rotate(${lean * (1 - p * 0.4)}deg)` }}>
              <TiltIn p={p}>
                <SubCard sub={s} />
              </TiltIn>
            </div>
          );
        })}
      </div>
    </AbsoluteFill>
  );
};

const StackSum: React.FC = () => {
  const f = useCurrentFrame();
  const total = interpolate(
    f,
    [14, 22, 34, 42, 54, 62, 74, 82],
    [0, 399, 399, 548, 548, 847, 847, 1146],
    { extrapolateLeft: "clamp", extrapolateRight: "clamp", easing: EASE_EDIT },
  );
  const yearRoll = interpolate(f, [118, 158], [1146, 13752], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const isYear = f >= 118;
  const moOp = 1 - prog(f, 114, 126);
  const yrOp = prog(f, 118, 130);

  return (
    <AbsoluteFill>
      <div
        style={{
          position: "absolute",
          top: 200,
          left: 0,
          right: 0,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          gap: 18,
        }}
      >
        {SUBS_LIST.map((s, i) => {
          const p = prog(f, 8 + i * 12, 22 + i * 12);
          return (
            <div key={s.name} style={{ opacity: Math.min(1, p * 1.5), transform: `translateY(${(1 - p) * 26}px)` }}>
              <SubCard sub={s} width={600} />
            </div>
          );
        })}
      </div>

      {/* running total */}
      <div style={{ position: "absolute", top: 1120, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
        <Odometer value={isYear ? yearRoll : total} maxDigits={5} fontSize={126} prefix="₱" color={COLORS.ink} />
      </div>
      <div style={{ position: "absolute", top: 1320, left: 0, right: 0, height: 56, textAlign: "center" }}>
        <div style={{ position: "absolute", inset: 0, opacity: moOp }}>
          <span style={{ fontFamily: FONT_STACK, fontSize: 34, fontWeight: 600, color: COLORS.muted, letterSpacing: 3 }}>
            EVERY MONTH
          </span>
        </div>
        <div style={{ position: "absolute", inset: 0, opacity: yrOp }}>
          <span style={{ fontFamily: FONT_STACK, fontSize: 34, fontWeight: 600, color: COLORS.crimson, letterSpacing: 3 }}>
            EVERY YEAR
          </span>
        </div>
      </div>

      <FadeUp at={146} style={{ position: "absolute", top: 1440, left: 0, right: 0, textAlign: "center" }}>
        <div style={{ fontFamily: SERIF_STACK, fontStyle: "italic", fontSize: 44, color: COLORS.goldDeep }}>
          On autopilot.
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};

/** The forgotten card flips edge-on and drops out of the stack. */
const AuditFall: React.FC = () => {
  const f = useCurrentFrame();
  const flip = prog(f, 36, 56);
  const fall = prog(f, 56, 92);
  const closeGap = prog(f, 74, 96);
  const monthly = interpolate(f, [92, 118], [1146, 847], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const savedIn = prog(f, 108, 124);

  return (
    <AbsoluteFill>
      <FadeUp at={4} style={{ position: "absolute", top: 200, left: 0, right: 0, textAlign: "center", padding: "0 90px" }}>
        <div style={{ fontFamily: SERIF_STACK, fontSize: 58, fontWeight: 700, lineHeight: 1.16, color: COLORS.ink }}>
          Forgot you had it?
          <br />
          It didn't forget you.
        </div>
      </FadeUp>

      <div
        style={{
          position: "absolute",
          top: 520,
          left: 0,
          right: 0,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          gap: 18,
        }}
      >
        {SUBS_LIST.map((s) => {
          const isCut = s.name === "CloudBox";
          if (isCut) {
            return (
              <div
                key={s.name}
                style={{
                  transform: `perspective(1100px) rotateX(${flip * -84}deg) translateY(${fall * 460}px) rotate(${fall * 16}deg)`,
                  transformOrigin: "center top",
                  opacity: 1 - fall,
                  height: 118 * (1 - closeGap),
                  marginBottom: closeGap > 0 ? -18 * closeGap : 0,
                }}
              >
                <SubCard sub={s} width={600} dim={flip} />
              </div>
            );
          }
          return (
            <div key={s.name}>
              <SubCard sub={s} width={600} />
            </div>
          );
        })}
      </div>

      {/* the new monthly line */}
      <div style={{ position: "absolute", top: 1180, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
        <Odometer value={monthly} maxDigits={4} fontSize={116} prefix="₱" color={COLORS.ink} />
      </div>
      <div style={{ position: "absolute", top: 1360, left: 0, right: 0, display: "flex", justifyContent: "center", opacity: savedIn, transform: `translateY(${(1 - savedIn) * 14}px)` }}>
        <div
          style={{
            padding: "13px 30px",
            borderRadius: 999,
            background: "rgba(11,107,58,0.1)",
            border: `2px solid ${COLORS.green}`,
            fontFamily: FONT_STACK,
            fontSize: 32,
            fontWeight: 700,
            color: COLORS.green,
            fontVariantNumeric: "tabular-nums",
          }}
        >
          -₱299 every month
        </div>
      </div>
    </AbsoluteFill>
  );
};
