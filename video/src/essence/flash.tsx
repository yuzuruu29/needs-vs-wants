import React from "react";
import { interpolate, useCurrentFrame } from "remotion";
import { COLORS, EASE_EDIT, EASE_SEAL, FONT_STACK } from "../theme";
import { prog } from "./blocks";

/**
 * "Flash" motion toolkit for the scenario pack 2 — bigger set pieces, still
 * editorial: 3D flip cells, rolling odometers, tilt cards, swing tags,
 * draw-on annotations, one-shot dust bursts. All deterministic, all driven
 * by the local Sequence frame, springs never bouncy (house rule).
 */

/* ────────────────────────────────────────────────────────────────────────── */
/* Odometer — per-digit vertical roll                                         */
/* ────────────────────────────────────────────────────────────────────────── */

const DIGIT_STRIP = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0];

export const Odometer: React.FC<{
  value: number; // may be fractional mid-animation
  maxDigits: number; // fixed layout width (digits only, commas added)
  fontSize?: number;
  color?: string;
  prefix?: string;
  suffix?: string;
  weight?: number;
}> = ({ value, maxDigits, fontSize = 96, color = COLORS.ink, prefix, suffix, weight = 700 }) => {
  const cellH = fontSize * 1.12;
  const cellW = fontSize * 0.62;
  const v = Math.max(0, value);

  const columns = Array.from({ length: maxDigits }, (_, i) => {
    const k = maxDigits - 1 - i; // power for this column (0 = rightmost)
    const pow = Math.pow(10, k);
    // Mechanical-odometer roll: a wheel only turns while the wheel below it
    // sweeps its last 10%, so resting values sit perfectly aligned.
    const base = Math.floor(v / pow) % 10;
    const lower = v % pow;
    const carry =
      k === 0
        ? v % 1
        : Math.max(0, Math.min(1, (lower - pow * 0.9) / (pow * 0.1)));
    const roll = base + carry;
    const visible = v >= pow || k === 0;
    return { k, roll, visible };
  });

  return (
    <div
      style={{
        display: "inline-flex",
        alignItems: "center",
        fontFamily: FONT_STACK,
        fontWeight: weight,
        fontSize,
        color,
        fontVariantNumeric: "tabular-nums",
        lineHeight: 1,
      }}
    >
      {prefix ? <span style={{ marginRight: fontSize * 0.06 }}>{prefix}</span> : null}
      {columns.map((c, i) => {
        const groupComma = c.k % 3 === 2 && i !== 0; // comma before this column's group
        return (
          <React.Fragment key={i}>
            {groupComma ? (
              <span style={{ opacity: c.visible ? 1 : 0, width: fontSize * 0.28 }}>,</span>
            ) : null}
            <span
              style={{
                position: "relative",
                display: "inline-block",
                width: cellW,
                height: cellH,
                overflow: "hidden",
                opacity: c.visible ? 1 : 0,
              }}
            >
              <span
                style={{
                  position: "absolute",
                  left: 0,
                  top: 0,
                  display: "flex",
                  flexDirection: "column",
                  alignItems: "center",
                  width: cellW,
                  transform: `translateY(${-c.roll * cellH}px)`,
                }}
              >
                {DIGIT_STRIP.map((d, j) => (
                  <span
                    key={j}
                    style={{
                      height: cellH,
                      display: "flex",
                      alignItems: "center",
                      justifyContent: "center",
                    }}
                  >
                    {d}
                  </span>
                ))}
              </span>
            </span>
          </React.Fragment>
        );
      })}
      {suffix ? <span style={{ marginLeft: fontSize * 0.12 }}>{suffix}</span> : null}
    </div>
  );
};

/* ────────────────────────────────────────────────────────────────────────── */
/* FlipSequence — split-flap clock cells                                      */
/* ────────────────────────────────────────────────────────────────────────── */

