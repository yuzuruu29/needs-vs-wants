import React from "react";
import { AbsoluteFill, Sequence, interpolate, useCurrentFrame } from "remotion";
import { COLORS, EASE_EDIT, FONT_STACK, SERIF_STACK } from "../theme";
import { PaperBackground } from "../fx/PaperBackground";
import { GoldDust } from "../fx/GoldDust";
import { AD } from "./timing";
import {
  CanvasPhone,
  CloseLockup,
  FadeUp,
  Kicker,
  ProofCard,
  Receipt,
  SealBadge,
  StampWord,
  prog,
} from "./blocks";
import { GuardDemo, LogDemo, SummaryDemo } from "./demos";
import { EssenceAdAudio } from "./audio";

const W = 1920;
const H = 1080;

/** Scene shell: soft fade from/to the paper void at both edges. */
const Scene: React.FC<
  React.PropsWithChildren<{ from: number; duration: number; fadeIn?: number; fadeOut?: number }>
> = ({ from, duration, fadeIn = 12, fadeOut = 14, children }) => {
  return (
    <Sequence from={from} durationInFrames={duration}>
      <SceneFade duration={duration} fadeIn={fadeIn} fadeOut={fadeOut}>
        {children}
      </SceneFade>
    </Sequence>
  );
};

const SceneFade: React.FC<
  React.PropsWithChildren<{ duration: number; fadeIn: number; fadeOut: number }>
> = ({ duration, fadeIn, fadeOut, children }) => {
  const f = useCurrentFrame();
  const opacity =
    prog(f, 0, fadeIn) * (1 - prog(f, duration - fadeOut, duration - 2));
  return <AbsoluteFill style={{ opacity }}>{children}</AbsoluteFill>;
};

/** Left/right editorial panel for the three how-to steps. */
const StepPanel: React.FC<{
  x: number;
  width: number;
  step: string;
  title: string;
  body: string;
  align?: "left" | "right";
}> = ({ x, width, step, title, body, align = "left" }) => {
  const f = useCurrentFrame();
  return (
    <div
      style={{
        position: "absolute",
        left: x,
        top: 0,
        bottom: 0,
        width,
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: align === "left" ? "flex-start" : "flex-end",
        textAlign: align,
      }}
    >
      <FadeUp at={8}>
        <Kicker text="How it works" align={align === "left" ? "flex-start" : "center"} />
      </FadeUp>
      <FadeUp at={16} style={{ marginTop: 34 }}>
        <div style={{ display: "flex", alignItems: "baseline", gap: 26 }}>
          <span
            style={{
              fontFamily: SERIF_STACK,
              fontSize: 132,
              fontWeight: 700,
              color: COLORS.gold,
              lineHeight: 1,
              textShadow: "0 6px 22px rgba(232,169,42,0.35)",
            }}
          >
            {step}
          </span>
          <span
            style={{
              fontFamily: SERIF_STACK,
              fontSize: 78,
              fontWeight: 700,
              color: COLORS.ink,
              lineHeight: 1.04,
              letterSpacing: 0.5,
            }}
          >
            {title}
          </span>
        </div>
      </FadeUp>
      <FadeUp at={30} style={{ marginTop: 30, maxWidth: width - 30 }}>
        <div
          style={{
            fontFamily: FONT_STACK,
            fontSize: 38,
            lineHeight: 1.52,
            color: COLORS.sub,
            letterSpacing: -0.2,
          }}
        >
          {body}
        </div>
      </FadeUp>
      {/* progress ticks */}
      <FadeUp at={40} style={{ marginTop: 44 }}>
        <div style={{ display: "flex", gap: 12 }}>
          {["01", "02", "03"].map((s) => (
            <div
              key={s}
              style={{
                width: s === step ? 54 : 22,
                height: 5,
                borderRadius: 3,
                background: s === step ? COLORS.gold : COLORS.dividerStrong,
              }}
            />
          ))}
        </div>
      </FadeUp>
      {/* keep panel breathing subtly */}
      <div style={{ height: Math.sin(f * 0.02) * 0.001 }} />
    </div>
  );
};

/* ────────────────────────────────────────────────────────────────────────── */

