import React from "react";
import { interpolate, useCurrentFrame } from "remotion";
import { COLORS, EASE_EDIT, EASE_SEAL, FONT_STACK, SERIF_STACK } from "../theme";
import { LAYOUT } from "../layout";
import { PhoneFrame } from "../phone/PhoneFrame";

const CLAMP = {
  extrapolateLeft: "clamp" as const,
  extrapolateRight: "clamp" as const,
};

/** Clamped eased progress 0..1 between two frames. */
export const prog = (
  f: number,
  from: number,
  to: number,
  easing: (t: number) => number = EASE_EDIT,
) => interpolate(f, [from, to], [0, 1], { ...CLAMP, easing });

/* ────────────────────────────────────────────────────────────────────────── */
/* Phone staging                                                              */
/* ────────────────────────────────────────────────────────────────────────── */

/**
 * Stages the shared portrait PhoneFrame (1440x2560 canvas coordinates) into
 * any output format. `x`/`y` place the phone centre in output px, `height`
 * is the phone's rendered height. Includes idle float + soft floor shadow.
 */
export const CanvasPhone: React.FC<{
  x: number;
  y: number;
  height: number;
  rotate?: number;
  float?: number;
  opacity?: number;
  shadow?: boolean;
  children: React.ReactNode;
}> = ({ x, y, height, rotate = 0, float = 1, opacity = 1, shadow = true, children }) => {
  const frame = useCurrentFrame();
  const s = height / LAYOUT.PHONE_H;
  const w = LAYOUT.PHONE_W * s;

  const driftY = Math.sin(frame * 0.045) * 5.5 * float;
  const driftX = Math.sin(frame * 0.031 + 1.2) * 2 * float;
  const driftRot = Math.sin(frame * 0.028) * 0.32 * float;
  const shadowBreath = 1 + Math.sin(frame * 0.045) * 0.05 * float;

  const cx = LAYOUT.PHONE_LEFT + LAYOUT.PHONE_W / 2;
  const cy = LAYOUT.PHONE_TOP + LAYOUT.PHONE_H / 2;

  return (
    <div style={{ position: "absolute", left: 0, top: 0, opacity }}>
      {shadow ? (
        <div
          style={{
            position: "absolute",
            left: x - (w * 0.72) / 2,
            top: y + height / 2 + 16 + driftY * 0.4,
            width: w * 0.72,
            height: Math.max(34, height * 0.055),
            borderRadius: "50%",
            background:
              "radial-gradient(ellipse at center, rgba(40,32,18,0.26) 0%, rgba(40,32,18,0.09) 46%, rgba(40,32,18,0) 72%)",
            transform: `scale(${shadowBreath}, 0.9)`,
            filter: "blur(9px)",
          }}
        />
      ) : null}
      <div
        style={{
          position: "absolute",
          left: 0,
          top: 0,
          width: LAYOUT.W,
          height: LAYOUT.H,
          transformOrigin: `${cx}px ${cy}px`,
          transform: `translate(${x - cx + driftX}px, ${y - cy + driftY}px) scale(${s}) rotate(${rotate + driftRot}deg)`,
          willChange: "transform",
        }}
      >
        <PhoneFrame>{children}</PhoneFrame>
      </div>
    </div>
  );
};

/* ────────────────────────────────────────────────────────────────────────── */
/* Typography moments                                                         */
/* ────────────────────────────────────────────────────────────────────────── */

/**
 * A serif word that lands like a rubber stamp: presses in from above with a
 * quick scale settle, ink bloom ring on impact, faint press shadow.
 * `at` is the local frame the stamp lands.
 */