const FlipCell: React.FC<{
  from: string;
  to: string;
  p: number; // 0..1 flip progress
  w: number;
  h: number;
  fontSize: number;
  color: string;
  bg: string;
}> = ({ from, to, p, w, h, fontSize, color, bg }) => {
  const digit = (ch: string) => (
    <span
      style={{
        fontFamily: FONT_STACK,
        fontWeight: 700,
        fontSize,
        color,
        fontVariantNumeric: "tabular-nums",
        lineHeight: 1,
      }}
    >
      {ch}
    </span>
  );
  const half = (ch: string, which: "top" | "bottom") => (
    <div
      style={{
        position: "absolute",
        left: 0,
        width: w,
        height: h / 2,
        overflow: "hidden",
        top: which === "top" ? 0 : h / 2,
        background: bg,
        display: "flex",
        justifyContent: "center",
        borderRadius: which === "top" ? "12px 12px 0 0" : "0 0 12px 12px",
      }}
    >
      <div
        style={{
          position: "absolute",
          top: which === "top" ? 0 : -h / 2,
          height: h,
          display: "flex",
          alignItems: "center",
        }}
      >
        {digit(ch)}
      </div>
    </div>
  );

  const topFlapRot = interpolate(p, [0, 0.5], [0, -89], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_EDIT,
  });
  const botFlapRot = interpolate(p, [0.5, 1], [89, 0], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
    easing: EASE_SEAL,
  });

  return (
    <div
      style={{
        position: "relative",
        width: w,
        height: h,
        perspective: 640,
        filter: "drop-shadow(0 10px 18px rgba(26,26,26,0.22))",
      }}
    >
      {/* static: top shows target, bottom shows source */}
      {half(to, "top")}
      {half(from, "bottom")}
      {/* falling top flap (source) */}
      {p > 0.001 && p < 0.5 ? (
        <div style={{ position: "absolute", inset: 0, transformStyle: "preserve-3d" }}>
          <div
            style={{
              position: "absolute",
              left: 0,
              top: 0,
              width: w,
              height: h / 2,
              transformOrigin: "bottom center",
              transform: `rotateX(${topFlapRot}deg)`,
              overflow: "hidden",
              background: bg,
              display: "flex",
              justifyContent: "center",
              borderRadius: "12px 12px 0 0",
              boxShadow: "0 1px 0 rgba(255,255,255,0.06) inset",
            }}
          >
            <div style={{ position: "absolute", top: 0, height: h, display: "flex", alignItems: "center" }}>
              {digit(from)}
            </div>
            <div
              style={{
                position: "absolute",
                inset: 0,
                background: `rgba(0,0,0,${(0.001 + p) * 0.5})`,
              }}
            />
          </div>
        </div>
      ) : null}
      {/* rising bottom flap (target) */}
      {p >= 0.5 && p < 0.999 ? (
        <div style={{ position: "absolute", inset: 0, transformStyle: "preserve-3d" }}>
          <div
            style={{
              position: "absolute",
              left: 0,
              top: h / 2,
              width: w,
              height: h / 2,
              transformOrigin: "top center",
              transform: `rotateX(${botFlapRot}deg)`,
              overflow: "hidden",
              background: bg,
              display: "flex",
              justifyContent: "center",
              borderRadius: "0 0 12px 12px",
            }}
          >
            <div style={{ position: "absolute", top: -h / 2, height: h, display: "flex", alignItems: "center" }}>
              {digit(to)}
            </div>
            <div
              style={{
                position: "absolute",
                inset: 0,
                background: `rgba(0,0,0,${(1 - p) * 0.4})`,
              }}
            />
          </div>
        </div>
      ) : null}
      {/* split hairline */}
      <div
        style={{
          position: "absolute",
          left: 2,
          right: 2,
          top: h / 2 - 1,
          height: 2,
          background: "rgba(0,0,0,0.28)",
        }}
      />
    </div>
  );
};

/**
 * Split-flap display stepping through `sequence`. Cells that differ between
 * consecutive entries flip; colons render as plain separators.
 */