const Hook: React.FC = () => {
  const f = useCurrentFrame();
  const rule = prog(f, 34, 58);
  return (
    <AbsoluteFill>
      <FadeUp
        at={6}
        duration={20}
        style={{ position: "absolute", top: 178, left: 0, right: 0, textAlign: "center" }}
      >
        <div
          style={{
            fontFamily: FONT_STACK,
            fontSize: 52,
            fontWeight: 500,
            letterSpacing: -0.3,
            color: COLORS.sub,
          }}
        >
          Every peso asks one question.
        </div>
      </FadeUp>

      {/* the two words */}
      <div
        style={{
          position: "absolute",
          top: 360,
          left: 0,
          right: 0,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
          gap: 96,
        }}
      >
        <StampWord word="NEED" color={COLORS.green} at={38} size={188} tilt={-2} />
        <div
          style={{
            width: 4,
            height: 210 * rule,
            borderRadius: 2,
            background: `linear-gradient(180deg, rgba(232,169,42,0) 0%, ${COLORS.gold} 30%, ${COLORS.gold} 70%, rgba(232,169,42,0) 100%)`,
            boxShadow: "0 0 18px rgba(232,169,42,0.4)",
          }}
        />
        <StampWord word="WANT" color={COLORS.crimson} at={52} size={188} tilt={2} />
      </div>

      <FadeUp
        at={92}
        duration={20}
        style={{ position: "absolute", top: 742, left: 0, right: 0, textAlign: "center" }}
      >
        <div
          style={{
            fontFamily: SERIF_STACK,
            fontStyle: "italic",
            fontSize: 46,
            color: COLORS.goldDeep,
            letterSpacing: 1,
          }}
        >
          Answer it while you are still holding the receipt.
        </div>
      </FadeUp>
    </AbsoluteFill>
  );
};

const Essence: React.FC = () => {
  const f = useCurrentFrame();
  const receiptIn = prog(f, 16, 40);
  const strike = prog(f, 46, 62);
  return (
    <AbsoluteFill>
      <div
        style={{
          position: "absolute",
          left: 170,
          top: 0,
          bottom: 0,
          width: 900,
          display: "flex",
          flexDirection: "column",
          justifyContent: "center",
        }}
      >
        <FadeUp at={6}>
          <Kicker text="The idea" />
        </FadeUp>
        <FadeUp at={16} style={{ marginTop: 40 }}>
          <div style={{ position: "relative", display: "inline-block" }}>
            <div
              style={{
                fontFamily: SERIF_STACK,
                fontSize: 74,
                fontWeight: 700,
                color: COLORS.muted,
                lineHeight: 1.1,
              }}
            >
              Most budget apps
              <br />
              are ledgers.
            </div>
            {/* crimson strike-through, drawn like a pen stroke */}
            <div
              style={{
                position: "absolute",
                left: -8,
                top: "52%",
                width: `${strike * 103}%`,
                height: 5,
                borderRadius: 3,
                background: COLORS.crimson,
                transform: "rotate(-1.6deg)",
                opacity: 0.82,
              }}
            />
          </div>
        </FadeUp>
        <FadeUp at={58} style={{ marginTop: 42 }}>
          <div
            style={{
              fontFamily: SERIF_STACK,
              fontSize: 88,
              fontWeight: 700,
              color: COLORS.ink,
              lineHeight: 1.08,
            }}
          >
            This one is a{" "}
            <span style={{ color: COLORS.goldDeep, fontStyle: "italic" }}>trainer.</span>
          </div>
        </FadeUp>
        <FadeUp at={92} style={{ marginTop: 38 }}>
          <div
            style={{
              fontFamily: FONT_STACK,
              fontSize: 38,
              lineHeight: 1.5,
              color: COLORS.sub,
              maxWidth: 780,
            }}
          >
            One honest call at the moment you pay, so impulse gets caught in the
            act, not in the month-end autopsy.
          </div>
        </FadeUp>
      </div>

      {/* receipt with the WANT verdict */}
      <div
        style={{
          position: "absolute",
          left: 1270,
          top: 250,
          opacity: receiptIn,
          transform: `translateY(${(1 - receiptIn) * 60}px) rotate(${3 - receiptIn * 0.6}deg)`,
        }}
      >
        <Receipt width={470} stampAt={74} />
      </div>
    </AbsoluteFill>
  );
};

const ProofScene: React.FC = () => {
  const f = useCurrentFrame();
  const cards = [
    {
      icon: "offline" as const,
      title: "Offline-first",
      sub: "Your ledger lives on your phone. No account needed to start.",
      accent: COLORS.green,
      at: 14,
    },
    {
      icon: "free" as const,
      title: "Free every day",
      sub: "5 free logs a day. 30 days of rolling history.",
      accent: COLORS.goldDeep,
      at: 26,
    },
    {
      icon: "streak" as const,
      title: "Streaks that stick",
      sub: "Keep the chain alive one honest day at a time.",
      accent: COLORS.crimson,
      at: 38,
    },
    {
      icon: "coach" as const,
      title: "Max: an AI coach",
      sub: "Advice grounded in cited economic study notes.",
      accent: COLORS.ink,
      at: 50,
    },
  ];
  return (
    <AbsoluteFill>
      <FadeUp at={4} style={{ position: "absolute", top: 168, left: 0, right: 0 }}>
        <div style={{ display: "flex", flexDirection: "column", alignItems: "center", gap: 18 }}>
          <Kicker text="Built for real life" align="center" />
          <div
            style={{
              fontFamily: SERIF_STACK,
              fontSize: 68,
              fontWeight: 700,
              color: COLORS.ink,
            }}
          >
            Small app. Strong spine.
          </div>
        </div>
      </FadeUp>
      <div
        style={{
          position: "absolute",
          top: 456,
          left: 0,
          right: 0,
          display: "flex",
          justifyContent: "center",
          gap: 40,
        }}
      >
        {cards.map((c) => (
          <ProofCard
            key={c.icon}
            icon={c.icon}
            title={c.title}
            sub={c.sub}
            accent={c.accent}
            progress={prog(f, c.at, c.at + 18)}
          />
        ))}
      </div>
    </AbsoluteFill>
  );
};