export const StampWord: React.FC<{
  word: string;
  color: string;
  at: number;
  size?: number;
  tilt?: number;
  ringScale?: number;
  style?: React.CSSProperties;
}> = ({ word, color, at, size = 150, tilt = -2.4, ringScale = 1, style }) => {
  const f = useCurrentFrame();
  const press = prog(f, at, at + 12, EASE_SEAL);
  const ring = prog(f, at + 1, at + 26, EASE_EDIT);
  const scale = interpolate(press, [0, 1], [1.75, 1]);
  const opacity = interpolate(press, [0, 0.35, 1], [0, 0.92, 1]);
  const settle = interpolate(press, [0, 1], [tilt * 2.2, tilt]);

  const ringSize = size * (1.4 + ring * 1.1) * ringScale;
  const ringOpacity = press >= 1 ? Math.max(0, 0.5 - ring * 0.5) : 0;

  return (
    <div style={{ position: "relative", display: "inline-flex", justifyContent: "center", ...style }}>
      {/* ink bloom on impact */}
      <div
        style={{
          position: "absolute",
          left: "50%",
          top: "50%",
          width: ringSize * 2.2,
          height: ringSize,
          transform: "translate(-50%, -50%)",
          borderRadius: "50%",
          border: `3px solid ${color}`,
          opacity: ringOpacity,
          filter: "blur(1px)",
        }}
      />
      <span
        style={{
          fontFamily: SERIF_STACK,
          fontWeight: 700,
          fontSize: size,
          letterSpacing: size * 0.045,
          lineHeight: 1,
          color,
          opacity,
          display: "inline-block",
          transform: `scale(${scale}) rotate(${settle}deg)`,
          textShadow: `0 ${3 + (1 - press) * 14}px ${8 + (1 - press) * 26}px ${color}44`,
          WebkitFontSmoothing: "antialiased",
        }}
      >
        {word}
      </span>
    </div>
  );
};

/** Quiet editorial line with fade + rise. */
export const FadeUp: React.FC<
  React.PropsWithChildren<{
    at: number;
    duration?: number;
    rise?: number;
    out?: { at: number; duration?: number } | null;
    style?: React.CSSProperties;
  }>
> = ({ at, duration = 18, rise = 22, out = null, children, style }) => {
  const f = useCurrentFrame();
  const inP = prog(f, at, at + duration);
  const outP = out ? prog(f, out.at, out.at + (out.duration ?? 14)) : 0;
  return (
    <div
      style={{
        opacity: inP * (1 - outP),
        transform: `translateY(${(1 - inP) * rise - outP * 14}px)`,
        ...style,
      }}
    >
      {children}
    </div>
  );
};

/** Uppercase micro-kicker with gold hairline. */
export const Kicker: React.FC<{
  text: string;
  color?: string;
  align?: "flex-start" | "center";
  size?: number;
}> = ({ text, color = COLORS.crimson, align = "flex-start", size = 27 }) => (
  <div style={{ display: "flex", flexDirection: "column", alignItems: align, gap: 16 }}>
    <div
      style={{
        width: 54,
        height: 3,
        borderRadius: 2,
        background: COLORS.gold,
        boxShadow: "0 0 12px rgba(232,169,42,0.3)",
      }}
    />
    <div
      style={{
        fontFamily: FONT_STACK,
        fontSize: size,
        fontWeight: 600,
        letterSpacing: 4.5,
        textTransform: "uppercase",
        color,
      }}
    >
      {text}
    </div>
  </div>
);

/* ────────────────────────────────────────────────────────────────────────── */
/* Brand seal                                                                 */
/* ────────────────────────────────────────────────────────────────────────── */

/**
 * The brand mark: a coin-like double seal split into Need green and Want
 * crimson halves under a gold ring — the app icon identity, drawn vector.
 */