export const FlipSequence: React.FC<{
  sequence: string[];
  startAt: number;
  stepFrames: number;
  cellW?: number;
  cellH?: number;
  fontSize?: number;
  color?: string;
  bg?: string;
  flipFrames?: number;
}> = ({
  sequence,
  startAt,
  stepFrames,
  cellW = 96,
  cellH = 130,
  fontSize = 84,
  color = "#FAFAF7",
  bg = "#22242A",
  flipFrames = 9,
}) => {
  const f = useCurrentFrame();
  const stepRaw = (f - startAt) / stepFrames;
  const idx = Math.max(0, Math.min(sequence.length - 1, Math.floor(stepRaw)));
  const next = Math.min(sequence.length - 1, idx + 1);
  const within = f < startAt ? 0 : (f - startAt - idx * stepFrames) / flipFrames;
  const p = idx === next ? 0 : Math.max(0, Math.min(1, within));

  const cur = sequence[idx];
  const nxt = sequence[next];

  return (
    <div style={{ display: "inline-flex", gap: 10, alignItems: "center" }}>
      {cur.split("").map((ch, i) => {
        if (ch === ":") {
          return (
            <span
              key={i}
              style={{
                fontFamily: FONT_STACK,
                fontWeight: 700,
                fontSize: fontSize * 0.82,
                color: bg,
                margin: "0 2px",
              }}
            >
              :
            </span>
          );
        }
        const target = nxt[i] ?? ch;
        return (
          <FlipCell
            key={i}
            from={ch}
            to={target}
            p={ch === target ? 0 : p}
            w={cellW}
            h={cellH}
            fontSize={fontSize}
            color={color}
            bg={bg}
          />
        );
      })}
    </div>
  );
};

/* ────────────────────────────────────────────────────────────────────────── */
/* Cards, tags, grids                                                         */
/* ────────────────────────────────────────────────────────────────────────── */

/** 3D tilt-in wrapper for cards. */
export const TiltIn: React.FC<
  React.PropsWithChildren<{ p: number; fromY?: number; fromRotX?: number; style?: React.CSSProperties }>
> = ({ p, fromY = 74, fromRotX = 26, children, style }) => (
  <div
    style={{
      opacity: Math.min(1, p * 1.4),
      transform: `perspective(1200px) rotateX(${(1 - p) * fromRotX}deg) translateY(${(1 - p) * fromY}px) scale(${0.93 + p * 0.07})`,
      transformOrigin: "center bottom",
      ...style,
    }}
  >
    {children}
  </div>
);

/** Price tag swinging from a string, damped pendulum. */
export const SwingTag: React.FC<{
  label: string;
  at: number;
  color?: string;
  stringLen?: number;
  fontSize?: number;
  phase?: number;
}> = ({ label, at, color = COLORS.crimson, stringLen = 90, fontSize = 40, phase = 0 }) => {
  const f = useCurrentFrame();
  const inP = prog(f, at, at + 16, EASE_SEAL);
  const t = Math.max(0, f - at);
  const damp = Math.exp(-t * 0.028);
  const swing = Math.sin(t * 0.16 + phase) * 14 * damp + Math.sin(t * 0.021 + phase) * 2.2;
  return (
    <div
      style={{
        display: "inline-block",
        transformOrigin: "top center",
        transform: `rotate(${swing}deg)`,
        opacity: inP,
      }}
    >
      <div style={{ width: 2.5, height: stringLen, background: COLORS.dividerStrong, margin: "0 auto" }} />
      <div
        style={{
          background: COLORS.card,
          border: `2.5px solid ${color}`,
          borderRadius: 14,
          padding: "12px 26px 14px",
          fontFamily: FONT_STACK,
          fontWeight: 700,
          fontSize,
          color,
          boxShadow: "0 18px 34px -18px rgba(26,26,26,0.4)",
          position: "relative",
          fontVariantNumeric: "tabular-nums",
        }}
      >
        <div
          style={{
            position: "absolute",
            top: -7,
            left: "50%",
            width: 12,
            height: 12,
            borderRadius: 7,
            background: COLORS.background,
            border: `2.5px solid ${color}`,
            transform: "translateX(-50%)",
          }}
        />
        {label}
      </div>
    </div>
  );
};

