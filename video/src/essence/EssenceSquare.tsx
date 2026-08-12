import React from "react";
import { AbsoluteFill, Sequence, interpolate, useCurrentFrame } from "remotion";
import { COLORS, EASE_EDIT, FONT_STACK, SERIF_STACK } from "../theme";
import { PaperBackground } from "../fx/PaperBackground";
import { GoldDust } from "../fx/GoldDust";
import { Donut } from "../ui/donut";
import { LedgerRow } from "../ui/ledger";
import { SQ } from "./timing";
import { CloseLockup, FadeUp, Kicker, StampWord, prog } from "./blocks";
import { EssenceSquareAudio } from "./audio";

const W = 1080;
const H = 1080;

const SceneFade: React.FC<
  React.PropsWithChildren<{ duration: number; fadeIn?: number; fadeOut?: number }>
> = ({ duration, fadeIn = 10, fadeOut = 12, children }) => {
  const f = useCurrentFrame();
  const opacity = prog(f, 0, fadeIn) * (1 - prog(f, duration - fadeOut, duration - 2));
  return <AbsoluteFill style={{ opacity }}>{children}</AbsoluteFill>;
};

const SqHook: React.FC = () => {
  const f = useCurrentFrame();
  const rule = prog(f, 34, 56);
  return (
    <AbsoluteFill>
      <FadeUp at={6} style={{ position: "absolute", top: 218, left: 0, right: 0, textAlign: "center" }}>
        <div style={{ fontFamily: FONT_STACK, fontSize: 42, fontWeight: 500, color: COLORS.sub }}>
          Every peso asks one question.
        </div>
      </FadeUp>
      <div
        style={{
          position: "absolute",
          top: 400,
          left: 0,
          right: 0,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          gap: 50,
        }}
      >
        <StampWord word="NEED" color={COLORS.green} at={38} size={116} tilt={-2} />
        <div
          style={{
            width: 4,
            height: 140 * rule,
            borderRadius: 2,
            background: `linear-gradient(180deg, rgba(232,169,42,0) 0%, ${COLORS.gold} 30%, ${COLORS.gold} 70%, rgba(232,169,42,0) 100%)`,
            boxShadow: "0 0 16px rgba(232,169,42,0.4)",
          }}
        />
        <StampWord word="WANT" color={COLORS.crimson} at={52} size={116} tilt={2} />
      </div>
      <FadeUp at={72} style={{ position: "absolute", top: 690, left: 0, right: 0, textAlign: "center" }}>
        <div
          style={{
            fontFamily: SERIF_STACK,
            fontStyle: "italic",
            fontSize: 38,
            color: COLORS.goldDeep,
          }}
        >
          Answer it while you are still holding the receipt.
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};

const SqSplit: React.FC = () => {
  const f = useCurrentFrame();
  const needPct = Math.round(
    interpolate(f, [12, 46], [100, 64], {
      extrapolateLeft: "clamp",
      extrapolateRight: "clamp",
      easing: EASE_EDIT,
    }),
  );
  const totalCents = Math.round(
    interpolate(f, [12, 44], [0, 531000], {
      extrapolateLeft: "clamp",
      extrapolateRight: "clamp",
      easing: EASE_EDIT,
    }),
  );
  const money =
    "₱" +
    (totalCents / 100).toLocaleString("en-PH", {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    });
  const donutIn = prog(f, 4, 22);
  return (
    <AbsoluteFill>
      <FadeUp at={4} style={{ position: "absolute", top: 96, left: 0, right: 0 }}>
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center", gap: 16 }}>
          <Kicker text="The honest split" align="center" size={24} />
          <div style={{ fontFamily: SERIF_STACK, fontSize: 54, fontWeight: 700, color: COLORS.ink }}>
            One week, told honestly.
          </div>
        </div>
      </FadeUp>
      <div
        style={{
          position: "absolute",
          top: 300,
          left: 0,
          right: 0,
          display: "flex",
          justifyContent: "center",
          opacity: donutIn,
          transform: `scale(${0.92 + donutIn * 0.08})`,
        }}
      >
        <Donut
          needSweep={(needPct / 100) * 360}
          size={470}
          ringWidth={52}
          centerEyebrow="THIS WEEK"
          centerMoney={money}
          centerMoneySize={62}
        />
      </div>
      <FadeUp at={40} style={{ position: "absolute", top: 830, left: 0, right: 0 }}>
        <div style={{ display: "flex", justifyContent: "center", alignItems: "center", gap: 34 }}>
          <Legend color={COLORS.green} label={`Need ${needPct}%`} />
          <div style={{ width: 3, height: 34, background: COLORS.divider }} />
          <Legend color={COLORS.crimson} label={`Want ${100 - needPct}%`} />
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};

const Legend: React.FC<{ color: string; label: string }> = ({ color, label }) => (
  <div style={{ display: "flex", alignItems: "center", gap: 14 }}>
    <div style={{ width: 20, height: 20, borderRadius: 10, background: color }} />
    <span
      style={{
        fontFamily: FONT_STACK,
        fontSize: 34,
        color: COLORS.sub,
        fontVariantNumeric: "tabular-nums",
      }}
    >
      {label}
    </span>
  </div>
);

const SqSeal: React.FC = () => {
  const f = useCurrentFrame();
  const rowIn = prog(f, 4, 18);
  return (
    <AbsoluteFill>
      <FadeUp at={2} style={{ position: "absolute", top: 200, left: 0, right: 0 }}>
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center", gap: 16 }}>
          <Kicker text="One honest call" align="center" size={24} />
          <div style={{ fontFamily: SERIF_STACK, fontSize: 54, fontWeight: 700, color: COLORS.ink }}>
            Sealed in one tap.
          </div>
        </div>
      </FadeUp>
      <div
        style={{
          position: "absolute",
          top: 480,
          left: 0,
          right: 0,
          display: "flex",
          justifyContent: "center",
          opacity: rowIn,
          transform: `translateY(${(1 - rowIn) * 30}px)`,
        }}
      >
        <div style={{ width: 760, position: "relative", transform: "scale(1.12)" }}>
          <LedgerRow
            card
            row={{ id: "sq", time: "9:41", item: "Milk tea", cost: "₱120.00", type: "WANT" }}
            style={{ boxShadow: "0 24px 48px -26px rgba(26,26,26,0.4)" }}
          />
          <div style={{ position: "absolute", right: 56, top: -26 }}>
            <StampWord word="WANT" color={COLORS.crimson} at={22} size={58} tilt={-10} ringScale={0.6} />
          </div>
        </div>
      </div>
      <FadeUp at={40} style={{ position: "absolute", top: 700, left: 0, right: 0, textAlign: "center" }}>
        <div style={{ fontFamily: FONT_STACK, fontSize: 32, lineHeight: 1.5, color: COLORS.sub }}>
          No spreadsheets. No shame.
          <br />
          Just the truth, one line at a time.
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};

export const EssenceSquare: React.FC = () => {
  const frame = useCurrentFrame();
  const boot = interpolate(frame, [0, 8], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });

  return (
    <AbsoluteFill style={{ width: W, height: H, background: COLORS.background }}>
      <PaperBackground />
      <GoldDust frame={frame} count={10} area={{ x: 0, y: 120, w: W, h: 840 }} opacity={0.38} />

      <AbsoluteFill style={{ opacity: boot }}>
        <Sequence from={SQ.hook} durationInFrames={SQ.split - SQ.hook}>
          <SceneFade duration={SQ.split - SQ.hook}>
            <SqHook />
          </SceneFade>
        </Sequence>

        <Sequence from={SQ.split} durationInFrames={SQ.seal - SQ.split}>
          <SceneFade duration={SQ.seal - SQ.split}>
            <SqSplit />
          </SceneFade>
        </Sequence>

        <Sequence from={SQ.seal} durationInFrames={SQ.close - SQ.seal}>
          <SceneFade duration={SQ.close - SQ.seal}>
            <SqSeal />
          </SceneFade>
        </Sequence>

        <Sequence from={SQ.close} durationInFrames={SQ.total - SQ.close}>
          <SceneFade duration={SQ.total - SQ.close} fadeOut={10}>
            <GoldDust frame={frame} count={18} area={{ x: 80, y: 120, w: 920, h: 820 }} opacity={0.75} />
            <div style={{ position: "absolute", top: 276, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
              <CloseLockup sealAt={8} scale={0.72} />
            </div>
          </SceneFade>
        </Sequence>
      </AbsoluteFill>

      <EssenceSquareAudio />
    </AbsoluteFill>
  );
};
