import React from "react";
import { AbsoluteFill, Sequence, useCurrentFrame, interpolate } from "remotion";
import { PaperBackground } from "./fx/PaperBackground";
import { GoldDust } from "./fx/GoldDust";
import { SceneHook, hookDurationFrames } from "./scenes/SceneHook";
import { SceneSummary, summaryDurationFrames } from "./scenes/SceneSummary";
import { SceneLog, logDurationFrames } from "./scenes/SceneLog";
import { SceneBudget, budgetDurationFrames } from "./scenes/SceneBudget";
import { SceneClose, closeDurationFrames } from "./scenes/SceneClose";
import { AudioBed } from "./audio/AudioBed";
import { EASE_EDIT, COLORS } from "./theme";

/**
 * Editorial paper/ledger wipe at the S3 → S4 boundary.
 * A warm sheet with gold hairline + faint ledger rules sweeps across —
 * boutique ledger motif, not a hard digital cut.
 */
const PaperWipe: React.FC = () => {
  const f = useCurrentFrame();
  const x = interpolate(f, [0, 16], [-1520, 1520], {
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
        top: 0,
        left: x,
        width: 1680,
        height: 2560,
        opacity,
        zIndex: 60,
        pointerEvents: "none",
        background: `linear-gradient(98deg, #FBF9F3 0%, #F7F3E8 42%, #F0EAD8 100%)`,
        boxShadow: "0 0 80px rgba(40,32,18,0.12)",
      }}
    >
      {/* gold leading edge hairline */}
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
      {/* faint ledger horizontal rules */}
      {Array.from({ length: 14 }).map((_, i) => (
        <div
          key={i}
          style={{
            position: "absolute",
            left: 48,
            right: 48,
            top: 180 + i * 160,
            height: 1,
            background: "rgba(214,210,198,0.55)",
          }}
        />
      ))}
      {/* soft paper grain on the wipe sheet */}
      <div
        style={{
          position: "absolute",
          inset: 0,
          opacity: 0.06,
          mixBlendMode: "multiply",
          backgroundImage:
            "url(\"data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='120' height='120'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.85' numOctaves='2' stitchTiles='stitch'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)' opacity='0.5'/%3E%3C/svg%3E\")",
        }}
      />
    </div>
  );
};

export const HowItWorks: React.FC = () => {
  return (
    <AbsoluteFill style={{ background: COLORS.background }}>
      <PaperBackground />
      <GoldDust frame={0} count={10} area={{ x: 0, y: 0, w: 1440, h: 380 }} opacity={0.22} />

      <Sequence from={0} durationInFrames={hookDurationFrames}>
        <SceneHook />
      </Sequence>
      <Sequence from={60} durationInFrames={summaryDurationFrames}>
        <SceneSummary />
      </Sequence>
      <Sequence from={150} durationInFrames={logDurationFrames}>
        <SceneLog />
      </Sequence>
      <Sequence from={270} durationInFrames={budgetDurationFrames}>
        <SceneBudget />
      </Sequence>
      <Sequence from={360} durationInFrames={closeDurationFrames}>
        <SceneClose />
      </Sequence>

      {/* editorial paper wipe at the S3→S4 boundary */}
      <Sequence from={268} durationInFrames={18}>
        <PaperWipe />
      </Sequence>

      <AudioBed />
    </AbsoluteFill>
  );
};