export const SealBadge: React.FC<{
  size?: number;
  progress?: number; // 0..1 stamp-in
  style?: React.CSSProperties;
}> = ({ size = 150, progress = 1, style }) => {
  const scale = interpolate(progress, [0, 1], [1.6, 1]);
  const opacity = interpolate(progress, [0, 0.4, 1], [0, 0.95, 1]);
  const r = size / 2;
  const inner = r - size * 0.085;
  return (
    <div
      style={{
        width: size,
        height: size,
        opacity,
        transform: `scale(${scale}) rotate(${(1 - progress) * -9}deg)`,
        filter: `drop-shadow(0 ${size * 0.08}px ${size * 0.16}px rgba(26,26,26,0.28))`,
        ...style,
      }}
    >
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
        {/* paper coin */}
        <circle cx={r} cy={r} r={r} fill="#FFFFFF" />
        <circle cx={r} cy={r} r={r - 1.5} fill="none" stroke={COLORS.gold} strokeWidth={size * 0.045} />
        {/* halves */}
        <path d={`M ${r} ${r - inner} A ${inner} ${inner} 0 0 0 ${r} ${r + inner} Z`} fill={COLORS.green} />
        <path d={`M ${r} ${r - inner} A ${inner} ${inner} 0 0 1 ${r} ${r + inner} Z`} fill={COLORS.crimson} />
        {/* split hairline */}
        <line x1={r} y1={r - inner} x2={r} y2={r + inner} stroke="#FFFFFF" strokeWidth={size * 0.02} />
        {/* letters */}
        <text
          x={r - inner * 0.46}
          y={r + size * 0.075}
          textAnchor="middle"
          fontFamily={SERIF_STACK}
          fontWeight={700}
          fontSize={size * 0.24}
          fill="#FFFFFF"
        >
          N
        </text>
        <text
          x={r + inner * 0.46}
          y={r + size * 0.075}
          textAnchor="middle"
          fontFamily={SERIF_STACK}
          fontWeight={700}
          fontSize={size * 0.24}
          fill="#FFFFFF"
        >
          W
        </text>
      </svg>
    </div>
  );
};

/* ────────────────────────────────────────────────────────────────────────── */
/* CTA + proof                                                                */
/* ────────────────────────────────────────────────────────────────────────── */

export const CtaPill: React.FC<{
  label: string;
  width?: number;
  height?: number;
  fontSize?: number;
  pulse?: number; // 0..1
  sheen?: number; // 0..1 sweep position
}> = ({ label, width = 430, height = 92, fontSize = 36, pulse = 0, sheen = -1 }) => (
  <div
    style={{
      width,
      height,
      borderRadius: height / 2,
      background: `linear-gradient(180deg, ${COLORS.crimson} 0%, ${COLORS.crimsonDeep} 100%)`,
      color: COLORS.card,
      display: "flex",
      alignItems: "center",
      justifyContent: "center",
      fontFamily: FONT_STACK,
      fontSize,
      fontWeight: 600,
      letterSpacing: 1.4,
      boxShadow: "0 24px 48px -18px rgba(200,16,46,0.55), 0 2px 0 rgba(255,255,255,0.14) inset",
      transform: `scale(${1 + pulse * 0.03})`,
      position: "relative",
      overflow: "hidden",
    }}
  >
    {sheen >= 0 && sheen <= 1 ? (
      <div
        style={{
          position: "absolute",
          top: -20,
          bottom: -20,
          width: 90,
          left: `${sheen * 130 - 15}%`,
          background:
            "linear-gradient(105deg, rgba(255,255,255,0) 0%, rgba(255,255,255,0.34) 50%, rgba(255,255,255,0) 100%)",
          transform: "skewX(-18deg)",
        }}
      />
    ) : null}
    <span style={{ position: "relative" }}>{label}</span>
  </div>
);

const ICONS: Record<string, React.ReactNode> = {
  offline: (
    <>
      <path d="M12 3l8 4v5c0 5-3.4 8.2-8 9-4.6-.8-8-4-8-9V7l8-4z" />
      <path d="M9 12l2.2 2.2L15.5 9.9" />
    </>
  ),
  free: (
    <>
      <circle cx="12" cy="12" r="8.6" />
      <path d="M9.2 8.6h4.4a2.2 2.2 0 1 1 0 4.4H9.2M9.2 8.6v10M7.4 15.4h5.2" />
    </>
  ),
  streak: (
    <>
      <path d="M13.2 2.8s.9 2.3-.7 4.6c-1.3 1.9-2.9 3-2.9 5a2.9 2.9 0 0 0 5.8.2c1.1 1 1.8 2.4 1.8 3.7A5.9 5.9 0 0 1 5.4 16c0-4.3 3.2-5.7 4.6-8.6.9-1.9.6-4 .6-4a7.6 7.6 0 0 1 2.6-.6z" />
    </>
  ),
  coach: (
    <>
      <path d="M4 6.5A2.5 2.5 0 0 1 6.5 4h11A2.5 2.5 0 0 1 20 6.5v7a2.5 2.5 0 0 1-2.5 2.5H12l-4.6 4v-4H6.5A2.5 2.5 0 0 1 4 13.5v-7z" />
      <path d="M8.4 9h7.2M8.4 12h4.6" />
    </>
  ),
};

