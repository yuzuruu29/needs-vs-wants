import React from "react";
import { AbsoluteFill, Sequence, interpolate, useCurrentFrame } from "remotion";
import { COLORS, EASE_EDIT, FONT_STACK, SERIF_STACK } from "../theme";
import { PaperBackground } from "../fx/PaperBackground";
import { GoldDust } from "../fx/GoldDust";
import { REEL } from "./timing";
import { CanvasPhone, CloseLockup, FadeUp, Kicker, StampWord, prog } from "./blocks";
import { GuardDemo, LogDemo, SummaryDemo } from "./demos";
import { EssenceReelAudio } from "./audio";

const W = 1080;
const H = 1920;

const SceneFade: React.FC<
  React.PropsWithChildren<{ duration: number; fadeIn?: number; fadeOut?: number }>
> = ({ duration, fadeIn = 10, fadeOut = 12, children }) => {
  const f = useCurrentFrame();
  const opacity = prog(f, 0, fadeIn) * (1 - prog(f, duration - fadeOut, duration - 2));
  return <AbsoluteFill style={{ opacity }}>{children}</AbsoluteFill>;
};

/** Top-of-frame step header for the vertical cut. */
const StepHeader: React.FC<{ step: string; title: string; sub?: string }> = ({
  step,
  title,
  sub,
}) => (
  <div
    style={{
      position: "absolute",
      top: 128,
      left: 0,
      right: 0,
      display: "flex",
      flexDirection: "column",
      alignItems: "center",
      textAlign: "center",
      padding: "0 70px",
    }}
  >
    <FadeUp at={6}>
      <Kicker text="How it works" align="center" size={24} />
    </FadeUp>
    <FadeUp at={14} style={{ marginTop: 24 }}>
      <div style={{ display: "flex", alignItems: "baseline", gap: 20, justifyContent: "center" }}>
        <span
          style={{
            fontFamily: SERIF_STACK,
            fontSize: 88,
            fontWeight: 700,
            color: COLORS.gold,
            lineHeight: 1,
            textShadow: "0 5px 18px rgba(232,169,42,0.35)",
          }}
        >
          {step}
        </span>
        <span
          style={{
            fontFamily: SERIF_STACK,
            fontSize: 62,
            fontWeight: 700,
            color: COLORS.ink,
            lineHeight: 1.05,
            whiteSpace: "nowrap",
          }}
        >
          {title}
        </span>
      </div>
    </FadeUp>
    {sub ? (
      <FadeUp at={24} style={{ marginTop: 20 }}>
        <div style={{ fontFamily: FONT_STACK, fontSize: 32, lineHeight: 1.45, color: COLORS.sub }}>
          {sub}
        </div>
      </FadeUp>
    ) : null}
    <FadeUp at={32} style={{ marginTop: 26 }}>
      <div style={{ display: "flex", gap: 10, justifyContent: "center" }}>
        {["01", "02", "03"].map((s) => (
          <div
            key={s}
            style={{
              width: s === step ? 46 : 18,
              height: 5,
              borderRadius: 3,
              background: s === step ? COLORS.gold : COLORS.dividerStrong,
            }}
          />
        ))}
      </div>
    </FadeUp>
  </div>
);

const PhoneRise: React.FC<React.PropsWithChildren> = ({ children }) => {
  const f = useCurrentFrame();
  const p = prog(f, 4, 26);
  return (
    <div style={{ position: "absolute", inset: 0, opacity: p, transform: `translateY(${(1 - p) * 90}px)` }}>
      {children}
    </div>
  );
};

