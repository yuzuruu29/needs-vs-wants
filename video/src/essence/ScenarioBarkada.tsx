import React from "react";
import { AbsoluteFill, interpolate, useCurrentFrame } from "remotion";
import { COLORS, EASE_EDIT, FONT_STACK, SERIF_STACK } from "../theme";
import { PaperBackground } from "../fx/PaperBackground";
import { GoldDust } from "../fx/GoldDust";
import { BARKADA } from "./timing";
import { CanvasPhone, CloseLockup, FadeUp, prog } from "./blocks";
import { InsightCard, MomentHeader, MomentLog, ScenarioScene, TimeChip } from "./scenario-blocks";
import { BurstDust, ReceiptRoll } from "./flash";
import { ScenarioBarkadaAudio } from "./audio2";

const W = 1080;
const H = 1920;
const PHONE = { x: W / 2, y: 1176, height: 1180 };

const BILL = [
  ["Sisig", "285.00"],
  ["Chicken bbq (4)", "480.00"],
  ["Sinigang bowl", "350.00"],
  ["Rice platter", "180.00"],
  ["Iced tea pitcher", "165.00"],
  ["Halo-halo (2)", "240.00"],
  ["Extra rice", "60.00"],
  ["Service", "160.00"],
] as const;

/**
 * "The Friday Split" — barkada dinner, bill divided on screen, your share
 * sealed as a Want with zero shame. Joy is a line item.
 */