export const ProofCard: React.FC<{
  icon: keyof typeof ICONS;
  title: string;
  sub: string;
  accent?: string;
  width?: number;
  progress?: number;
}> = ({ icon, title, sub, accent = COLORS.goldDeep, width = 384, progress = 1 }) => (
  <div
    style={{
      width,
      background: COLORS.card,
      borderRadius: 26,
      border: "1px solid rgba(232,169,42,0.34)",
      boxShadow: "0 18px 40px -22px rgba(26,26,26,0.35)",
      padding: "34px 32px 32px",
      opacity: progress,
      transform: `translateY(${(1 - progress) * 34}px) scale(${0.96 + progress * 0.04})`,
      position: "relative",
      overflow: "hidden",
    }}
  >
    <div
      style={{
        position: "absolute",
        inset: 0,
        background: "linear-gradient(180deg, rgba(255,255,255,0.65) 0%, rgba(255,255,255,0) 40%)",
      }}
    />
    <div style={{ position: "relative" }}>
      <div
        style={{
          width: 74,
          height: 74,
          borderRadius: 20,
          background: `${accent}14`,
          border: `1.5px solid ${accent}55`,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <svg
          width="40"
          height="40"
          viewBox="0 0 24 24"
          fill="none"
          stroke={accent}
          strokeWidth="1.7"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          {ICONS[icon]}
        </svg>
      </div>
      <div
        style={{
          marginTop: 24,
          fontFamily: FONT_STACK,
          fontSize: 33,
          fontWeight: 700,
          letterSpacing: -0.2,
          color: COLORS.ink,
        }}
      >
        {title}
      </div>
      <div
        style={{
          marginTop: 12,
          fontFamily: FONT_STACK,
          fontSize: 26,
          lineHeight: 1.42,
          color: COLORS.sub,
        }}
      >
        {sub}
      </div>
    </div>
  </div>
);

/* ────────────────────────────────────────────────────────────────────────── */
/* Receipt (essence scene metaphor)                                           */
/* ────────────────────────────────────────────────────────────────────────── */

export const Receipt: React.FC<{
  width?: number;
  stampAt?: number; // local frame the WANT stamp lands; -1 = never
  style?: React.CSSProperties;
}> = ({ width = 430, stampAt = -1, style }) => {
  const f = useCurrentFrame();
  const rows = [
    { item: "Rice (5kg)", cost: "280.00" },
    { item: "Instant coffee", cost: "115.00" },
    { item: "Milk tea (large)", cost: "120.00", flag: true },
    { item: "Dish soap", cost: "64.00" },
  ];
  return (
    <div
      style={{
        width,
        background: "#FFFEFB",
        borderRadius: 6,
        boxShadow: "0 26px 54px -26px rgba(26,26,26,0.4)",
        padding: "34px 34px 40px",
        fontFamily: FONT_STACK,
        position: "relative",
        ...style,
      }}
    >
      {/* zigzag edges */}
      <div
        style={{
          position: "absolute",
          left: 0,
          right: 0,
          top: -7,
          height: 8,
          background:
            "linear-gradient(135deg, #FFFEFB 5px, transparent 0) 0 0/14px 8px repeat-x, linear-gradient(-135deg, #FFFEFB 5px, transparent 0) 7px 0/14px 8px repeat-x",
        }}
      />
      <div
        style={{
          position: "absolute",
          left: 0,
          right: 0,
          bottom: -7,
          height: 8,
          background:
            "linear-gradient(45deg, #FFFEFB 5px, transparent 0) 0 0/14px 8px repeat-x, linear-gradient(-45deg, #FFFEFB 5px, transparent 0) 7px 0/14px 8px repeat-x",
        }}
      />
      <div style={{ textAlign: "center", fontSize: 24, letterSpacing: 4, color: COLORS.muted, fontWeight: 600 }}>
        GROCERY · AUG 12
      </div>
      <div style={{ margin: "20px 0 16px", borderTop: `2px dashed ${COLORS.divider}` }} />
      {rows.map((r) => (
        <div
          key={r.item}
          style={{
            display: "flex",
            justifyContent: "space-between",
            padding: "10px 0",
            fontSize: 28,
            color: COLORS.ink,
            fontVariantNumeric: "tabular-nums",
            position: "relative",
          }}
        >
          <span>{r.item}</span>
          <span style={{ fontWeight: 600 }}>{r.cost}</span>
          {r.flag && stampAt >= 0 ? (
            <div
              style={{
                position: "absolute",
                left: "50%",
                top: -10,
                transform: "translateX(-50%)",
              }}
            >
              <StampWord
                word="WANT"
                color={COLORS.crimson}
                at={stampAt}
                size={46}
                tilt={-8}
                ringScale={0.6}
              />
            </div>
          ) : null}
        </div>
      ))}
      <div style={{ margin: "16px 0 18px", borderTop: `2px dashed ${COLORS.divider}` }} />
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
          fontSize: 30,
          fontWeight: 700,
          color: COLORS.ink,
          fontVariantNumeric: "tabular-nums",
        }}
      >
        <span>TOTAL</span>
        <span>₱579.00</span>
      </div>
      {/* faint paper grain */}
      <div
        style={{
          position: "absolute",
          inset: 0,
          opacity: 0.05 + Math.sin(f * 0.03) * 0.008,
          background:
            "repeating-linear-gradient(0deg, rgba(26,26,26,0.05) 0 1px, transparent 1px 3px)",
          pointerEvents: "none",
        }}
      />
    </div>
  );
};