/** Small milk-tea cup, used by the multiplication grid. */
export const CupIcon: React.FC<{ size?: number; tone?: number }> = ({ size = 74, tone = 1 }) => (
  <svg width={size} height={size * 1.25} viewBox="0 0 60 75">
    <line x1="36" y1="2" x2="26" y2="26" stroke={COLORS.goldDeep} strokeWidth="4" strokeLinecap="round" />
    <path
      d={`M12 18 L48 18 L43 70 Q42.5 73 39.5 73 L20.5 73 Q17.5 73 17 70 Z`}
      fill="#F7EBD9"
      stroke={COLORS.dividerStrong}
      strokeWidth="2.5"
      opacity={0.4 + tone * 0.6}
    />
    <path d={`M15.5 44 L44.5 44 L43 70 Q42.5 73 39.5 73 L20.5 73 Q17.5 73 17 70 Z`} fill="#D9A05B" opacity={0.55 + tone * 0.35} />
    {[22, 30, 38].map((x) => (
      <circle key={x} cx={x} cy={66} r={3.4} fill="#4A2E18" opacity={0.75} />
    ))}
    <rect x="10" y="14" width="40" height="7" rx="3.5" fill="#FFFFFF" stroke={COLORS.dividerStrong} strokeWidth="2" />
  </svg>
);

/** Wave-staggered grid reveal of children clones. */
export const StaggerGrid: React.FC<{
  rows: number;
  cols: number;
  at: number;
  cellDelay?: number;
  gap?: number;
  render: (i: number, p: number) => React.ReactNode;
}> = ({ rows, cols, at, cellDelay = 3, gap = 20, render }) => {
  const f = useCurrentFrame();
  return (
    <div
      style={{
        display: "grid",
        gridTemplateColumns: `repeat(${cols}, auto)`,
        gap,
        justifyContent: "center",
      }}
    >
      {Array.from({ length: rows * cols }, (_, i) => {
        const r = Math.floor(i / cols);
        const c = i % cols;
        const delay = (r + c) * cellDelay + c * 1.2;
        const p = prog(f, at + delay, at + delay + 10, EASE_SEAL);
        return (
          <div
            key={i}
            style={{
              opacity: Math.min(1, p * 1.5),
              transform: `scale(${0.3 + p * 0.7}) translateY(${(1 - p) * 16}px)`,
            }}
          >
            {render(i, p)}
          </div>
        );
      })}
    </div>
  );
};

/* ────────────────────────────────────────────────────────────────────────── */
/* Annotation + particles                                                     */
/* ────────────────────────────────────────────────────────────────────────── */

/** Hand-drawn gold ellipse that draws itself around a target area. */
export const DrawEllipse: React.FC<{
  w: number;
  h: number;
  p: number;
  color?: string;
  stroke?: number;
  tilt?: number;
}> = ({ w, h, p, color = COLORS.goldDeep, stroke = 5, tilt = -5 }) => (
  <svg
    width={w}
    height={h}
    viewBox={`0 0 ${w} ${h}`}
    style={{ transform: `rotate(${tilt}deg)`, overflow: "visible" }}
  >
    <ellipse
      cx={w / 2}
      cy={h / 2}
      rx={w / 2 - stroke}
      ry={h / 2 - stroke}
      fill="none"
      stroke={color}
      strokeWidth={stroke}
      strokeLinecap="round"
      pathLength={1}
      strokeDasharray={1}
      strokeDashoffset={1 - Math.min(1, p)}
      opacity={p > 0.02 ? 0.9 : 0}
      transform={`rotate(-8 ${w / 2} ${h / 2})`}
    />
    <ellipse
      cx={w / 2 + 4}
      cy={h / 2 + 3}
      rx={w / 2 - stroke - 3}
      ry={h / 2 - stroke - 1}
      fill="none"
      stroke={color}
      strokeWidth={stroke * 0.7}
      strokeLinecap="round"
      pathLength={1}
      strokeDasharray={1}
      strokeDashoffset={1 - Math.min(1, Math.max(0, p * 1.15 - 0.15))}
      opacity={p > 0.2 ? 0.5 : 0}
      transform={`rotate(-11 ${w / 2} ${h / 2})`}
    />
  </svg>
);

const seed = (i: number) => {
  const x = Math.sin(i * 91.7 + 47.3) * 43758.5453;
  return x - Math.floor(x);
};