export const ScenarioBarkada: React.FC = () => {
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
        <ScenarioScene from={BARKADA.hook} duration={BARKADA.split - BARKADA.hook} fadeIn={4}>
          <BillHook />
        </ScenarioScene>

        <ScenarioScene from={BARKADA.split} duration={BARKADA.log - BARKADA.split}>
          <SplitFour />
        </ScenarioScene>

        <ScenarioScene from={BARKADA.log} duration={BARKADA.allowed - BARKADA.log}>
          <MomentHeader chip="9:41 PM" line="Your share. Your call." accent={COLORS.crimson} />
          <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
            <MomentLog
              spec={{
                item: "Barkada dinner",
                costDigits: "480",
                costCents: 48000,
                type: "WANT",
                time: "9:41",
                dateLabel: "FRIDAY · AUG 14",
                priorRows: [
                  { id: "merienda", time: "4:15", item: "Merienda", cost: "₱65.00", type: "WANT" },
                  { id: "jeep", time: "6:52", item: "Jeep fare", cost: "₱26.00", type: "NEED" },
                ],
                sheetBefore: 2,
                chipAt: 66,
              }}
            />
          </CanvasPhone>
        </ScenarioScene>

        <ScenarioScene from={BARKADA.allowed} duration={BARKADA.insight - BARKADA.allowed}>
          <AllowedBeat />
        </ScenarioScene>

        <ScenarioScene from={BARKADA.insight} duration={BARKADA.close - BARKADA.insight}>
          <div style={{ position: "absolute", top: 620, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
            <InsightCard
              eyebrow="Friday, fully accounted"
              headline="Fun is a line item."
              sub="Enjoy it in full. Label it in one tap. Owe your budget nothing."
              stats={[
                { label: "Dinner", value: "₱480.00", accent: COLORS.crimson },
                { label: "Split", value: "4 ways", accent: COLORS.ink },
                { label: "Guilt", value: "₱0.00", accent: COLORS.goldDeep },
              ]}
            />
          </div>
        </ScenarioScene>

        <ScenarioScene from={BARKADA.close} duration={BARKADA.total - BARKADA.close} fadeOut={10}>
          <GoldDust frame={frame} count={20} area={{ x: 60, y: 300, w: 960, h: 1300 }} opacity={0.8} scale={1.1} />
          <div style={{ position: "absolute", top: 636, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
            <CloseLockup sealAt={8} scale={0.9} tagline="Enjoy it. Label it." />
          </div>
        </ScenarioScene>
      </AbsoluteFill>

      <ScenarioBarkadaAudio />
    </AbsoluteFill>
  );
};

/* ────────────────────────────────────────────────────────────────────────── */

const Glass: React.FC<{ flip?: boolean; tilt: number }> = ({ flip = false, tilt }) => (
  <svg
    width="92"
    height="132"
    viewBox="0 0 92 132"
    style={{
      transform: `scaleX(${flip ? -1 : 1}) rotate(${tilt}deg)`,
      transformOrigin: "bottom center",
    }}
  >
    <path
      d="M22 8 H70 L63 56 Q46 74 29 56 Z"
      fill="rgba(244,201,104,0.3)"
      stroke={COLORS.ink}
      strokeWidth="3"
      strokeLinejoin="round"
    />
    <path d="M27 30 H65" stroke="rgba(232,169,42,0.8)" strokeWidth="4" />
    <line x1="46" y1="72" x2="46" y2="110" stroke={COLORS.ink} strokeWidth="3" />
    <path d="M28 116 Q46 108 64 116" fill="none" stroke={COLORS.ink} strokeWidth="3" strokeLinecap="round" />
  </svg>
);

const BillHook: React.FC = () => {
  const f = useCurrentFrame();
  const meet = prog(f, 8, 24, EASE_EDIT);
  const clink = prog(f, 24, 40);
  const scroll = interpolate(f, [16, 96], [0, 115], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });

  return (
    <AbsoluteFill>
      <FadeUp at={2} style={{ position: "absolute", top: 138, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
        <TimeChip label="Friday · 9:26 PM" accent={COLORS.crimson} />
      </FadeUp>
      <FadeUp at={10} style={{ position: "absolute", top: 236, left: 0, right: 0, textAlign: "center" }}>
        <div style={{ fontFamily: SERIF_STACK, fontSize: 64, fontWeight: 700, color: COLORS.ink }}>
          Friday happened.
        </div>
      </FadeUp>

      {/* glasses meet over the bill */}
      <div style={{ position: "absolute", top: 356, left: 0, right: 0, display: "flex", justifyContent: "center", gap: 4 }}>
        <div style={{ transform: `translateX(${(1 - meet) * -60}px)` }}>
          <Glass tilt={interpolate(meet, [0, 1], [-4, 11])} />
        </div>
        <div style={{ position: "relative", width: 10 }}>
          <BurstDust p={clink} radius={90} count={9} />
        </div>
        <div style={{ transform: `translateX(${(1 - meet) * 60}px)` }}>
          <Glass flip tilt={interpolate(meet, [0, 1], [-4, 11])} />
        </div>
      </div>

      {/* the long receipt, rolling */}
      <div style={{ position: "absolute", top: 540, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
        <ReceiptRoll width={560} height={660} scroll={scroll}>
          <div
            style={{
              background: "#FFFEFB",
              borderRadius: 8,
              boxShadow: "0 26px 54px -26px rgba(26,26,26,0.4)",
              padding: "38px 40px 44px",
              fontFamily: FONT_STACK,
            }}
          >
            <div style={{ textAlign: "center", fontSize: 26, letterSpacing: 4, color: COLORS.muted, fontWeight: 600 }}>
              KAINAN SA KANTO
            </div>
            <div style={{ textAlign: "center", marginTop: 8, fontSize: 22, color: COLORS.muted }}>
              Table 7 · 4 pax
            </div>
            <div style={{ margin: "22px 0 16px", borderTop: `2px dashed ${COLORS.divider}` }} />
            {BILL.map(([item, cost]) => (
              <div
                key={item}
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  padding: "12px 0",
                  fontSize: 30,
                  color: COLORS.ink,
                  fontVariantNumeric: "tabular-nums",
                }}
              >
                <span>{item}</span>
                <span style={{ fontWeight: 600 }}>{cost}</span>
              </div>
            ))}
            <div style={{ margin: "18px 0 20px", borderTop: `2px dashed ${COLORS.divider}` }} />
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                fontSize: 34,
                fontWeight: 700,
                color: COLORS.ink,
                fontVariantNumeric: "tabular-nums",
              }}
            >
              <span>TOTAL</span>
              <span>₱1,920.00</span>
            </div>
          </div>
        </ReceiptRoll>
      </div>
    </AbsoluteFill>
  );
};

/** ₱1,920 divides into four flying ₱480 chips; yours lands highlighted. */
const SplitFour: React.FC = () => {
  const f = useCurrentFrame();
  const totalIn = prog(f, 6, 22);
  const divideIn = prog(f, 30, 44);

  const people = [
    { label: "J", x: 264, y: 1010, gold: false },
    { label: "M", x: 816, y: 1010, gold: false },
    { label: "A", x: 264, y: 1330, gold: false },
    { label: "YOU", x: 816, y: 1330, gold: true },
  ];

  return (
    <AbsoluteFill>
      <FadeUp at={2} style={{ position: "absolute", top: 210, left: 0, right: 0, textAlign: "center" }}>
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
          The damage
        </div>
      </FadeUp>

      {/* the total */}
      <div
        style={{
          position: "absolute",
          top: 320,
          left: 0,
          right: 0,
          textAlign: "center",
          opacity: totalIn,
          transform: `scale(${0.9 + totalIn * 0.1})`,
        }}
      >
        <span
          style={{
            fontFamily: FONT_STACK,
            fontWeight: 700,
            fontSize: 132,
            color: COLORS.ink,
            fontVariantNumeric: "tabular-nums",
          }}
        >
          ₱1,920
        </span>
      </div>

      {/* divided by four */}
      <div
        style={{
          position: "absolute",
          top: 540,
          left: 0,
          right: 0,
          display: "flex",
          justifyContent: "center",
          opacity: divideIn,
          transform: `translateY(${(1 - divideIn) * 16}px)`,
        }}
      >
        <div
          style={{
            padding: "14px 36px",
            borderRadius: 999,
            background: COLORS.card,
            border: `2px solid ${COLORS.goldDeep}`,
            fontFamily: FONT_STACK,
            fontSize: 40,
            fontWeight: 700,
            color: COLORS.goldDeep,
            boxShadow: "0 14px 30px -18px rgba(26,26,26,0.35)",
          }}
        >
          ÷ 4
        </div>
      </div>

      {/* four shares fly out */}
      {people.map((pp, i) => {
        const p = prog(f, 56 + i * 5, 84 + i * 5, EASE_EDIT);
        const x = interpolate(p, [0, 1], [W / 2, pp.x]);
        const y = interpolate(p, [0, 1], [700, pp.y]) - Math.sin(Math.PI * p) * 60;
        const youPulse = pp.gold ? prog(f, 108, 130) : 0;
        return (
          <div
            key={pp.label}
            style={{
              position: "absolute",
              left: x,
              top: y,
              transform: `translate(-50%, -50%) scale(${0.55 + p * 0.45})`,
              opacity: Math.min(1, p * 1.6),
            }}
          >
            <div
              style={{
                display: "flex",
                alignItems: "center",
                gap: 16,
                padding: "18px 30px",
                borderRadius: 22,
                background: COLORS.card,
                border: `2.5px solid ${pp.gold ? COLORS.goldDeep : COLORS.divider}`,
                boxShadow: pp.gold
                  ? "0 24px 48px -20px rgba(185,136,30,0.5)"
                  : "0 18px 36px -20px rgba(26,26,26,0.3)",
                position: "relative",
                transform: youPulse > 0 && youPulse < 1 ? `scale(${1 + Math.sin(youPulse * Math.PI) * 0.05})` : undefined,
              }}
            >
              <div
                style={{
                  width: 56,
                  height: 56,
                  borderRadius: 28,
                  background: pp.gold ? "rgba(232,169,42,0.2)" : COLORS.raised,
                  border: `2px solid ${pp.gold ? COLORS.goldDeep : COLORS.dividerStrong}`,
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  fontFamily: FONT_STACK,
                  fontSize: pp.label.length > 1 ? 18 : 26,
                  fontWeight: 700,
                  color: pp.gold ? COLORS.goldDeep : COLORS.sub,
                }}
              >
                {pp.label}
              </div>
              <span
                style={{
                  fontFamily: FONT_STACK,
                  fontSize: 40,
                  fontWeight: 700,
                  color: COLORS.ink,
                  fontVariantNumeric: "tabular-nums",
                }}
              >
                ₱480.00
              </span>
            </div>
          </div>
        );
      })}

      <FadeUp at={116} style={{ position: "absolute", top: 1540, left: 0, right: 0, textAlign: "center" }}>
        <div style={{ fontFamily: SERIF_STACK, fontStyle: "italic", fontSize: 44, color: COLORS.goldDeep }}>
          Your share of the good time.
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};

const AllowedBeat: React.FC = () => {
  const f = useCurrentFrame();
  const land = prog(f, 12, 26);
  const burst = prog(f, 22, 52);
  return (
    <AbsoluteFill>
      <div
        style={{
          position: "absolute",
          top: 700,
          left: 0,
          right: 0,
          textAlign: "center",
          opacity: land,
          transform: `scale(${1.12 - land * 0.12})`,
        }}
      >
        <div style={{ position: "relative", display: "inline-block", padding: "0 40px" }}>
          <BurstDust p={burst} radius={300} count={16} />
          <div style={{ fontFamily: SERIF_STACK, fontSize: 92, fontWeight: 700, color: COLORS.ink }}>
            Wants are <span style={{ color: COLORS.crimson }}>allowed.</span>
          </div>
        </div>
      </div>
      <FadeUp at={40} style={{ position: "absolute", top: 900, left: 0, right: 0, textAlign: "center", padding: "0 110px" }}>
        <div style={{ fontFamily: FONT_STACK, fontSize: 36, lineHeight: 1.5, color: COLORS.sub }}>
          This is not a guilt app. It is a clarity app.
          <br />
          Joy just gets its honest label.
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};
