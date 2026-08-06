import React from "react";
import { AbsoluteFill, useCurrentFrame, interpolate } from "remotion";
import { PhoneStage } from "../fx/PhoneStage";
import { PhoneFrame } from "../phone/PhoneFrame";
import { SummaryScreen } from "../ui/SummaryScreen";
import { Caption } from "../fx/Caption";
import { CONTENT, CAPTION_Y } from "../layout";
import { EASE_EDIT, EASE_SOFT } from "../theme";

const DUR = 90;
const FINAL_NEED_PCT = 58;

/**
 * SHOT 2 — SUMMARY (2.0–5.0s).
 * Phone at three-quarter front with real perspective; the donut's green Need
 * arc draws in, the crimson Want arc fills the rest. Day pill active.
 * CTA "Log a Purchase" pulses once. Slow dolly-in. Caption "See Needs vs Wants."
 */
export const SceneSummary: React.FC = () => {
  const frame = useCurrentFrame();

  // Donut: green arc grows 0 → 58% (power ease), want fills the rest.
  const needSweep = interpolate(frame, [8, 46], [0, (FINAL_NEED_PCT / 100) * 360], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });

  // CTA pulse once (frames ~62..78)
  const pulse = (() => {
    const up = interpolate(frame, [62, 70], [0, 1], {
      extrapolateLeft: "clamp",
      extrapolateRight: "clamp",
      easing: EASE_SOFT,
    });
    const down = interpolate(frame, [70, 82], [1, 0], {
      extrapolateLeft: "clamp",
      extrapolateRight: "clamp",
      easing: EASE_SOFT,
    });
    return Math.max(up, down);
  })();

  // Camera: three-quarter tilt + slow dolly-in (~8%)
  const scale = interpolate(frame, [0, DUR], [0.96, 1.045], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_SOFT,
  });
  // Subtle rotateX for more dimensional three-quarter
  const rotateY = interpolate(frame, [0, DUR], [-9.5, -7.5], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_SOFT,
  });
  const rotateX = 2.5;

  // Caption
  const capOpacity = interpolate(frame, [14, 28], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const capRise = interpolate(frame, [14, 30], [22, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });

  return (
    <AbsoluteFill>
      <PhoneStage
        scale={scale}
        originX={720}
        originY={1220}
        rotateY={rotateY}
        rotateX={rotateX}
        float={1}
      >
        <PhoneFrame>
          <SummaryScreen
            width={CONTENT.W}
            height={CONTENT.H}
            data={{
              needPct: Math.round((needSweep / 360) * 100),
              needsMoney: "₱1,160.00",
              wantsMoney: "₱840.00",
              totalMoney: "₱2,000.00",
              period: "Day",
              rangeLabel: "Aug 5, 2026",
              ctaLabel: "Log a Purchase",
              ctaPulse: pulse,
            }}
          />
        </PhoneFrame>
      </PhoneStage>

      <Caption
        text="See Needs vs Wants."
        opacity={capOpacity}
        rise={capRise}
        y={CAPTION_Y}
      />
    </AbsoluteFill>
  );
};
export const summaryDurationFrames = DUR;
