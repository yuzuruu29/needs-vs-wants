import React from "react";
import { AbsoluteFill, Sequence, useCurrentFrame, interpolate, spring, useVideoConfig } from "remotion";
import { PaperBackground } from "./fx/PaperBackground";
import { GoldDust } from "./fx/GoldDust";
import { AudioBed } from "./audio/AudioBed";
import { EASE_EDIT, COLORS } from "./theme";
import { Inter, PlayfairSC } from "./Root";

/**
 * Scene 1: The Impulse Trap Hook (0 - 120 frames / 0-4s)
 */
const HookScene: React.FC = () => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  const titleOpacity = interpolate(frame, [0, 15], [0, 1], { extrapolateLeft: "clamp", extrapolateRight: "clamp" });
  const titleY = interpolate(frame, [0, 20], [40, 0], { extrapolateLeft: "clamp", extrapolateRight: "clamp", easing: EASE_EDIT });

  const cardScale = spring({ frame: frame - 15, fps, config: { damping: 14, stiffness: 90 } });
  const cardOpacity = interpolate(frame, [15, 25], [0, 1], { extrapolateLeft: "clamp", extrapolateRight: "clamp" });

  const morphProgress = interpolate(frame, [70, 110], [0, 1], { extrapolateLeft: "clamp", extrapolateRight: "clamp", easing: EASE_EDIT });

  return (
    <AbsoluteFill style={{ justifyContent: "center", alignItems: "center", padding: 80 }}>
      {/* Title */}
      <div
        style={{
          position: "absolute",
          top: 320,
          opacity: titleOpacity,
          transform: `translateY(${titleY}px)`,
          textAlign: "center",
        }}
      >
        <span style={{ fontFamily: Inter, fontSize: 36, fontWeight: 700, letterSpacing: "0.15em", color: COLORS.gold, textTransform: "uppercase" }}>
          Google Flow Video Prompt 01
        </span>
        <h1 style={{ fontFamily: PlayfairSC, fontSize: 84, color: COLORS.text, marginTop: 24, margin: "24px 0 0 0" }}>
          The Impulse Trap
        </h1>
      </div>

      {/* Credit Card / App Morph Container */}
      <div
        style={{
          width: 900,
          height: 560,
          borderRadius: 40,
          background: morphProgress > 0.5 ? COLORS.card : "linear-gradient(135deg, #1e293b 0%, #0f172a 100%)",
          border: `2px solid ${morphProgress > 0.5 ? COLORS.gold : "#334155"}`,
          boxShadow: "0 24px 60px rgba(0,0,0,0.18)",
          transform: `scale(${cardScale})`,
          opacity: cardOpacity,
          display: "flex",
          flexDirection: "column",
          padding: 60,
          justifyContent: "space-between",
          color: morphProgress > 0.5 ? COLORS.text : "#f8fafc",
          transition: "all 0.3s ease",
        }}
      >
        {morphProgress < 0.5 ? (
          <>
            <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
              <span style={{ fontFamily: Inter, fontSize: 32, fontWeight: 600, letterSpacing: "0.1em", color: "#94a3b8" }}>
                IMPULSE BUYING
              </span>
              <div style={{ width: 60, height: 44, borderRadius: 10, background: "linear-gradient(135deg, #f59e0b 0%, #d97706 100%)" }} />
            </div>
            <div>
              <div style={{ fontFamily: Inter, fontSize: 44, letterSpacing: "0.2em", color: "#e2e8f0" }}>•••• •••• •••• 8842</div>
              <div style={{ fontFamily: Inter, fontSize: 28, color: "#ef4444", marginTop: 20, fontWeight: 600 }}>
                Month-End Regret: -$184.50
              </div>
            </div>
          </>
        ) : (
          <div style={{ display: "flex", flexDirection: "column", height: "100%", justifyContent: "center", alignItems: "center" }}>
            <span style={{ fontFamily: Inter, fontSize: 32, fontWeight: 700, color: COLORS.want, textTransform: "uppercase", letterSpacing: "0.1em" }}>
              Confront Impulse Spending
            </span>
            <span style={{ fontFamily: PlayfairSC, fontSize: 52, color: COLORS.text, marginTop: 20 }}>
              Real-Time Binary Classification
            </span>
          </div>
        )}
      </div>
    </AbsoluteFill>
  );
};

