import React from "react";
import { AbsoluteFill, useCurrentFrame, interpolate } from "remotion";
import { PhoneStage } from "../fx/PhoneStage";
import { PhoneFrame } from "../phone/PhoneFrame";
import { LogScreen } from "../ui/LogScreen";
import { Caption } from "../fx/Caption";
import { Finger } from "../fx/Finger";
import { GoldDust } from "../fx/GoldDust";
import { CONTENT, TARGETS, CAPTION_Y } from "../layout";
import { EASE_EDIT } from "../theme";

const DUR = 60;

/**
 * SHOT 1 — HOOK (0.0–2.0s).
 * Extreme close-up on the NEED/WANT chips, optical rack-focus from soft
 * bokeh to sharp, gold dust, caption "Every purchase. One choice.", then
 * pull back to the full phone.
 */
export const SceneHook: React.FC = () => {
  const frame = useCurrentFrame();

  // Macro → pull-back: scale 3.45 → 1.0 (power ease), framed on the chip pair
  const scale = interpolate(frame, [0, 36], [3.45, 1], {
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  // Optical rack-focus: heavy soft bokeh → sharp (not a flat CSS blur cut)
  const blur = interpolate(frame, [0, 8, 24], [18, 10, 0], {
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const rackFocus = interpolate(frame, [0, 22], [1, 0], {
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });

  // Finger hovers over WANT chip, small bob, fades out as camera pulls back
  const fingerFade = interpolate(frame, [2, 8, 18, 28], [0, 1, 1, 0], {
    extrapolateRight: "clamp",
    extrapolateLeft: "clamp",
  });
  const bob = Math.sin(frame * 0.42) * 6;
  const fingerY = TARGETS.wantChip.y - 48 + bob;

  // Caption
  const capOpacity = interpolate(frame, [22, 34], [0, 1], {
    extrapolateRight: "clamp",
    extrapolateLeft: "clamp",
    easing: EASE_EDIT,
  });
  const capRise = interpolate(frame, [22, 38], [22, 0], {
    extrapolateRight: "clamp",
    extrapolateLeft: "clamp",
    easing: EASE_EDIT,
  });

  return (
    <AbsoluteFill>
      <PhoneStage
        scale={scale}
        originX={TARGETS.chipsMid.x}
        originY={TARGETS.chipsMid.y}
        blur={blur}
        rackFocus={rackFocus}
        float={0.35}
      >
        <PhoneFrame>
          <LogScreen
            width={CONTENT.W}
            height={CONTENT.H}
            data={{
              dateLabel: "TODAY  ·  Aug 5, 2026",
              filledLabel: "3 / 20",
              itemValue: "Coffee",
              costValue: "180",
              wantSelected: true,
              rows: [
                { row: { id: "r1", time: "12:40", item: "Lunch", cost: "₱180", type: "NEED" } },
                { row: { id: "r2", time: "08:12", item: "Rice & eggs", cost: "₱125", type: "NEED" } },
              ],
            }}
          />
        </PhoneFrame>
        {fingerFade > 0 ? (
          <Finger
            tipX={TARGETS.wantChip.x}
            tipY={fingerY}
            angle={-18}
            press={0.1}
            opacity={fingerFade}
          />
        ) : null}
      </PhoneStage>

      <GoldDust
        frame={frame}
        count={20}
        area={{ x: 100, y: 260, w: 1240, h: 960 }}
        opacity={0.92}
      />

      <Caption
        text="Every purchase. One choice."
        opacity={capOpacity}
        rise={capRise}
        y={CAPTION_Y}
      />
    </AbsoluteFill>
  );
};
export const hookDurationFrames = DUR;
