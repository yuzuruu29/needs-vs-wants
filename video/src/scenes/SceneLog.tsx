import React from "react";
import { AbsoluteFill, useCurrentFrame, interpolate } from "remotion";
import { PhoneStage } from "../fx/PhoneStage";
import { PhoneFrame } from "../phone/PhoneFrame";
import { LogScreen } from "../ui/LogScreen";
import { Caption, Ripple } from "../fx/Caption";
import { Finger } from "../fx/Finger";
import { CONTENT, TARGETS, CAPTION_Y } from "../layout";
import { EASE_EDIT, EASE_SOFT, COLORS } from "../theme";

const DUR = 120;

/** 0→1→0 tap pulse over [t0,t1]. */
const tap = (f: number, t0: number, t1: number) => {
  const up = interpolate(f, [t0, t0 + 4], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_SOFT,
  });
  const down = interpolate(f, [t1 - 4, t1], [1, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_SOFT,
  });
  return Math.max(0, Math.min(1, Math.max(up, down)));
};

/**
 * SHOT 3 — LOG (5.0–9.0s).
 * Finger types Item "Coffee", Cost "180", taps WANT — the row seals with a
 * crimson badge and the time stamp locks. Ink-drying settle. Caption
 * "Seal it in the moment."
 */
export const SceneLog: React.FC = () => {
  const f = useCurrentFrame();

  // ---- typed values ----
  const itemChars = Math.max(0, Math.min(6, Math.ceil((f - 10) / 4)));
  const costChars = Math.max(0, Math.min(3, Math.ceil((f - 48) / 6)));

  // ---- seal ----
  const wantSel = f >= 86;
  const sealed = f >= 92;
  const filledLabel = sealed ? "3 / 20" : "2 / 20";
  const cupsSelected = f >= 88 && !sealed;

  const itemValue = sealed ? "" : "Coffee".slice(0, itemChars);
  const costValue = sealed ? "" : "180".slice(0, costChars);

  const itemFocused = f >= 6 && f < 44;
  const costFocused = f >= 48 && f < 76;

  // ---- finger waypoints ----
  const hoverItem = { x: TARGETS.itemField.x, y: TARGETS.itemField.y - 30 };
  const tapItem = { x: TARGETS.itemField.x, y: TARGETS.itemField.y };
  const hoverCost = { x: TARGETS.costField.x, y: TARGETS.costField.y - 28 };
  const tapCost = { x: TARGETS.costField.x, y: TARGETS.costField.y };
  const hoverWant = { x: TARGETS.wantChip.x, y: TARGETS.wantChip.y - 34 };
  const tapWant = { x: TARGETS.wantChip.x, y: TARGETS.wantChip.y };

  const lerp = (a: { x: number; y: number }, b: { x: number; y: number }, t: number) => ({
    x: a.x + (b.x - a.x) * t,
    y: a.y + (b.y - a.y) * t,
  });

  let pos: { x: number; y: number };
  let travelSpeed = 0;
  if (f < 40) {
    const t = tap(f, 6, 14);
    pos = lerp(hoverItem, tapItem, t);
    travelSpeed = t > 0.05 && t < 0.95 ? 0.35 : 0;
  } else if (f < 46) {
    const t = interpolate(f, [40, 46], [0, 1], {
      extrapolateLeft: "clamp",
      extrapolateRight: "clamp",
      easing: EASE_EDIT,
    });
    pos = lerp(hoverItem, hoverCost, t * 0.55);
    travelSpeed = 0.55;
  } else if (f < 74) {
    const t = tap(f, 50, 58);
    pos = lerp(hoverCost, tapCost, t);
    travelSpeed = t > 0.05 && t < 0.95 ? 0.35 : 0;
  } else if (f < 84) {
    const t = interpolate(f, [74, 84], [0, 1], {
      extrapolateLeft: "clamp",
      extrapolateRight: "clamp",
      easing: EASE_EDIT,
    });
    pos = lerp(hoverCost, hoverWant, t);
    travelSpeed = 0.7;
  } else {
    const t = tap(f, 86, 94);
    pos = lerp(hoverWant, tapWant, t);
    travelSpeed = t > 0.05 && t < 0.95 ? 0.4 : 0;
  }

  const pressAmt = Math.max(tap(f, 6, 14), tap(f, 50, 58), tap(f, 86, 94));
  // hover bob damps during press
  pos.y += Math.sin(f * 0.42) * 3.5 * (1 - pressAmt);

  const fingerFade = interpolate(f, [0, 4, 106, 116], [0, 1, 1, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  // ---- new sealed row animation (ink drying) ----
  const rowEnter = interpolate(f, [92, 106], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const rowScale = 1.06 - 0.06 * rowEnter;

  // ---- caption ----
  const capOpacity = interpolate(f, [100, 114], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const capRise = interpolate(f, [100, 116], [22, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });

  const rows = sealed
    ? [
        {
          row: { id: "c1", time: "15:42", item: "Coffee", cost: "₱180", type: "WANT" as const },
          style: {
            opacity: rowEnter,
            transform: `scale(${rowScale})`,
            border: `1.5px solid ${rowEnter >= 1 ? COLORS.divider : COLORS.goldSoft}`,
          },
        },
        { row: { id: "r1", time: "08:12", item: "Rice & eggs", cost: "₱125", type: "NEED" as const } },
      ]
    : [{ row: { id: "r1", time: "08:12", item: "Rice & eggs", cost: "₱125", type: "NEED" as const } }];

  return (
    <AbsoluteFill>
      <PhoneStage scale={1.0} originX={720} originY={1220} float={0.7}>
        <PhoneFrame>
          <LogScreen
            width={CONTENT.W}
            height={CONTENT.H}
            data={{
              dateLabel: "TODAY  ·  Aug 5, 2026",
              filledLabel,
              itemValue,
              costValue,
              focusedField: itemFocused ? "item" : costFocused ? "cost" : null,
              wantSelected: wantSel && !sealed,
              rows,
            }}
          />
        </PhoneFrame>

        {f >= 8 && f < 28 && (
          <Ripple
            x={TARGETS.itemField.x}
            y={TARGETS.itemField.y}
            progress={(f - 8) / 20}
            color="rgba(200,16,46,0.16)"
          />
        )}
        {f >= 52 && f < 72 && (
          <Ripple
            x={TARGETS.costField.x}
            y={TARGETS.costField.y}
            progress={(f - 52) / 20}
            color="rgba(200,16,46,0.16)"
          />
        )}
        {f >= 88 && cupsSelected && (
          <Ripple
            x={TARGETS.wantChip.x}
            y={TARGETS.wantChip.y}
            progress={(f - 88) / 22}
            color="rgba(200,16,46,0.28)"
          />
        )}

        {fingerFade > 0 ? (
          <Finger
            tipX={pos.x}
            tipY={pos.y}
            angle={-18}
            press={pressAmt}
            opacity={fingerFade}
            motionBlur={travelSpeed * (1 - pressAmt * 0.5)}
          />
        ) : null}
      </PhoneStage>

      <Caption text="Seal it in the moment." opacity={capOpacity} rise={capRise} y={CAPTION_Y} />
    </AbsoluteFill>
  );
};
export const logDurationFrames = DUR;