/* ────────────────────────────────────────────────────────────────────────── */
/* Close lockup (shared by all three cuts)                                    */
/* ────────────────────────────────────────────────────────────────────────── */

export const CloseLockup: React.FC<{
  sealAt: number;
  scale?: number;
  linkAt?: number;
  tagline?: string;
}> = ({ sealAt, scale = 1, linkAt, tagline = "Train your spending." }) => {
  const f = useCurrentFrame();
  const seal = prog(f, sealAt, sealAt + 14, EASE_SEAL);
  const title = prog(f, sealAt + 10, sealAt + 28);
  const tag = prog(f, sealAt + 22, sealAt + 40);
  const cta = prog(f, sealAt + 36, sealAt + 52);
  const link = prog(f, (linkAt ?? sealAt + 48), (linkAt ?? sealAt + 48) + 16);
  const pulse = 0.5 + Math.sin(f * 0.11) * 0.5;
  const sheen = interpolate(f, [sealAt + 58, sealAt + 96], [0, 1], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  return (
    <div
      style={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        transform: `scale(${scale})`,
        transformOrigin: "center top",
      }}
    >
      <SealBadge size={168} progress={seal} />
      <div
        style={{
          marginTop: 40,
          fontFamily: SERIF_STACK,
          fontSize: 92,
          fontWeight: 700,
          letterSpacing: 7,
          color: COLORS.ink,
          fontVariantCaps: "small-caps",
          opacity: title,
          transform: `translateY(${(1 - title) * 20}px)`,
          WebkitFontSmoothing: "antialiased",
          whiteSpace: "nowrap",
        }}
      >
        NEEDS VS WANTS
      </div>
      <div
        style={{
          marginTop: 16,
          fontFamily: SERIF_STACK,
          fontStyle: "italic",
          fontSize: 42,
          color: COLORS.goldDeep,
          letterSpacing: 1.6,
          opacity: tag,
          transform: `translateY(${(1 - tag) * 14}px)`,
        }}
      >
        {tagline}
      </div>
      <div style={{ marginTop: 52, opacity: cta, transform: `translateY(${(1 - cta) * 18}px)` }}>
        <CtaPill label="Get it free on Android" pulse={cta >= 1 ? pulse * 0.5 : 0} sheen={sheen} />
      </div>
      <div
        style={{
          marginTop: 30,
          fontFamily: FONT_STACK,
          fontSize: 30,
          fontWeight: 500,
          letterSpacing: 1.8,
          color: COLORS.sub,
          opacity: link,
        }}
      >
        needs-vs-wants.vercel.app
      </div>
    </div>
  );
};
