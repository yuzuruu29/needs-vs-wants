import React from "react";
import { AbsoluteFill, useCurrentFrame, interpolate } from "remotion";
import { PhoneStage } from "../fx/PhoneStage";
import { PhoneFrame } from "../phone/PhoneFrame";
import { SummaryScreen } from "../ui/SummaryScreen";
import { Caption } from "../fx/Caption";
import { CONTENT, CAPTION_Y } from "../layout";
import { EASE_EDIT, COLORS, SERIF_STACK, FONT_STACK } from "../theme";

const DUR = 90;
const START_NEED_PCT = 60;
const FINAL_NEED_PCT = 50;

/**
 * SHOT 5 — CLOSE — TRAINER, NOT ARCHIVE (12.0–15.0s).
 * Cut back to Summary: donut updates with new Want slice, gold "35-day window",
 * phone eases back, brand lockup + "Get the app" pill. Hold last 0.8s.
 */
export const SceneClose: React.FC = () => {
  const f = useCurrentFrame();

  const needSweepStart = (START_NEED_PCT / 100) * 360;
  const needSweepEnd = (FINAL_NEED_PCT / 100) * 360;
  const needSweep = interpolate(f, [4, 22], [needSweepStart, needSweepEnd], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const needPct = Math.round((needSweep / 360) * 100);

  // phone eases back (pull-out to centered hero)
  const scale = interpolate(f, [0, 24], [1.1, 0.98], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });

  const windowOpacity = interpolate(f, [26, 40], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const windowRise = interpolate(f, [26, 42], [12, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });

  const capOpacity = interpolate(f, [12, 26], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const capRise = interpolate(f, [12, 28], [20, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });

  const brandOpacity = interpolate(f, [40, 56], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const brandRise = interpolate(f, [40, 58], [18, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const ctaOpacity = interpolate(f, [58, 72], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });

  return (
    <AbsoluteFill>
      <PhoneStage scale={scale} originX={720} originY={1180} float={0.85}>
        <PhoneFrame>
          <SummaryScreen
            width={CONTENT.W}
            height={CONTENT.H}
            data={{
              needPct,
              needsMoney: "₱1,000.00",
              wantsMoney: "₱1,000.00",
              totalMoney: "₱2,000.00",
              period: "Day",
              ctaLabel: "Log a Purchase",
            }}
          />
        </PhoneFrame>
      </PhoneStage>

      {/* gold "35-day window" */}
      <div
        style={{
          position: "absolute",
          top: 1935,
          left: 0,
          right: 0,
          textAlign: "center",
          opacity: windowOpacity,
          transform: `translateY(${windowRise}px)`,
          fontFamily: SERIF_STACK,
          fontSize: 38,
          fontStyle: "italic",
          color: COLORS.goldDeep,
          letterSpacing: 1.2,
          WebkitFontSmoothing: "antialiased",
        }}
      >
        35-day window
      </div>

      <Caption text="Train spending. Offline." opacity={capOpacity} rise={capRise} y={CAPTION_Y - 10} />

      {/* brand lockup — small-caps serif */}
      <div
        style={{
          position: "absolute",
          top: 2165,
          left: 0,
          right: 0,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          opacity: brandOpacity,
          transform: `translateY(${brandRise}px)`,
        }}
      >
        <div
          style={{
            width: 56,
            height: 2.5,
            background: COLORS.gold,
            borderRadius: 2,
            marginBottom: 18,
            boxShadow: "0 0 10px rgba(232,169,42,0.3)",
          }}
        />
        <div
          style={{
            fontFamily: SERIF_STACK,
            fontSize: 70,
            fontWeight: 700,
            letterSpacing: 5.5,
            color: COLORS.ink,
            fontVariantCaps: "small-caps",
            WebkitFontSmoothing: "antialiased",
          }}
        >
          NEEDS VS WANTS
        </div>
      </div>

      {/* get the app CTA pill */}
      <div
        style={{
          position: "absolute",
          top: 2315,
          left: 0,
          right: 0,
          display: "flex",
          justifyContent: "center",
          opacity: ctaOpacity,
        }}
      >
        <div
          style={{
            width: 400,
            height: 90,
            borderRadius: 45,
            background: COLORS.crimson,
            color: COLORS.card,
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
            fontFamily: FONT_STACK,
            fontSize: 38,
            fontWeight: 600,
            letterSpacing: 1.2,
            boxShadow: "0 22px 48px -20px rgba(200,16,46,0.55)",
          }}
        >
          Get the app
        </div>
      </div>
    </AbsoluteFill>
  );
};
export const closeDurationFrames = DUR;
