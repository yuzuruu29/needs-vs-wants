// src/NeedsVsWantsPromo.tsx
//
// 45s · 16:9 (1920×1080) marketing promo — purpose + how-to.
// Narrative: Hook → Purpose → See → Seal → Guard → Benefits → Close
// Brand (D7): Inter body, Playfair Display SC display,
// Need = green, Want = crimson, gold trim. Paper atmosphere + offline audio.

import React from "react";
import {
  AbsoluteFill,
  Sequence,
  useCurrentFrame,
  useVideoConfig,
  spring,
  interpolate,
  Easing,
} from "remotion";
import { COLORS, FONT_STACK, SERIF_STACK, EASE_EDIT, EASE_SEAL } from "./theme";
import { PaperBackground } from "./fx/PaperBackground";
import { GoldDust } from "./fx/GoldDust";
import { PromoAudioBed } from "./audio/PromoAudioBed";
import {
  DUR_HOOK,
  DUR_PURPOSE,
  DUR_SEE,
  DUR_SEAL,
  DUR_GUARD,
  DUR_BENEFITS,
  DUR_CLOSE,
} from "./promoTiming";

export {
  DUR_HOOK,
  DUR_PURPOSE,
  DUR_SEE,
  DUR_SEAL,
  DUR_GUARD,
  DUR_BENEFITS,
  DUR_CLOSE,
  PROMO_TOTAL_FRAMES,
} from "./promoTiming";

// ============================================
// Spring / motion configs
// ============================================
const calmSpring = { damping: 200, stiffness: 120, mass: 0.8 };
const sealSpring = { damping: 16, stiffness: 190, mass: 0.55 };
const softSpring = { damping: 200, stiffness: 95, mass: 1 };
const cardSpring = { damping: 180, stiffness: 110, mass: 0.9 };

// ============================================
// Shared primitives
// ============================================

const SoftText: React.FC<{
  delay?: number;
  children: React.ReactNode;
  style?: React.CSSProperties;
  rise?: number;
}> = ({ delay = 0, children, style, rise = 18 }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();
  const progress = spring({ frame: frame - delay, fps, config: softSpring });

  return (
    <div
      style={{
        opacity: progress,
        transform: `translateY(${interpolate(progress, [0, 1], [rise, 0])}px)`,
        fontFamily: FONT_STACK,
        ...style,
      }}
    >
      {children}
    </div>
  );
};

const GoldHairline: React.FC<{
  delay?: number;
  width?: number;
  align?: "left" | "center";
}> = ({ delay = 0, width = 56, align = "center" }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();
  const progress = spring({ frame: frame - delay, fps, config: calmSpring });
  const w = interpolate(progress, [0, 1], [0, width]);

  return (
    <div
      style={{
        display: "flex",
        justifyContent: align === "center" ? "center" : "flex-start",
        width: "100%",
      }}
    >
      <div
        style={{
          width: w,
          height: 2.5,
          borderRadius: 2,
          background: `linear-gradient(90deg, ${COLORS.goldSoft}, ${COLORS.gold}, ${COLORS.goldDeep})`,
          opacity: progress,
          boxShadow: `0 0 12px rgba(232,169,42,0.35)`,
        }}
      />
    </div>
  );
};