/** One-shot radial gold burst (milestones, wins). progress 0..1. */
export const BurstDust: React.FC<{
  p: number;
  radius?: number;
  count?: number;
  color?: string;
}> = ({ p, radius = 170, count = 14, color = "rgba(232,169,42," }) => {
  if (p <= 0 || p >= 1) return null;
  return (
    <div style={{ position: "absolute", inset: 0, pointerEvents: "none" }}>
      {Array.from({ length: count }, (_, i) => {
        const a = (i / count) * Math.PI * 2 + seed(i) * 0.6;
        const dist = radius * (0.55 + seed(i + 31) * 0.45) * Math.pow(p, 0.62);
        const size = (7 + seed(i + 7) * 9) * (1 - p * 0.6);
        const op = (1 - p) * (0.5 + seed(i + 13) * 0.5);
        return (
          <div
            key={i}
            style={{
              position: "absolute",
              left: `calc(50% + ${Math.cos(a) * dist}px)`,
              top: `calc(50% + ${Math.sin(a) * dist * 0.86}px)`,
              width: size,
              height: size,
              borderRadius: size,
              background: `radial-gradient(circle, ${color}0.95) 0%, ${color}0) 70%)`,
              opacity: op,
            }}
          />
        );
      })}
    </div>
  );
};

/** Expanding milestone ring. */
export const PulseRing: React.FC<{ p: number; size?: number; color?: string }> = ({
  p,
  size = 190,
  color = COLORS.gold,
}) => {
  if (p <= 0 || p >= 1) return null;
  const s = size * (0.55 + p * 0.85);
  return (
    <div
      style={{
        position: "absolute",
        left: "50%",
        top: "50%",
        width: s,
        height: s,
        transform: "translate(-50%, -50%)",
        borderRadius: "50%",
        border: `3px solid ${color}`,
        opacity: (1 - p) * 0.8,
        boxShadow: `0 0 24px ${color}55`,
      }}
    />
  );
};

/* ────────────────────────────────────────────────────────────────────────── */
/* Receipt roll + night veil                                                  */
/* ────────────────────────────────────────────────────────────────────────── */

/** Tall receipt scrolling inside a fixed window, with edge fades. */
export const ReceiptRoll: React.FC<{
  width: number;
  height: number;
  scroll: number; // px scrolled (positive = content moves up)
  children: React.ReactNode;
}> = ({ width, height, scroll, children }) => (
  <div style={{ position: "relative", width, height }}>
    <div style={{ position: "absolute", inset: 0, overflow: "hidden", borderRadius: 8 }}>
      <div style={{ transform: `translateY(${-scroll}px)` }}>{children}</div>
    </div>
    <div
      style={{
        position: "absolute",
        left: -8,
        right: -8,
        top: -4,
        height: 64,
        background: `linear-gradient(180deg, ${COLORS.background} 8%, rgba(250,250,247,0) 100%)`,
      }}
    />
    <div
      style={{
        position: "absolute",
        left: -8,
        right: -8,
        bottom: -4,
        height: 64,
        background: `linear-gradient(0deg, ${COLORS.background} 8%, rgba(250,250,247,0) 100%)`,
      }}
    />
  </div>
);

/**
 * Evening treatment over the paper void: cool dusk shade plus a warm lamp
 * pool, both scaled by `night` (0 = day, 1 = deep night) so scenes can
 * animate a sunrise.
 */
export const NightVeil: React.FC<{ night: number }> = ({ night }) => {
  const n = Math.max(0, Math.min(1, night));
  if (n <= 0.003) return null;
  return (
    <div style={{ position: "absolute", inset: 0, pointerEvents: "none" }}>
      <div
        style={{
          position: "absolute",
          inset: 0,
          background: "linear-gradient(180deg, #232A3A 0%, #38394A 55%, #4A4252 100%)",
          opacity: n * 0.5,
          mixBlendMode: "multiply",
        }}
      />
      <div
        style={{
          position: "absolute",
          inset: 0,
          background:
            "radial-gradient(52% 34% at 50% 24%, rgba(255,196,110,0.34) 0%, rgba(255,196,110,0) 70%)",
          opacity: n,
          mixBlendMode: "soft-light",
        }}
      />
      <div
        style={{
          position: "absolute",
          inset: 0,
          background:
            "radial-gradient(130% 100% at 50% 110%, rgba(10,12,24,0.5) 0%, rgba(10,12,24,0) 55%)",
          opacity: n * 0.55,
        }}
      />
    </div>
  );
};
