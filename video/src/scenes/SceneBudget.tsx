import React from "react";
import { AbsoluteFill, useCurrentFrame, interpolate } from "remotion";
import { PhoneStage } from "../fx/PhoneStage";
import { PhoneFrame } from "../phone/PhoneFrame";
import { LogScreen } from "../ui/LogScreen";
import { PremiumDialog } from "../ui/primitives";
import { Caption, Ripple } from "../fx/Caption";
import { Finger } from "../fx/Finger";
import { CONTENT, CAPTION_Y } from "../layout";
import { EASE_EDIT, EASE_SOFT } from "../theme";

const DUR = 90;
// Shifted −60 with PHONE_TOP 600→540
const METER_CENTER = { x: 720, y: 930 };
const LOG_BUTTON = { x: 919, y: 1277 };

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
 * SHOT 4 — OPTIONAL DAILY BUDGET (9.0–12.0s).
 * Spent bar fills toward the ₱1,500 limit (green → warm → crimson), then a
 * restrained "Log anyway?" dialog. Finger taps Log. Caption "Optional daily guardrail."
 */
export const SceneBudget: React.FC = () => {
  const f = useCurrentFrame();
  const dialogShow = f >= 24 && f < 62;
  const dialogOpacity = interpolate(f, [24, 32, 56, 62], [0, 1, 1, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const dialogScale = interpolate(f, [24, 32], [0.92, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const over = f >= 62;

  const fill = over
    ? 1
    : interpolate(f, [2, 22], [0.81, 0.933], {
        extrapolateLeft: "clamp",
        extrapolateRight: "clamp",
        easing: EASE_SOFT,
      });
  const spent = over
    ? "₱1,580"
    : interpolate(f, [2, 22], [0, 1], { extrapolateLeft: "clamp", extrapolateRight: "clamp" }) >= 0.5
      ? "₱1,400"
      : "₱1,220";
  const remainLabel = over ? "Over by ₱80" : "Remaining ₱100";

  // push into the meter, then hold
  const scale = interpolate(f, [0, 36, DUR], [1.0, 1.065, 1.06], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_SOFT,
  });

  const press = tap(f, 54, 62);
  const fingerFade = interpolate(f, [40, 48, 66, 74], [0, 1, 1, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });
  const fingerY = LOG_BUTTON.y - 24 + Math.sin(f * 0.38) * 2.8 * (1 - press);

  const rowEnter = interpolate(f, [62, 74], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const sealedRow = over
    ? [
        {
          row: { id: "c1", time: "15:42", item: "Coffee", cost: "₱180", type: "WANT" as const },
          style: { opacity: rowEnter, transform: `scale(${1.05 - 0.05 * rowEnter})` },
        },
      ]
    : [];

  const capOpacity = interpolate(f, [64, 78], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const capRise = interpolate(f, [64, 80], [22, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });

  return (
    <AbsoluteFill>
      <PhoneStage scale={scale} originX={METER_CENTER.x} originY={METER_CENTER.y} float={0.55}>
        <PhoneFrame>
          <LogScreen
            width={CONTENT.W}
            height={CONTENT.H}
            data={{
              dateLabel: "TODAY  ·  Aug 5, 2026",
              filledLabel: "3 / 20",
              budget: {
                spent,
                budget: "₱1,500",
                fillPct: fill,
                over,
                remainLabel,
              },
              itemValue: dialogShow ? "Coffee" : "",
              costValue: dialogShow ? "180" : "",
              wantSelected: dialogShow,
              rows: [
                ...sealedRow,
                { row: { id: "r3", time: "15:10", item: "Iced tea", cost: "₱85", type: "WANT" } },
                { row: { id: "r2", time: "12:40", item: "Lunch", cost: "₱180", type: "NEED" } },
                { row: { id: "r1", time: "08:12", item: "Rice & eggs", cost: "₱125", type: "NEED" } },
              ],
            }}
            overlay={
              dialogShow ? (
                <PremiumDialog
                  eyebrow="DAILY BUDGET"
                  eyebrowColor="#C8102E"
                  title="Log anyway?"
                  buttons={[{ label: "Cancel", ghost: true }, { label: "Log", danger: true }]}
                  anim={{ opacity: dialogOpacity, scale: dialogScale }}
                >
                  <div style={{ fontFamily: "inherit" }}>
                    <div style={{ fontSize: 34, fontWeight: 600, color: "#1A1A1A" }}>
                      {'"Coffee" · ₱180'}
                    </div>
                    <div style={{ marginTop: 12, fontSize: 28, color: "#5A5A5A", lineHeight: 1.35 }}>
                      This puts you over by ₱80. Your daily budget is ₱1,500. Log anyway?
                    </div>
                  </div>
                </PremiumDialog>
              ) : null
            }
          />
        </PhoneFrame>

        {f >= 54 && f < 74 && (
          <Ripple
            x={LOG_BUTTON.x}
            y={LOG_BUTTON.y}
            progress={(f - 54) / 20}
            color="rgba(200,16,46,0.26)"
          />
        )}
        {fingerFade > 0 ? (
          <Finger
            tipX={LOG_BUTTON.x}
            tipY={fingerY}
            angle={-18}
            press={press}
            opacity={fingerFade}
            motionBlur={press > 0.1 && press < 0.9 ? 0.35 : 0}
          />
        ) : null}
      </PhoneStage>

      <Caption text="Optional daily guardrail." opacity={capOpacity} rise={capRise} y={CAPTION_Y} />
    </AbsoluteFill>
  );
};
export const budgetDurationFrames = DUR;