const SceneFade: React.FC<{
  children: React.ReactNode;
  inEnd?: number;
  outStartFromEnd?: number;
  durationInFrames: number;
}> = ({ children, inEnd = 12, outStartFromEnd = 14, durationInFrames }) => {
  const frame = useCurrentFrame();
  const fadeIn = interpolate(frame, [0, inEnd], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const fadeOut =
    outStartFromEnd > 0
      ? interpolate(
          frame,
          [durationInFrames - outStartFromEnd, durationInFrames - 1],
          [1, 0],
          {
            extrapolateLeft: "clamp",
            extrapolateRight: "clamp",
            easing: Easing.in(Easing.cubic),
          },
        )
      : 1;

  return (
    <AbsoluteFill style={{ opacity: Math.min(fadeIn, fadeOut) }}>
      {children}
    </AbsoluteFill>
  );
};

/** Step badge for how-to beats — 01 · SEE */
const StepBadge: React.FC<{
  step: string;
  label: string;
  delay?: number;
  portrait?: boolean;
}> = ({ step, label, delay = 0, portrait = false }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();
  const p = spring({ frame: frame - delay, fps, config: calmSpring });

  return (
    <div
      style={{
        display: "inline-flex",
        alignItems: "center",
        gap: portrait ? 14 : 10,
        opacity: p,
        transform: `translateY(${interpolate(p, [0, 1], [10, 0])}px)`,
        fontFamily: FONT_STACK,
        marginBottom: portrait ? 22 : 14,
      }}
    >
      <span
        style={{
          fontSize: portrait ? 18 : 12,
          fontWeight: 700,
          letterSpacing: 1.4,
          color: COLORS.goldDeep,
          background: "rgba(232,169,42,0.12)",
          border: `1px solid rgba(232,169,42,0.35)`,
          borderRadius: 999,
          padding: portrait ? "8px 18px" : "5px 12px",
        }}
      >
        {step}
      </span>
      <span
        style={{
          fontSize: portrait ? 18 : 13,
          fontWeight: 700,
          letterSpacing: 2.2,
          textTransform: "uppercase",
          color: COLORS.muted,
        }}
      >
        {label}
      </span>
    </div>
  );
};

const TypeChip: React.FC<{
  type: "NEED" | "WANT";
  opacity?: number;
  selected?: boolean;
  size?: "sm" | "md" | "lg";
  portrait?: boolean;
}> = ({ type, opacity = 1, selected = true, size = "sm", portrait = false }) => {
  const isNeed = type === "NEED";
  const accent = isNeed ? COLORS.green : COLORS.crimson;
  const soft = isNeed ? "rgba(11,107,58,0.12)" : "rgba(200,16,46,0.12)";
  const fontSize =
    size === "lg" ? (portrait ? 34 : 22) : size === "md" ? 14 : 11;
  const pad =
    size === "lg"
      ? portrait
        ? "16px 42px"
        : "12px 28px"
      : size === "md"
        ? "6px 14px"
        : "4px 10px";

  return (
    <span
      style={{
        fontSize,
        fontWeight: 700,
        letterSpacing: size === "lg" ? (portrait ? 3 : 2.2) : 1.1,
        color: selected ? accent : COLORS.muted,
        background: selected ? soft : COLORS.raised,
        border: `1.5px solid ${selected ? `${accent}55` : COLORS.divider}`,
        borderRadius: 999,
        padding: pad,
        opacity,
        fontFamily: FONT_STACK,
        boxShadow: selected ? `0 0 0 1px ${accent}18` : "none",
        transform: selected ? "scale(1)" : "scale(0.96)",
      }}
    >
      {type}
    </span>
  );
};

const SealingRow: React.FC<{
  delay?: number;
  item: string;
  amount: string;
  type: "NEED" | "WANT";
  note?: string;
}> = ({ delay = 0, item, amount, type, note }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();
  const progress = spring({ frame: frame - delay, fps, config: sealSpring });
  const scale = interpolate(progress, [0, 1], [1.06, 1], { easing: EASE_SEAL });
  const opacity = interpolate(progress, [0, 0.2, 1], [0, 1, 1]);
  const checkOpacity = interpolate(progress, [0.42, 0.85], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const sealFlash = interpolate(progress, [0.35, 0.55, 0.9], [0, 0.55, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const accent = type === "NEED" ? COLORS.green : COLORS.crimson;
  const inkDry = interpolate(progress, [0.5, 1], [0.55, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  return (
    <div
      style={{
        display: "flex",
        alignItems: "center",
        justifyContent: "space-between",
        padding: "12px 14px",
        marginBottom: 8,
        borderRadius: 12,
        backgroundColor: COLORS.card,
        border: `1.5px solid ${accent}`,
        transform: `scale(${scale})`,
        opacity,
        boxShadow:
          progress > 0.5
            ? `0 8px 28px rgba(0,0,0,0.08), 0 0 0 1px ${accent}22, inset 0 0 0 1px rgba(255,255,255,0.65)`
            : "none",
        fontFamily: FONT_STACK,
        position: "relative",
        overflow: "hidden",
      }}
    >
      <div
        style={{
          position: "absolute",
          inset: 0,
          background: `radial-gradient(80% 120% at 90% 50%, ${accent} 0%, transparent 70%)`,
          opacity: sealFlash,
          pointerEvents: "none",
        }}
      />
      <div style={{ display: "flex", flexDirection: "column", gap: 2, position: "relative" }}>
        <span style={{ fontSize: 14, fontWeight: 600, color: COLORS.ink, opacity: inkDry }}>
          {item}
        </span>
        <span
          style={{
            fontSize: 12.5,
            color: COLORS.sub,
            fontVariantNumeric: "tabular-nums",
            fontWeight: 500,
          }}
        >
          {amount}
          {note ? (
            <span style={{ color: COLORS.muted, fontWeight: 400 }}> · {note}</span>
          ) : null}
        </span>
      </div>
      <div style={{ display: "flex", alignItems: "center", gap: 8, position: "relative" }}>
        <TypeChip type={type} />
        <span
          style={{
            opacity: checkOpacity,
            fontSize: 14,
            color: accent,
            fontWeight: 700,
            width: 20,
            height: 20,
            borderRadius: 999,
            background: `${accent}14`,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            lineHeight: 1,
          }}
        >
          ✓
        </span>
      </div>
    </div>
  );
};

const FeatureIcon: React.FC<{
  kind: "trainer" | "budget" | "offline" | "seal";
  portrait?: boolean;
}> = ({ kind, portrait = false }) => {
  const stroke =
    kind === "budget"
      ? COLORS.goldDeep
      : kind === "offline"
        ? COLORS.green
        : kind === "seal"
          ? COLORS.crimson
          : COLORS.crimson;
  const bg =
    kind === "budget"
      ? "rgba(232,169,42,0.12)"
      : kind === "offline"
        ? "rgba(11,107,58,0.1)"
        : "rgba(200,16,46,0.1)";

  return (
    <div
      style={{
        width: portrait ? 62 : 44,
        height: portrait ? 62 : 44,
        borderRadius: portrait ? 16 : 12,
        background: bg,
        border: `1px solid ${stroke}28`,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        marginBottom: portrait ? 20 : 14,
      }}
    >
      <svg width={portrait ? 32 : 22} height={portrait ? 32 : 22} viewBox="0 0 24 24" fill="none">
        {kind === "trainer" && (
          <>
            <rect x="4" y="3" width="16" height="18" rx="2" stroke={stroke} strokeWidth="1.8" />
            <path
              d="M8 8h8M8 12h8M8 16h5"
              stroke={stroke}
              strokeWidth="1.8"
              strokeLinecap="round"
            />
          </>
        )}
        {kind === "budget" && (
          <>
            <circle cx="12" cy="12" r="8.5" stroke={stroke} strokeWidth="1.8" />
            <path
              d="M12 7.5v9M9.5 10c0-1.2 1.1-2 2.5-2s2.5.8 2.5 2-1.1 2-2.5 2-2.5.8-2.5 2 1.1 2 2.5 2 2.5-.8 2.5-2"
              stroke={stroke}
              strokeWidth="1.6"
              strokeLinecap="round"
            />
          </>
        )}
        {kind === "offline" && (
          <>
            <path
              d="M12 3l7 3v5.5c0 4.2-2.8 7.8-7 9-4.2-1.2-7-4.8-7-9V6l7-3z"
              stroke={stroke}
              strokeWidth="1.8"
              strokeLinejoin="round"
            />
            <path
              d="M9.2 12.2l1.8 1.8 3.8-4"
              stroke={stroke}
              strokeWidth="1.8"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </>
        )}
        {kind === "seal" && (
          <>
            <circle cx="12" cy="12" r="8.5" stroke={stroke} strokeWidth="1.8" />
            <path
              d="M8.5 12.2l2.2 2.2 4.8-5"
              stroke={stroke}
              strokeWidth="1.8"
              strokeLinecap="round"
              strokeLinejoin="round"
            />
          </>
        )}
      </svg>
    </div>
  );
};

const FeatureCard: React.FC<{
  delay: number;
  title: string;
  subtitle: string;
  kind: "trainer" | "budget" | "offline" | "seal";
  portrait?: boolean;
}> = ({ delay, title, subtitle, kind, portrait = false }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();
  const progress = spring({ frame: frame - delay, fps, config: cardSpring });
  const y = interpolate(progress, [0, 1], [40, 0]);
  const scale = interpolate(progress, [0, 1], [0.96, 1]);

  return (
    <div
      style={{
        transform: `translateY(${y}px) scale(${scale})`,
        opacity: progress,
        backgroundColor: COLORS.card,
        padding: portrait ? "28px 26px 30px" : "22px 22px 24px",
        borderRadius: portrait ? 20 : 18,
        border: `1px solid rgba(232,169,42,0.28)`,
        width: portrait ? 330 : 280,
        fontFamily: FONT_STACK,
        boxShadow:
          "0 16px 40px -20px rgba(26,26,26,0.28), inset 0 1px 0 rgba(255,255,255,0.85)",
        position: "relative",
        overflow: "hidden",
      }}
    >
      <div
        style={{
          position: "absolute",
          inset: 0,
          background:
            "linear-gradient(180deg, rgba(255,255,255,0.7) 0%, rgba(255,255,255,0) 40%)",
          pointerEvents: "none",
        }}
      />
      <div style={{ position: "relative" }}>
        <FeatureIcon kind={kind} portrait={portrait} />
        <div
          style={{
            fontWeight: 650,
            fontSize: portrait ? 24 : 16.5,
            marginBottom: portrait ? 8 : 6,
            color: COLORS.ink,
            letterSpacing: portrait ? -0.3 : -0.2,
          }}
        >
          {title}
        </div>
        <div
          style={{
            fontSize: portrait ? 20 : 13.5,
            color: COLORS.sub,
            lineHeight: portrait ? 1.5 : 1.45,
          }}
        >
          {subtitle}
        </div>
      </div>
    </div>
  );
};

/** Mini donut for landscape phone summary */
const MiniDonut: React.FC<{ needPct: number; size?: number }> = ({
  needPct,
  size = 148,
}) => {
  const ring = 18;
  const r = (size - ring) / 2;
  const c = 2 * Math.PI * r;
  const needLen = (needPct / 100) * c;
  const wantLen = c - needLen;

  return (
    <div style={{ position: "relative", width: size, height: size, margin: "0 auto" }}>
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
        <circle
          cx={size / 2}
          cy={size / 2}
          r={r}
          fill="none"
          stroke={COLORS.divider}
          strokeWidth={ring}
        />
        <circle
          cx={size / 2}
          cy={size / 2}
          r={r}
          fill="none"
          stroke={COLORS.green}
          strokeWidth={ring}
          strokeLinecap="round"
          strokeDasharray={`${needLen} ${c - needLen}`}
          transform={`rotate(-90 ${size / 2} ${size / 2})`}
        />
        <circle
          cx={size / 2}
          cy={size / 2}
          r={r}
          fill="none"
          stroke={COLORS.crimson}
          strokeWidth={ring}
          strokeLinecap="round"
          strokeDasharray={`${wantLen} ${c - wantLen}`}
          strokeDashoffset={-needLen}
          transform={`rotate(-90 ${size / 2} ${size / 2})`}
        />
      </svg>
      <div
        style={{
          position: "absolute",
          inset: 0,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          justifyContent: "center",
          fontFamily: FONT_STACK,
        }}
      >
        <div
          style={{
            fontSize: 9,
            fontWeight: 700,
            letterSpacing: 1.2,
            color: COLORS.muted,
            textTransform: "uppercase",
          }}
        >
          Today
        </div>
        <div
          style={{
            fontSize: 18,
            fontWeight: 700,
            color: COLORS.ink,
            fontVariantNumeric: "tabular-nums",
            marginTop: 2,
          }}
        >
          ₱350
        </div>
      </div>
    </div>
  );
};

// ============================================
// Phone chrome
// ============================================

const PhoneChrome: React.FC<{
  children: React.ReactNode;
  float?: number;
}> = ({ children, float = 1 }) => {
  const frame = useCurrentFrame();
  const bob = Math.sin(frame * 0.045) * 4.5 * float;
  const tilt = Math.sin(frame * 0.03) * 0.4 * float;

  return (
    <div
      style={{
        width: 300,
        height: 580,
        transform: `translateY(${bob}px) rotate(${tilt}deg)`,
        borderRadius: 40,
        background: COLORS.phoneBezel,
        padding: 10,
        boxShadow: [
          "0 32px 70px -24px rgba(26,26,26,0.45)",
          "0 14px 32px -16px rgba(40,32,18,0.28)",
          "0 0 0 1px rgba(255,255,255,0.08) inset",
          "0 1px 0 0 rgba(255,255,255,0.1) inset",
        ].join(", "),
        position: "relative",
      }}
    >
      <div
        style={{
          width: "100%",
          height: "100%",
          backgroundColor: COLORS.background,
          borderRadius: 30,
          overflow: "hidden",
          position: "relative",
        }}
      >
        <div
          style={{
            height: 26,
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
            padding: "0 14px",
            fontFamily: FONT_STACK,
            fontSize: 10.5,
            fontWeight: 600,
            color: COLORS.ink,
          }}
        >
          <span>9:41</span>
          <div
            style={{
              width: 48,
              height: 11,
              borderRadius: 8,
              background: COLORS.ink,
              opacity: 0.88,
            }}
          />
          <span>100%</span>
        </div>
        <div style={{ padding: "4px 12px 14px", height: "calc(100% - 26px)" }}>{children}</div>
        <div
          style={{
            position: "absolute",
            inset: 0,
            pointerEvents: "none",
            background:
              "linear-gradient(115deg, rgba(255,255,255,0.12) 0%, rgba(255,255,255,0) 28%, rgba(255,255,255,0) 58%, rgba(255,255,255,0.04) 100%)",
          }}
        />
      </div>
    </div>
  );
};

const CaptionBeat: React.FC<{
  delay: number;
  title: React.ReactNode;
  body: string;
  portrait?: boolean;
}> = ({ delay, title, body, portrait = false }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();
  const progress = spring({ frame: frame - delay, fps, config: softSpring });

  return (
    <div
      style={{
        opacity: progress,
        transform: `translateY(${interpolate(progress, [0, 1], [20, 0])}px)`,
        textAlign: portrait ? "center" : undefined,
        width: portrait ? "100%" : undefined,
      }}
    >
      <div
        style={{
          marginBottom: portrait ? 14 : 10,
          display: portrait ? "flex" : undefined,
          justifyContent: portrait ? "center" : undefined,
        }}
      >
        <GoldHairline delay={delay} width={portrait ? 56 : 40} align={portrait ? "center" : "left"} />
      </div>
      <div
        style={{
          fontSize: portrait ? 46 : 26,
          fontWeight: 600,
          color: COLORS.ink,
          lineHeight: 1.28,
          letterSpacing: portrait ? -0.6 : -0.3,
          marginBottom: portrait ? 12 : 8,
          fontFamily: FONT_STACK,
        }}
      >
        {title}
      </div>
      <div
        style={{
          fontSize: portrait ? 26 : 15.5,
          color: COLORS.sub,
          lineHeight: 1.5,
          maxWidth: portrait ? 680 : 440,
          fontFamily: FONT_STACK,
          margin: portrait ? "0 auto" : undefined,
        }}
      >
        {body}
      </div>
    </div>
  );
};

/** Editorial paper wipe between major beats — horizontal sweep in 16:9, vertical in 9:16. */
const PaperWipe: React.FC = () => {
  const f = useCurrentFrame();
  const { width, height } = useVideoConfig();
  const portrait = height > width;
  const span = portrait ? height : width;
  const lead = interpolate(f, [0, 16], [-span, span], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const opacity = interpolate(f, [0, 3, 12, 16], [0, 1, 1, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  return (
    <div
      style={{
        position: "absolute",
        top: portrait ? lead : 0,
        left: portrait ? 0 : lead,
        width,
        height,
        opacity,
        zIndex: 80,
        pointerEvents: "none",
        background: `linear-gradient(98deg, #FBF9F3 0%, #F7F3E8 42%, #F0EAD8 100%)`,
        boxShadow: "0 0 80px rgba(40,32,18,0.12)",
      }}
    >
      {portrait ? (
        <div
          style={{
            position: "absolute",
            left: 0,
            right: 0,
            bottom: 0,
            height: 3,
            background: `linear-gradient(90deg, transparent 0%, ${COLORS.gold} 18%, ${COLORS.gold} 82%, transparent 100%)`,
            opacity: 0.85,
          }}
        />
      ) : (
        <div
          style={{
            position: "absolute",
            top: 0,
            bottom: 0,
            left: 0,
            width: 3,
            background: `linear-gradient(180deg, transparent 0%, ${COLORS.gold} 18%, ${COLORS.gold} 82%, transparent 100%)`,
            opacity: 0.85,
          }}
        />
      )}
      {Array.from({ length: 10 }).map((_, i) => (
        <div
          key={i}
          style={{
            position: "absolute",
            left: portrait ? 90 : 64,
            right: portrait ? 90 : 64,
            top: portrait ? 110 + i * 168 : 90 + i * 96,
            height: 1,
            background: "rgba(214,210,198,0.5)",
          }}
        />
      ))}
    </div>
  );
};

// ============================================
// SCENES
// ============================================

/** 0–4s — Scroll-stop problem */
const SceneHook: React.FC<{ portrait?: boolean }> = ({ portrait = false }) => {
  const frame = useCurrentFrame();
  const word1 = spring({ frame: frame - 8, fps: 30, config: softSpring });
  const word2 = spring({ frame: frame - 26, fps: 30, config: softSpring });
  const sub = spring({ frame: frame - 52, fps: 30, config: calmSpring });

  return (
    <SceneFade durationInFrames={DUR_HOOK} inEnd={8} outStartFromEnd={14}>
      <AbsoluteFill
        style={{
          justifyContent: "center",
          alignItems: "center",
          fontFamily: FONT_STACK,
        }}
      >
        <GoldDust
          frame={frame}
          count={16}
          area={
            portrait
              ? { x: 60, y: 300, w: 960, h: 1300 }
              : { x: 360, y: 160, w: 1200, h: 720 }
          }
          opacity={0.55}
          scale={portrait ? 1.7 : 1}
        />
        <div style={{ textAlign: "center", maxWidth: portrait ? 920 : 960, padding: "0 48px" }}>
          <div style={{ marginBottom: portrait ? 28 : 20 }}>
            <GoldHairline delay={4} width={portrait ? 96 : 64} />
          </div>
          <div
            style={{
              fontSize: portrait ? 84 : 54,
              fontWeight: 600,
              color: COLORS.ink,
              letterSpacing: portrait ? -1.4 : -0.9,
              lineHeight: 1.18,
            }}
          >
            <div
              style={{
                opacity: word1,
                transform: `translateY(${interpolate(word1, [0, 1], [22, 0])}px)`,
              }}
            >
              Most people decide
            </div>
            <div
              style={{
                opacity: word2,
                transform: `translateY(${interpolate(word2, [0, 1], [22, 0])}px)`,
                marginTop: portrait ? 12 : 8,
              }}
            >
              <span style={{ color: COLORS.sub }}>after</span>{" "}
              <span
                style={{
                  fontFamily: SERIF_STACK,
                  fontWeight: 700,
                  color: COLORS.crimson,
                  letterSpacing: 0.5,
                }}
              >
                they spend.
              </span>
            </div>
          </div>
          <div
            style={{
              marginTop: portrait ? 40 : 28,
              opacity: sub,
              transform: `translateY(${interpolate(sub, [0, 1], [12, 0])}px)`,
              fontSize: portrait ? 26 : 16,
              color: COLORS.muted,
              letterSpacing: portrait ? 4 : 2.6,
              textTransform: "uppercase",
              fontWeight: 600,
            }}
          >
            Train the moment of choice
          </div>
        </div>
      </AbsoluteFill>
    </SceneFade>
  );
};

/** 4–9s — What the app is for */
const ScenePurpose: React.FC<{ portrait?: boolean }> = ({ portrait = false }) => {
  const frame = useCurrentFrame();
  const needIn = spring({ frame: frame - 36, fps: 30, config: sealSpring });
  const wantIn = spring({ frame: frame - 52, fps: 30, config: sealSpring });
  const vsPulse = spring({ frame: frame - 44, fps: 30, config: calmSpring });

  return (
    <SceneFade durationInFrames={DUR_PURPOSE} inEnd={12} outStartFromEnd={16}>
      <AbsoluteFill
        style={{
          justifyContent: "center",
          alignItems: "center",
          fontFamily: FONT_STACK,
          flexDirection: "column",
          gap: portrait ? 34 : 28,
          padding: portrait ? "48px 44px" : undefined,
        }}
      >
        <GoldDust
          frame={frame}
          count={12}
          area={
            portrait
              ? { x: 120, y: 340, w: 840, h: 1150 }
              : { x: 400, y: 140, w: 1120, h: 600 }
          }
          opacity={0.4}
          scale={portrait ? 1.7 : 1}
        />
        <SoftText delay={6} rise={16}>
          <div
            style={{
              fontSize: portrait ? 24 : 14,
              fontWeight: 700,
              letterSpacing: portrait ? 4 : 2.8,
              textTransform: "uppercase",
              color: COLORS.goldDeep,
              textAlign: "center",
            }}
          >
            Not a ledger · A trainer
          </div>
        </SoftText>
        <SoftText delay={16} rise={20}>
          <div
            style={{
              fontSize: portrait ? 66 : 42,
              fontWeight: 550,
              color: COLORS.ink,
              textAlign: "center",
              lineHeight: 1.25,
              letterSpacing: portrait ? -1 : -0.5,
              maxWidth: portrait ? 920 : 880,
            }}
          >
            Every purchase forces one choice.
            <br />
            <span style={{ color: COLORS.sub, fontWeight: 500 }}>
              Need or Want — before the receipt cools.
            </span>
          </div>
        </SoftText>

        <div
          style={{
            display: "flex",
            alignItems: "center",
            gap: portrait ? 36 : 28,
            marginTop: portrait ? 14 : 8,
          }}
        >
          <div
            style={{
              opacity: needIn,
              transform: `scale(${interpolate(needIn, [0, 1], [0.86, 1])}) translateY(${interpolate(needIn, [0, 1], [16, 0])}px)`,
            }}
          >
            <TypeChip type="NEED" size="lg" portrait={portrait} />
          </div>
          <div
            style={{
              opacity: vsPulse,
              fontFamily: SERIF_STACK,
              fontSize: portrait ? 28 : 18,
              fontWeight: 700,
              color: COLORS.muted,
              letterSpacing: 2,
            }}
          >
            vs
          </div>
          <div
            style={{
              opacity: wantIn,
              transform: `scale(${interpolate(wantIn, [0, 1], [0.86, 1])}) translateY(${interpolate(wantIn, [0, 1], [16, 0])}px)`,
            }}
          >
            <TypeChip type="WANT" size="lg" portrait={portrait} />
          </div>
        </div>

        <SoftText delay={72}>
          <div
            style={{
              fontSize: portrait ? 24 : 16,
              color: COLORS.sub,
              textAlign: "center",
              maxWidth: portrait ? 760 : 520,
              lineHeight: 1.5,
            }}
          >
            Confront impulse in real time — not at month-end when the money is already gone.
          </div>
        </SoftText>
      </AbsoluteFill>
    </SceneFade>
  );
};

/** 9–15s — How 1: Summary donut */
const SceneSee: React.FC<{ portrait?: boolean }> = ({ portrait = false }) => {
  const frame = useCurrentFrame();
  const phoneIn = spring({ frame: frame - 4, fps: 30, config: calmSpring });
  // 49% ≈ ₱170 of ₱350 — arc must match the NEEDS/WANTS numbers below.
  const needPct = interpolate(frame, [20, 100], [0, 49], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const ctaPulse =
    0.92 +
    Math.sin(Math.max(0, frame - 70) * 0.14) *
      0.04 *
      interpolate(frame, [70, 90], [0, 1], {
        extrapolateLeft: "clamp",
        extrapolateRight: "clamp",
      });

  return (
    <SceneFade durationInFrames={DUR_SEE} inEnd={12} outStartFromEnd={16}>
      <AbsoluteFill
        style={{
          justifyContent: "center",
          alignItems: "center",
          flexDirection: portrait ? "column" : "row",
          gap: portrait ? 44 : 80,
          padding: portrait ? "56px 44px 72px" : "0 90px",
          fontFamily: FONT_STACK,
        }}
      >
        <div
          style={{
            opacity: phoneIn,
            transform: `translateX(${interpolate(phoneIn, [0, 1], [-40, 0])}px) scale(${portrait ? 1.28 : 1})`,
          }}
        >
          <PhoneChrome float={0.85}>
            <div
              style={{
                fontSize: 10,
                fontWeight: 700,
                letterSpacing: 1.4,
                color: COLORS.crimson,
                textTransform: "uppercase",
                marginBottom: 4,
              }}
            >
              A 35-day trainer
            </div>
            <div
              style={{
                fontFamily: SERIF_STACK,
                fontSize: 20,
                fontWeight: 700,
                color: COLORS.ink,
                letterSpacing: 0.6,
                lineHeight: 1.1,
                marginBottom: 10,
              }}
            >
              NEEDS
              <br />
              vs WANTS
            </div>
            <div
              style={{
                width: 36,
                height: 2,
                background: COLORS.gold,
                borderRadius: 2,
                marginBottom: 14,
              }}
            />
            <MiniDonut needPct={needPct} size={150} />
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                marginTop: 16,
                padding: "0 6px",
              }}
            >
              <div>
                <div style={{ fontSize: 10, color: COLORS.green, fontWeight: 700 }}>NEEDS</div>
                <div
                  style={{
                    fontSize: 15,
                    fontWeight: 700,
                    color: COLORS.ink,
                    fontVariantNumeric: "tabular-nums",
                  }}
                >
                  ₱170
                </div>
              </div>
              <div style={{ textAlign: "right" }}>
                <div style={{ fontSize: 10, color: COLORS.crimson, fontWeight: 700 }}>WANTS</div>
                <div
                  style={{
                    fontSize: 15,
                    fontWeight: 700,
                    color: COLORS.ink,
                    fontVariantNumeric: "tabular-nums",
                  }}
                >
                  ₱180
                </div>
              </div>
            </div>
            <div
              style={{
                marginTop: 18,
                background: COLORS.crimson,
                color: COLORS.card,
                textAlign: "center",
                padding: "11px 0",
                borderRadius: 999,
                fontSize: 13,
                fontWeight: 650,
                transform: `scale(${ctaPulse})`,
                boxShadow: "0 8px 20px -8px rgba(200,16,46,0.55)",
              }}
            >
              Log a Purchase
            </div>
          </PhoneChrome>
        </div>

        <div
          style={{
            maxWidth: portrait ? 660 : 480,
            width: portrait ? "100%" : undefined,
            display: portrait ? "flex" : undefined,
            flexDirection: portrait ? "column" : undefined,
            alignItems: portrait ? "center" : undefined,
          }}
        >
          <StepBadge step="01" label="See" delay={10} portrait={portrait} />
          <CaptionBeat
            delay={18}
            portrait={portrait}
            title={
              <>
                Open Summary.
                <br />
                See the{" "}
                <span style={{ color: COLORS.green }}>split</span>.
              </>
            }
            body="One ring. Day, Week, or 35 days — the split stays visible, never buried in rows."
          />
          <div style={{ height: portrait ? 36 : 28 }} />
          <CaptionBeat
            delay={70}
            portrait={portrait}
            title={<>Tap “Log a Purchase.”</>}
            body="One CTA. Straight into the trainer — no menus, no accounts."
          />
        </div>
      </AbsoluteFill>
    </SceneFade>
  );
};

/** 15–28s — How 2: Log, classify, seal */
const SceneSeal: React.FC<{ portrait?: boolean }> = ({ portrait = false }) => {
  const frame = useCurrentFrame();
  const phoneIn = spring({ frame: frame - 2, fps: 30, config: calmSpring });

  // Typing Coffee / 180
  const itemChars = Math.max(0, Math.min(6, Math.ceil((frame - 28) / 3.2)));
  const costChars = Math.max(0, Math.min(3, Math.ceil((frame - 72) / 4)));
  const itemValue = frame < 118 ? "Coffee".slice(0, itemChars) : "";
  const costValue = frame < 118 ? "180".slice(0, costChars) : "";
  const itemFocused = frame >= 24 && frame < 68;
  const costFocused = frame >= 70 && frame < 100;
  const wantSelected = frame >= 108 && frame < 125;
  const sealedCoffee = frame >= 118;
  const sealedBus = frame >= 200;
  const sealedRice = frame >= 270;

  const spentCents = sealedRice ? 35000 : sealedBus ? 22500 : sealedCoffee ? 18000 : 0;
  const spentLabel = `₱${(spentCents / 100).toLocaleString("en-PH", {
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
  })}`;
  const dayFillPct = Math.min(100, (spentCents / 150000) * 100);

  const fieldBorder = (focused: boolean) =>
    focused ? `1.5px solid ${COLORS.gold}` : `1px solid ${COLORS.divider}`;

  return (
    <SceneFade durationInFrames={DUR_SEAL} inEnd={12} outStartFromEnd={18}>
      <AbsoluteFill
        style={{
          justifyContent: "center",
          alignItems: "center",
          flexDirection: portrait ? "column" : "row",
          gap: portrait ? 40 : 80,
          padding: portrait ? "56px 44px 60px" : "0 90px",
          fontFamily: FONT_STACK,
        }}
      >
        <div
          style={{
            opacity: phoneIn,
            transform: `translateX(${interpolate(phoneIn, [0, 1], [-36, 0])}px) scale(${portrait ? 1.28 : 1})`,
          }}
        >
          <PhoneChrome float={0.9}>
            <div
              style={{
                fontSize: 10,
                fontWeight: 700,
                color: COLORS.muted,
                letterSpacing: 1.5,
                textTransform: "uppercase",
                marginBottom: 2,
              }}
            >
              Today · Log
            </div>
            <div
              style={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "baseline",
                marginBottom: 10,
              }}
            >
              <div
                style={{
                  fontSize: 28,
                  fontWeight: 700,
                  color: COLORS.ink,
                  letterSpacing: 1,
                }}
              >
                LOG
              </div>
              <div style={{ fontSize: 12, color: COLORS.sub, fontWeight: 600 }}>
                {sealedRice ? "3" : sealedBus ? "2" : sealedCoffee ? "1" : "0"} / 20
              </div>
            </div>

            {/* day budget mini */}
            <div
              style={{
                marginBottom: 10,
                padding: "8px 10px",
                borderRadius: 10,
                background: COLORS.raised,
                border: `1px solid ${COLORS.divider}`,
              }}
            >
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  fontSize: 10,
                  color: COLORS.sub,
                  marginBottom: 5,
                  fontWeight: 600,
                }}
              >
                <span>Day budget</span>
                <span style={{ fontVariantNumeric: "tabular-nums" }}>
                  {spentLabel} / ₱1,500
                </span>
              </div>
              <div
                style={{
                  height: 5,
                  borderRadius: 999,
                  background: COLORS.divider,
                  overflow: "hidden",
                }}
              >
                <div
                  style={{
                    width: `${dayFillPct}%`,
                    height: "100%",
                    borderRadius: 999,
                    background: `linear-gradient(90deg, ${COLORS.green}, ${COLORS.greenSoft})`,
                  }}
                />
              </div>
            </div>

            {/* entry form — only while typing the first seal */}
            {!sealedCoffee && (
              <div
                style={{
                  background: COLORS.card,
                  borderRadius: 12,
                  border: `1px solid ${COLORS.divider}`,
                  padding: "10px 12px 12px",
                  marginBottom: 10,
                }}
              >
                <div
                  style={{
                    fontSize: 9,
                    fontWeight: 700,
                    color: COLORS.muted,
                    letterSpacing: 1,
                    marginBottom: 4,
                    textTransform: "uppercase",
                  }}
                >
                  Item
                </div>
                <div
                  style={{
                    fontSize: 14,
                    fontWeight: 600,
                    color: COLORS.ink,
                    minHeight: 22,
                    borderBottom: fieldBorder(itemFocused),
                    paddingBottom: 4,
                    marginBottom: 10,
                  }}
                >
                  {itemValue}
                  {itemFocused ? (
                    <span style={{ opacity: frame % 16 < 8 ? 1 : 0, color: COLORS.gold }}>|</span>
                  ) : null}
                </div>
                <div style={{ display: "flex", alignItems: "flex-end", gap: 8 }}>
                  <div style={{ flex: 1 }}>
                    <div
                      style={{
                        fontSize: 9,
                        fontWeight: 700,
                        color: COLORS.muted,
                        letterSpacing: 1,
                        marginBottom: 4,
                        textTransform: "uppercase",
                      }}
                    >
                      Cost
                    </div>
                    <div
                      style={{
                        fontSize: 14,
                        fontWeight: 600,
                        color: COLORS.ink,
                        minHeight: 22,
                        borderBottom: fieldBorder(costFocused),
                        paddingBottom: 4,
                        fontVariantNumeric: "tabular-nums",
                      }}
                    >
                      {costValue ? `₱${costValue}` : ""}
                      {costFocused ? (
                        <span style={{ opacity: frame % 16 < 8 ? 1 : 0, color: COLORS.gold }}>
                          |
                        </span>
                      ) : null}
                    </div>
                  </div>
                  <TypeChip type="NEED" selected={false} size="sm" opacity={0.7} />
                  <TypeChip type="WANT" selected={wantSelected || sealedCoffee} size="sm" />
                </div>
              </div>
            )}

            {sealedCoffee && (
              <SealingRow
                delay={0}
                item="Coffee"
                amount="₱180.00"
                type="WANT"
                note="just now"
              />
            )}
            {sealedBus && (
              <SealingRow
                delay={0}
                item="Bus fare"
                amount="₱45.00"
                type="NEED"
                note="just now"
              />
            )}
            {sealedRice && (
              <SealingRow
                delay={0}
                item="Rice & eggs"
                amount="₱125.00"
                type="NEED"
                note="just now"
              />
            )}

            <div
              style={{
                marginTop: 6,
                fontSize: 10.5,
                color: COLORS.muted,
                textAlign: "center",
                letterSpacing: 0.2,
                opacity: interpolate(frame, [130, 155], [0, 1], {
                  extrapolateLeft: "clamp",
                  extrapolateRight: "clamp",
                }),
              }}
            >
              No save button · sealed on classify
            </div>
          </PhoneChrome>
        </div>

        <div
          style={{
            display: "flex",
            flexDirection: "column",
            gap: portrait ? 30 : 26,
            maxWidth: portrait ? 660 : 480,
            width: portrait ? "100%" : undefined,
            alignItems: portrait ? "center" : undefined,
          }}
        >
          <StepBadge step="02" label="Seal" delay={8} portrait={portrait} />
          <CaptionBeat
            delay={20}
            portrait={portrait}
            title={
              <>
                Type the purchase.
                <br />
                Choose{" "}
                <span style={{ color: COLORS.green }}>Need</span> or{" "}
                <span style={{ color: COLORS.crimson }}>Want</span>.
              </>
            }
            body="Item + cost + type. The decision is the action — no separate save step."
          />
          <CaptionBeat
            delay={110}
            portrait={portrait}
            title={
              <>
                It{" "}
                <span style={{ fontFamily: SERIF_STACK, color: COLORS.green }}>seals</span> in
                the moment.
              </>
            }
            body="Ink dries. Time stamps. The impulse gets a name before it becomes a habit."
          />
          <CaptionBeat
            delay={220}
            portrait={portrait}
            title={<>Stack the day.</>}
            body="Each row is a conscious call. Repeat until the split feels honest."
          />
        </div>
      </AbsoluteFill>
    </SceneFade>
  );
};

/** 28–34s — How 3: Daily budget guardrail */
const SceneGuard: React.FC<{ portrait?: boolean }> = ({ portrait = false }) => {
  const frame = useCurrentFrame();
  const phoneIn = spring({ frame: frame - 4, fps: 30, config: calmSpring });
  const fillPct = interpolate(frame, [10, 90], [0.42, 1.08], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const over = fillPct > 1;
  const dialogIn = spring({ frame: frame - 72, fps: 30, config: sealSpring });
  const fillColor = over ? COLORS.crimson : fillPct > 0.78 ? COLORS.goldDeep : COLORS.green;

  return (
    <SceneFade durationInFrames={DUR_GUARD} inEnd={12} outStartFromEnd={16}>
      <AbsoluteFill
        style={{
          justifyContent: "center",
          alignItems: "center",
          flexDirection: portrait ? "column" : "row",
          gap: portrait ? 44 : 80,
          padding: portrait ? "56px 44px 64px" : "0 90px",
          fontFamily: FONT_STACK,
        }}
      >
        <div
          style={{
            opacity: phoneIn,
            transform: `translateX(${interpolate(phoneIn, [0, 1], [-36, 0])}px) scale(${portrait ? 1.28 : 1})`,
          }}
        >
          <PhoneChrome float={0.8}>
            <div
              style={{
                fontSize: 10,
                fontWeight: 700,
                color: COLORS.muted,
                letterSpacing: 1.5,
                textTransform: "uppercase",
                marginBottom: 6,
              }}
            >
              Log · Daily budget
            </div>
            <div
              style={{
                background: COLORS.card,
                borderRadius: 14,
                border: `1px solid ${over ? "rgba(200,16,46,0.45)" : COLORS.divider}`,
                padding: "12px 14px 14px",
                marginBottom: 10,
              }}
            >
              <div
                style={{
                  fontSize: 10,
                  fontWeight: 700,
                  letterSpacing: 1.2,
                  color: over ? COLORS.crimson : COLORS.goldDeep,
                  textTransform: "uppercase",
                  marginBottom: 6,
                }}
              >
                Daily budget
              </div>
              <div
                style={{
                  display: "flex",
                  justifyContent: "space-between",
                  alignItems: "baseline",
                  marginBottom: 8,
                }}
              >
                <span
                  style={{
                    fontSize: 20,
                    fontWeight: 700,
                    color: over ? COLORS.crimson : COLORS.ink,
                    fontVariantNumeric: "tabular-nums",
                  }}
                >
                  ₱{over ? "1,620" : Math.round(1500 * Math.min(1, fillPct)).toLocaleString()}
                </span>
                <span style={{ fontSize: 12, color: COLORS.muted }}>of ₱1,500</span>
              </div>
              <div
                style={{
                  height: 7,
                  borderRadius: 999,
                  background: COLORS.raised,
                  overflow: "hidden",
                }}
              >
                <div
                  style={{
                    width: `${Math.min(1, fillPct) * 100}%`,
                    height: "100%",
                    background: fillColor,
                    borderRadius: 999,
                  }}
                />
              </div>
              <div
                style={{
                  marginTop: 8,
                  fontSize: 12,
                  color: over ? COLORS.crimson : COLORS.sub,
                  fontWeight: over ? 600 : 400,
                }}
              >
                {over ? "Over by ₱120" : "Remaining today"}
              </div>
            </div>

            <div
              style={{
                opacity: interpolate(frame, [20, 40], [0, 1], {
                  extrapolateLeft: "clamp",
                  extrapolateRight: "clamp",
                }),
                marginBottom: 10,
              }}
            >
              <SealingRow delay={0} item="Snack pack" amount="₱120.00" type="WANT" note="just now" />
            </div>

            {/* Log anyway dialog */}
            <div
              style={{
                opacity: dialogIn,
                transform: `scale(${interpolate(dialogIn, [0, 1], [0.94, 1])}) translateY(${interpolate(dialogIn, [0, 1], [12, 0])}px)`,
                background: COLORS.card,
                borderRadius: 16,
                border: `1px solid ${COLORS.dividerStrong}`,
                padding: "16px 16px 14px",
                boxShadow: "0 16px 40px -16px rgba(26,26,26,0.35)",
              }}
            >
              <div
                style={{
                  fontSize: 15,
                  fontWeight: 650,
                  color: COLORS.ink,
                  marginBottom: 6,
                }}
              >
                Log anyway?
              </div>
              <div style={{ fontSize: 12.5, color: COLORS.sub, lineHeight: 1.45, marginBottom: 14 }}>
                Coffee · ₱180
                <br />
                This puts you{" "}
                <span style={{ color: COLORS.crimson, fontWeight: 600 }}>over by ₱120</span>.
              </div>
              <div style={{ display: "flex", gap: 8 }}>
                <div
                  style={{
                    flex: 1,
                    textAlign: "center",
                    padding: "9px 0",
                    borderRadius: 999,
                    border: `1px solid ${COLORS.divider}`,
                    fontSize: 12,
                    fontWeight: 600,
                    color: COLORS.sub,
                  }}
                >
                  Cancel
                </div>
                <div
                  style={{
                    flex: 1,
                    textAlign: "center",
                    padding: "9px 0",
                    borderRadius: 999,
                    background: COLORS.crimson,
                    fontSize: 12,
                    fontWeight: 650,
                    color: COLORS.card,
                  }}
                >
                  Log
                </div>
              </div>
            </div>
          </PhoneChrome>
        </div>

        <div
          style={{
            maxWidth: portrait ? 660 : 480,
            width: portrait ? "100%" : undefined,
            display: portrait ? "flex" : undefined,
            flexDirection: portrait ? "column" : undefined,
            alignItems: portrait ? "center" : undefined,
          }}
        >
          <StepBadge step="03" label="Guard" delay={8} portrait={portrait} />
          <CaptionBeat
            delay={16}
            portrait={portrait}
            title={
              <>
                Optional daily{" "}
                <span style={{ color: COLORS.goldDeep }}>budget</span>.
              </>
            }
            body="Set a quiet limit on the Log. Watch the meter fill as you seal purchases."
          />
          <div style={{ height: portrait ? 34 : 24 }} />
          <CaptionBeat
            delay={70}
            portrait={portrait}
            title={
              <>
                Overspend still allowed — with a{" "}
                <span style={{ color: COLORS.crimson }}>clear confirm</span>.
              </>
            }
            body="“Log anyway?” shows item, cost, and how far over. You choose. It never nags — it just makes the cost visible."
          />
        </div>
      </AbsoluteFill>
    </SceneFade>
  );
};

/** 34–40s — Benefits */
const SceneBenefits: React.FC<{ portrait?: boolean }> = ({ portrait = false }) => {
  const frame = useCurrentFrame();
  const head = spring({ frame: frame - 4, fps: 30, config: calmSpring });

  return (
    <SceneFade durationInFrames={DUR_BENEFITS} inEnd={12} outStartFromEnd={16}>
      <AbsoluteFill
        style={{
          justifyContent: "center",
          alignItems: "center",
          gap: portrait ? 28 : 32,
          fontFamily: FONT_STACK,
          padding: portrait ? "48px 40px" : undefined,
        }}
      >
        <GoldDust
          frame={frame}
          count={10}
          area={
            portrait
              ? { x: 140, y: 80, w: 800, h: 680 }
              : { x: 200, y: 60, w: 1520, h: 300 }
          }
          opacity={0.35}
        />
        <div
          style={{
            textAlign: "center",
            opacity: head,
            transform: `translateY(${interpolate(head, [0, 1], [14, 0])}px)`,
          }}
        >
          <div
            style={{
              fontSize: portrait ? 20 : 13,
              fontWeight: 700,
              letterSpacing: portrait ? 4 : 2.8,
              textTransform: "uppercase",
              color: COLORS.goldDeep,
              marginBottom: portrait ? 14 : 10,
            }}
          >
            Built for the habit
          </div>
          <div
            style={{
              fontFamily: SERIF_STACK,
              fontSize: portrait ? 48 : 34,
              fontWeight: 700,
              color: COLORS.ink,
              letterSpacing: portrait ? 1.4 : 1,
              lineHeight: 1.15,
            }}
          >
            Why people keep opening it
          </div>
          <div
            style={{
              display: "flex",
              justifyContent: "center",
              marginTop: portrait ? 20 : 16,
            }}
          >
            <GoldHairline delay={8} width={portrait ? 88 : 72} />
          </div>
        </div>

        <div
          style={{
            display: "flex",
            gap: 18,
            flexWrap: portrait ? "wrap" : "nowrap",
            justifyContent: "center",
            maxWidth: portrait ? 680 : undefined,
          }}
        >
          <FeatureCard
            delay={14}
            portrait={portrait}
            kind="seal"
            title="Seals on classify"
            subtitle="No save button. Need or Want is the action — ink dries in the moment."
          />
          <FeatureCard
            delay={26}
            portrait={portrait}
            kind="budget"
            title="Optional daily budget"
            subtitle="A quiet guardrail on the Log. Overspend still possible — with a clear confirm."
          />
          <FeatureCard
            delay={38}
            portrait={portrait}
            kind="trainer"
            title="35-day trainer window"
            subtitle="Not a permanent archive. Lessons stay sharp — old rows fade on purpose."
          />
          <FeatureCard
            delay={50}
            portrait={portrait}
            kind="offline"
            title="Fully offline"
            subtitle="No accounts required to train. Your money decisions stay on your phone."
          />
        </div>
      </AbsoluteFill>
    </SceneFade>
  );
};

/** 40–45s — Brand lockup + CTA */
const SceneClosing: React.FC<{ portrait?: boolean }> = ({ portrait = false }) => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();
  const progress = spring({ frame, fps, config: calmSpring });
  const cta = spring({ frame: frame - 28, fps, config: sealSpring });
  const meta = spring({ frame: frame - 48, fps, config: softSpring });
  const pulse =
    1 +
    Math.sin(Math.max(0, frame - 40) * 0.12) *
      0.014 *
      interpolate(frame, [40, 60], [0, 1], {
        extrapolateLeft: "clamp",
        extrapolateRight: "clamp",
      });

  return (
    <SceneFade durationInFrames={DUR_CLOSE} inEnd={10} outStartFromEnd={0}>
      <AbsoluteFill
        style={{
          justifyContent: "center",
          alignItems: "center",
          fontFamily: FONT_STACK,
          padding: portrait ? "40px 44px" : undefined,
        }}
      >
        <GoldDust
          frame={frame}
          count={20}
          area={
            portrait
              ? { x: 120, y: 240, w: 840, h: 1500 }
              : { x: 320, y: 100, w: 1280, h: 880 }
          }
          opacity={0.65}
          scale={portrait ? 1.7 : 1}
        />
        <div
          style={{
            opacity: progress,
            transform: `translateY(${interpolate(progress, [0, 1], [22, 0])}px)`,
            textAlign: "center",
          }}
        >
          <div style={{ marginBottom: portrait ? 22 : 16, display: "flex", justifyContent: "center" }}>
            <GoldHairline delay={4} width={portrait ? 104 : 72} />
          </div>
          <div
            style={{
              fontSize: portrait ? 84 : 54,
              fontWeight: 700,
              color: COLORS.ink,
              letterSpacing: portrait ? 2 : 1.5,
              marginBottom: portrait ? 16 : 12,
              fontFamily: SERIF_STACK,
              lineHeight: 1.12,
            }}
          >
            Needs vs Wants
          </div>
          <div
            style={{
              fontSize: portrait ? 30 : 19,
              color: COLORS.sub,
              marginBottom: portrait ? 14 : 10,
              fontWeight: 450,
            }}
          >
            The offline-first expense trainer
          </div>
          <div
            style={{
              fontSize: portrait ? 24 : 15,
              color: COLORS.muted,
              marginBottom: portrait ? 40 : 28,
              fontWeight: 500,
              letterSpacing: portrait ? 0.6 : undefined,
            }}
          >
            Classify · Seal · Train the habit
          </div>

          <div
            style={{
              display: "inline-flex",
              position: "relative",
              overflow: "hidden",
              opacity: cta,
              transform: `scale(${interpolate(cta, [0, 1], [0.92, 1]) * pulse})`,
              background: COLORS.crimson,
              color: COLORS.card,
              fontWeight: 650,
              fontSize: portrait ? 26 : 16,
              letterSpacing: 0.4,
              padding: portrait ? "20px 52px" : "14px 34px",
              borderRadius: 999,
              boxShadow: "0 14px 36px -10px rgba(200,16,46,0.55)",
              marginBottom: portrait ? 36 : 26,
            }}
          >
            Get the app
            {/* one slow shine sweep across the pill */}
            <div
              style={{
                position: "absolute",
                top: 0,
                bottom: 0,
                width: 80,
                background:
                  "linear-gradient(105deg, transparent 0%, rgba(255,255,255,0.5) 50%, transparent 100%)",
                transform: `translateX(${interpolate(frame, [36, 100], [-220, 460], {
                  extrapolateLeft: "clamp",
                  extrapolateRight: "clamp",
                })}px)`,
                pointerEvents: "none",
              }}
            />
          </div>

          <div
            style={{
              opacity: meta,
              transform: `translateY(${interpolate(meta, [0, 1], [10, 0])}px)`,
            }}
          >
            <div
              style={{
                fontSize: portrait ? 26 : 16,
                color: COLORS.muted,
                letterSpacing: 0.4,
                fontWeight: 500,
              }}
            >
              needs-vs-wants.vercel.app
            </div>
            <div
              style={{
                fontSize: portrait ? 20 : 13,
                color: COLORS.muted,
                marginTop: portrait ? 18 : 12,
                display: "flex",
                gap: portrait ? 18 : 14,
                justifyContent: "center",
                alignItems: "center",
              }}
            >
              <span
                style={{
                  padding: portrait ? "8px 20px" : "4px 12px",
                  borderRadius: 999,
                  border: `1px solid ${COLORS.dividerStrong}`,
                  background: COLORS.raised,
                  fontWeight: 600,
                  fontSize: portrait ? 18 : 12,
                  color: COLORS.sub,
                }}
              >
                Android available
              </span>
              <span
                style={{
                  padding: portrait ? "8px 20px" : "4px 12px",
                  borderRadius: 999,
                  border: `1px solid ${COLORS.divider}`,
                  fontWeight: 500,
                  fontSize: portrait ? 18 : 12,
                  color: COLORS.muted,
                }}
              >
                iOS coming soon
              </span>
            </div>
          </div>
        </div>
      </AbsoluteFill>
    </SceneFade>
  );
};

// ============================================
// Main composition
// ============================================

export const NeedsVsWantsPromo: React.FC<{ portrait?: boolean }> = ({
  portrait = false,
}) => {
  const t0 = 0;
  const t1 = DUR_HOOK;
  const t2 = t1 + DUR_PURPOSE;
  const t3 = t2 + DUR_SEE;
  const t4 = t3 + DUR_SEAL;
  const t5 = t4 + DUR_GUARD;
  const t6 = t5 + DUR_BENEFITS;

  return (
    <AbsoluteFill style={{ backgroundColor: COLORS.background }}>
      <PaperBackground />

      {/* 0–4s Hook */}
      <Sequence from={t0} durationInFrames={DUR_HOOK}>
        <SceneHook portrait={portrait} />
      </Sequence>

      {/* 4–9s Purpose */}
      <Sequence from={t1} durationInFrames={DUR_PURPOSE}>
        <ScenePurpose portrait={portrait} />
      </Sequence>

      {/* 9–15s See (Summary) */}
      <Sequence from={t2} durationInFrames={DUR_SEE}>
        <SceneSee portrait={portrait} />
      </Sequence>

      {/* paper wipe into how-to seal */}
      <Sequence from={t3 - 8} durationInFrames={18}>
        <PaperWipe />
      </Sequence>

      {/* 15–28s Seal (Log) */}
      <Sequence from={t3} durationInFrames={DUR_SEAL}>
        <SceneSeal portrait={portrait} />
      </Sequence>

      {/* 28–34s Guard (Budget) */}
      <Sequence from={t4} durationInFrames={DUR_GUARD}>
        <SceneGuard portrait={portrait} />
      </Sequence>

      {/* 34–40s Benefits */}
      <Sequence from={t5} durationInFrames={DUR_BENEFITS}>
        <SceneBenefits portrait={portrait} />
      </Sequence>

      {/* 40–45s Close */}
      <Sequence from={t6} durationInFrames={DUR_CLOSE}>
        <SceneClosing portrait={portrait} />
      </Sequence>

      <PromoAudioBed />
    </AbsoluteFill>
  );
};