const Close: React.FC = () => {
  const f = useCurrentFrame();
  return (
    <AbsoluteFill>
      <GoldDust
        frame={f}
        count={22}
        area={{ x: 360, y: 120, w: 1200, h: 840 }}
        opacity={0.8}
        scale={1.1}
      />
      <div style={{ position: "absolute", top: 208, left: 0, right: 0, display: "flex", justifyContent: "center" }}>
        <CloseLockup sealAt={10} />
      </div>
    </AbsoluteFill>
  );
};

/* ────────────────────────────────────────────────────────────────────────── */

export const EssenceAd: React.FC = () => {
  const frame = useCurrentFrame();

  // gentle global fade-in from paper at the very start
  const boot = interpolate(frame, [0, 8], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });

  return (
    <AbsoluteFill style={{ width: W, height: H, background: COLORS.background }}>
      <PaperBackground />
      <GoldDust frame={frame} count={12} area={{ x: 0, y: 140, w: W, h: 900 }} opacity={0.4} />

      <AbsoluteFill style={{ opacity: boot }}>
        <Scene from={AD.hook} duration={AD.essence - AD.hook}>
          <Hook />
        </Scene>

        <Scene from={AD.essence} duration={AD.log - AD.essence}>
          <Essence />
        </Scene>

        <Scene from={AD.log} duration={AD.see - AD.log}>
          <StepPanel
            x={170}
            width={880}
            step="01"
            title="Log it as you spend."
            body="Item, cost, and one honest call: Need or Want. Twenty lines to a sheet, just like a real ledger."
          />
          <PhoneEntrance side="right">
            <CanvasPhone x={1428} y={556} height={900}>
              <LogDemo />
            </CanvasPhone>
          </PhoneEntrance>
        </Scene>

        <Scene from={AD.see} duration={AD.guard - AD.see}>
          <StepPanel
            x={1000}
            width={760}
            step="02"
            title="See the split."
            body="Needs in green. Wants in crimson. The ring tells the truth about your week."
          />
          <PhoneEntrance side="left">
            <CanvasPhone x={492} y={556} height={900}>
              <SummaryDemo />
            </CanvasPhone>
          </PhoneEntrance>
        </Scene>

        <Scene from={AD.guard} duration={AD.proof - AD.guard}>
          <StepPanel
            x={170}
            width={880}
            step="03"
            title="Guard your day."
            body="Set a daily line. The app will not stop you at the border, but you will cross it on purpose."
          />
          <PhoneEntrance side="right">
            <CanvasPhone x={1428} y={556} height={900}>
              <GuardDemo />
            </CanvasPhone>
          </PhoneEntrance>
        </Scene>

        <Scene from={AD.proof} duration={AD.close - AD.proof}>
          <ProofScene />
        </Scene>

        <Scene from={AD.close} duration={AD.total - AD.close} fadeOut={10}>
          <Close />
        </Scene>
      </AbsoluteFill>

      {/* tiny corner wordmark, present through the how-to body */}
      <Sequence from={AD.log} durationInFrames={AD.close - AD.log}>
        <CornerMark />
      </Sequence>

      <EssenceAdAudio />
    </AbsoluteFill>
  );
};

const PhoneEntrance: React.FC<React.PropsWithChildren<{ side: "left" | "right" }>> = ({
  side,
  children,
}) => {
  const f = useCurrentFrame();
  const p = prog(f, 0, 20);
  const dx = (1 - p) * (side === "right" ? 190 : -190);
  return (
    <div style={{ position: "absolute", inset: 0, opacity: p, transform: `translateX(${dx}px)` }}>
      {children}
    </div>
  );
};

const CornerMark: React.FC = () => {
  const f = useCurrentFrame();
  const p = prog(f, 0, 16);
  return (
    <div
      style={{
        position: "absolute",
        left: 64,
        bottom: 46,
        display: "flex",
        alignItems: "center",
        gap: 16,
        opacity: p * 0.85,
      }}
    >
      <SealBadge size={44} />
      <span
        style={{
          fontFamily: SERIF_STACK,
          fontSize: 27,
          fontWeight: 700,
          letterSpacing: 2.4,
          color: COLORS.sub,
          fontVariantCaps: "small-caps",
        }}
      >
        NEEDS VS WANTS
      </span>
    </div>
  );
};