/**
 * Scene 2: Binary Choice & Tactile Seal (120 - 270 frames / 4-9s)
 */
const BinaryChoiceScene: React.FC = () => {
  const frame = useCurrentFrame();
  const { fps } = useVideoConfig();

  // Local frame within Scene 2 (0 to 150)
  const localFrame = frame - 120;

  const sceneOpacity = interpolate(localFrame, [0, 15], [0, 1], { extrapolateLeft: "clamp", extrapolateRight: "clamp" });
  const rowTypeProgress = interpolate(localFrame, [20, 50], [0, 1], { extrapolateLeft: "clamp", extrapolateRight: "clamp" });

  const stampTriggered = localFrame >= 70;
  const stampScale = stampTriggered
    ? spring({ frame: localFrame - 70, fps, config: { damping: 12, stiffness: 220 } })
    : 3;
  const stampOpacity = interpolate(localFrame, [70, 75], [0, 1], { extrapolateLeft: "clamp", extrapolateRight: "clamp" });

  const typedText = "Late Night Delivery".slice(0, Math.floor(rowTypeProgress * 19));

  return (
    <AbsoluteFill style={{ justifyContent: "center", alignItems: "center", opacity: sceneOpacity, padding: 80 }}>
      {/* Title */}
      <div style={{ position: "absolute", top: 260, textAlign: "center" }}>
        <span style={{ fontFamily: Inter, fontSize: 32, fontWeight: 700, color: COLORS.gold, textTransform: "uppercase", letterSpacing: "0.15em" }}>
          Step 01 • Force A Single Binary Choice
        </span>
        <h2 style={{ fontFamily: PlayfairSC, fontSize: 72, color: COLORS.text, marginTop: 16, margin: "16px 0 0 0" }}>
          Need vs. Want
        </h2>
      </div>

      {/* 5-Column Ledger Card */}
      <div
        style={{
          width: 1100,
          background: COLORS.card,
          borderRadius: 36,
          border: `2px solid ${COLORS.gold}`,
          boxShadow: "0 30px 80px rgba(40,32,18,0.15)",
          padding: 50,
          position: "relative",
          marginTop: 100,
        }}
      >
        {/* Ledger Header */}
        <div style={{ display: "grid", gridTemplateColumns: "1.2fr 1fr 3fr 1.5fr 1.5fr", borderBottom: "2px solid #E5E0D8", paddingBottom: 20, fontFamily: Inter, fontSize: 24, fontWeight: 700, color: "#78716C" }}>
          <div>DATE</div>
          <div>TIME</div>
          <div>ITEM</div>
          <div>COST</div>
          <div>TYPE</div>
        </div>

        {/* Active Row */}
        <div style={{ display: "grid", gridTemplateColumns: "1.2fr 1fr 3fr 1.5fr 1.5fr", alignItems: "center", paddingTop: 30, fontFamily: Inter, fontSize: 32, fontWeight: 600, color: COLORS.text }}>
          <div>Aug 06</div>
          <div>11:52</div>
          <div>{typedText}<span style={{ opacity: localFrame % 20 < 10 ? 1 : 0, color: COLORS.want }}>|</span></div>
          <div style={{ color: COLORS.want }}>$18.50</div>
          <div style={{ display: "flex", gap: 12 }}>
            <span style={{ padding: "8px 16px", borderRadius: 12, background: "#E6F4EA", color: COLORS.need, fontSize: 22, fontWeight: 700 }}>
              NEED
            </span>
            <span style={{ padding: "8px 16px", borderRadius: 12, background: stampTriggered ? COLORS.want : "#FCE8E6", color: stampTriggered ? "#FFFFFF" : COLORS.want, fontSize: 22, fontWeight: 700 }}>
              WANT
            </span>
          </div>
        </div>

        {/* Tactile Red Seal Stamp */}
        {stampTriggered && (
          <div
            style={{
              position: "absolute",
              right: 60,
              top: 100,
              transform: `scale(${stampScale}) rotate(-12deg)`,
              opacity: stampOpacity,
              border: `6px double ${COLORS.want}`,
              borderRadius: 20,
              padding: "16px 36px",
              color: COLORS.want,
              fontFamily: PlayfairSC,
              fontSize: 48,
              fontWeight: 700,
              letterSpacing: "0.15em",
              textTransform: "uppercase",
              boxShadow: "0 12px 30px rgba(200,16,46,0.3)",
              background: "rgba(255,255,255,0.92)",
            }}
          >
            WANT SEALED
          </div>
        )}
      </div>
    </AbsoluteFill>
  );
};