const ReelHook: React.FC = () => {
  const f = useCurrentFrame();
  const rule = prog(f, 34, 56);
  return (
    <AbsoluteFill>
      <FadeUp
        at={6}
        style={{ position: "absolute", top: 400, left: 0, right: 0, textAlign: "center", padding: "0 90px" }}
      >
        <div style={{ fontFamily: FONT_STACK, fontSize: 46, fontWeight: 500, color: COLORS.sub }}>
          Every peso asks one question.
        </div>
      </FadeUp>
      <div
        style={{
          position: "absolute",
          top: 640,
          left: 0,
          right: 0,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          gap: 54,
        }}
      >
        <StampWord word="NEED" color={COLORS.green} at={38} size={124} tilt={-2} />
        <div
          style={{
            width: 4,
            height: 150 * rule,
            borderRadius: 2,
            background: `linear-gradient(180deg, rgba(232,169,42,0) 0%, ${COLORS.gold} 30%, ${COLORS.gold} 70%, rgba(232,169,42,0) 100%)`,
            boxShadow: "0 0 16px rgba(232,169,42,0.4)",
          }}
        />
        <StampWord word="WANT" color={COLORS.crimson} at={52} size={124} tilt={2} />
      </div>
      <FadeUp
        at={70}
        style={{ position: "absolute", top: 950, left: 0, right: 0, textAlign: "center", padding: "0 90px" }}
      >
        <div
          style={{
            fontFamily: SERIF_STACK,
            fontStyle: "italic",
            fontSize: 40,
            lineHeight: 1.4,
            color: COLORS.goldDeep,
          }}
        >
          Answer it while you are
          <br />
          still holding the receipt.
        </div>
      </FadeUp>
      <FadeUp at={82} style={{ position: "absolute", top: 1330, left: 0, right: 0, textAlign: "center" }}>
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
          A 30-day spending trainer
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};

export const EssenceReel: React.FC = () => {
  const frame = useCurrentFrame();
  const boot = interpolate(frame, [0, 8], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });

  const PHONE = { x: W / 2, y: 1128, height: 1230 };

  return (
    <AbsoluteFill style={{ width: W, height: H, background: COLORS.background }}>
      <PaperBackground />
      <GoldDust frame={frame} count={12} area={{ x: 0, y: 160, w: W, h: 1500 }} opacity={0.4} />

      <AbsoluteFill style={{ opacity: boot }}>
        <Sequence from={REEL.hook} durationInFrames={REEL.log - REEL.hook}>
          <SceneFade duration={REEL.log - REEL.hook}>
            <ReelHook />
          </SceneFade>
        </Sequence>

        <Sequence from={REEL.log} durationInFrames={REEL.see - REEL.log}>
          <SceneFade duration={REEL.see - REEL.log}>
            <StepHeader step="01" title="Log it as you spend." sub="Item, cost, one honest call." />
            <PhoneRise>
              <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
                <LogDemo />
              </CanvasPhone>
            </PhoneRise>
          </SceneFade>
        </Sequence>

        <Sequence from={REEL.see} durationInFrames={REEL.guard - REEL.see}>
          <SceneFade duration={REEL.guard - REEL.see}>
            <StepHeader step="02" title="See the split." sub="Needs in green. Wants in crimson." />
            <PhoneRise>
              <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
                <SummaryDemo />
              </CanvasPhone>
            </PhoneRise>
          </SceneFade>
        </Sequence>

        <Sequence from={REEL.guard} durationInFrames={REEL.close - REEL.guard}>
          <SceneFade duration={REEL.close - REEL.guard}>
            <StepHeader step="03" title="Guard your day." sub="Cross your daily line on purpose." />
            <PhoneRise>
              <CanvasPhone x={PHONE.x} y={PHONE.y} height={PHONE.height}>
                <GuardDemo />
              </CanvasPhone>
            </PhoneRise>
          </SceneFade>
        </Sequence>

        <Sequence from={REEL.close} durationInFrames={REEL.total - REEL.close}>
          <SceneFade duration={REEL.total - REEL.close} fadeOut={10}>
            <CloseReel />
          </SceneFade>
        </Sequence>
      </AbsoluteFill>

      <EssenceReelAudio />
    </AbsoluteFill>
  );
};

const CloseReel: React.FC = () => {
  const f = useCurrentFrame();
  return (
    <AbsoluteFill>
      <GoldDust frame={f} count={20} area={{ x: 60, y: 300, w: 960, h: 1300 }} opacity={0.8} scale={1.1} />
      <div
        style={{
          position: "absolute",
          top: 636,
          left: 0,
          right: 0,
          display: "flex",
          justifyContent: "center",
        }}
      >
        <CloseLockup sealAt={8} scale={0.9} />
      </div>
    </AbsoluteFill>
  );
};