/**
 * Scene 3: Summary Donut & CTA (270 - 450 frames / 9-15s)
 */
const SummaryCTAScene: React.FC = () => {
  const frame = useCurrentFrame();
  const localFrame = frame - 270;

  const opacity = interpolate(localFrame, [0, 15], [0, 1], { extrapolateLeft: "clamp", extrapolateRight: "clamp" });
  const chartProgress = interpolate(localFrame, [20, 80], [0, 100], { extrapolateLeft: "clamp", extrapolateRight: "clamp" });

  const ctaScale = interpolate(localFrame, [100, 130], [0.9, 1], { extrapolateLeft: "clamp", extrapolateRight: "clamp", easing: EASE_EDIT });

  return (
    <AbsoluteFill style={{ justifyContent: "center", alignItems: "center", opacity, padding: 80 }}>
      {/* Title */}
      <div style={{ textAlign: "center", marginBottom: 60 }}>
        <span style={{ fontFamily: Inter, fontSize: 32, fontWeight: 700, color: COLORS.gold, textTransform: "uppercase", letterSpacing: "0.15em" }}>
          Real-Time Behavioral Trainer
        </span>
        <h2 style={{ fontFamily: PlayfairSC, fontSize: 76, color: COLORS.text, marginTop: 16 }}>
          Stop Impulse Buying In Real Time.
        </h2>
      </div>

      {/* Donut Chart Mockup */}
      <div
        style={{
          width: 420,
          height: 420,
          borderRadius: "50%",
          background: `conic-gradient(${COLORS.need} 0% ${Math.min(chartProgress, 65)}%, ${COLORS.want} ${Math.min(chartProgress, 65)}% 100%)`,
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          boxShadow: "0 20px 60px rgba(0,0,0,0.12)",
        }}
      >
        <div
          style={{
            width: 280,
            height: 280,
            borderRadius: "50%",
            background: COLORS.background,
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
            alignItems: "center",
          }}
        >
          <span style={{ fontFamily: Inter, fontSize: 24, fontWeight: 700, color: "#78716C" }}>NEED / WANT</span>
          <span style={{ fontFamily: PlayfairSC, fontSize: 64, fontWeight: 700, color: COLORS.text }}>65 / 35</span>
        </div>
      </div>

      {/* CTA Pill */}
      <div
        style={{
          marginTop: 80,
          background: `linear-gradient(135deg, ${COLORS.want} 0%, #900B20 100%)`,
          color: "#FFFFFF",
          borderRadius: 100,
          padding: "28px 72px",
          display: "flex",
          alignItems: "center",
          gap: 20,
          boxShadow: "0 20px 50px rgba(200,16,46,0.35)",
          transform: `scale(${ctaScale})`,
        }}
      >
        <span style={{ fontFamily: Inter, fontSize: 36, fontWeight: 700 }}>Download Needs Vs. Wants</span>
        <span style={{ fontFamily: Inter, fontSize: 28, opacity: 0.85 }}>| needs-vs-wants.vercel.app</span>
      </div>
    </AbsoluteFill>
  );
};

export const GoogleFlowPromptImpulseTrap: React.FC = () => {
  return (
    <AbsoluteFill style={{ background: COLORS.background }}>
      <PaperBackground />
      <GoldDust frame={0} count={12} area={{ x: 0, y: 0, w: 1440, h: 400 }} opacity={0.25} />
      <AudioBed />

      <Sequence from={0} durationInFrames={120}>
        <HookScene />
      </Sequence>
      <Sequence from={120} durationInFrames={150}>
        <BinaryChoiceScene />
      </Sequence>
      <Sequence from={270} durationInFrames={180}>
        <SummaryCTAScene />
      </Sequence>
    </AbsoluteFill>
  );
};
