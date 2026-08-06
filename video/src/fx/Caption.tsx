import React from "react";
import { COLORS, FONT_STACK } from "../theme";

/**
 * Lower-third editorial caption. Dry, honest, Apple-keynote quiet:
 * gold hairline above clean sans-serif text. Max 5 words per brief.
 */
export const Caption: React.FC<{
  text: string;
  opacity?: number; // 0..1
  rise?: number; // px translate-up while fading in
  y?: number;
  emphasis?: string; // substring to accent in gold
}> = ({ text, opacity = 1, rise = 0, y = 2060, emphasis }) => {
  const parts = emphasis ? text.split(emphasis) : null;
  return (
    <div
      style={{
        position: "absolute",
        left: 0,
        right: 0,
        top: y,
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        textAlign: "center",
        opacity,
        transform: `translateY(${rise}px)`,
        padding: "0 72px",
        zIndex: 20,
      }}
    >
      <div
        style={{
          width: 56,
          height: 2.5,
          background: COLORS.gold,
          borderRadius: 2,
          marginBottom: 22,
          boxShadow: "0 0 12px rgba(232,169,42,0.25)",
        }}
      />
      <div
        style={{
          fontFamily: FONT_STACK,
          fontSize: 60,
          fontWeight: 600,
          letterSpacing: -0.4,
          color: COLORS.ink,
          lineHeight: 1.2,
          fontVariantNumeric: "tabular-nums",
          WebkitFontSmoothing: "antialiased",
          textRendering: "optimizeLegibility",
        }}
      >
        {parts ? (
          <>
            {parts[0]}
            <span style={{ color: COLORS.goldDeep, fontStyle: "italic" }}>{emphasis}</span>
            {parts[1]}
          </>
        ) : (
          text
        )}
      </div>
    </div>
  );
};

/** Expanding tap ripple centred on a point. progress 0..1. */
export const Ripple: React.FC<{
  x: number;
  y: number;
  progress: number;
  color?: string;
}> = ({ x, y, progress, color = "rgba(26,26,26,0.22)" }) => {
  const p = Math.max(0, Math.min(1, progress));
  const size = 16 + p * 200;
  const ring = 10 + p * 170;
  const opacity = Math.max(0, 1 - p);
  return (
    <>
      {/* soft fill bloom */}
      <div
        style={{
          position: "absolute",
          left: x - size / 2,
          top: y - size / 2,
          width: size,
          height: size,
          borderRadius: size,
          background: color,
          opacity: opacity * 0.85,
          pointerEvents: "none",
          zIndex: 25,
        }}
      />
      {/* crisp highlight ring */}
      <div
        style={{
          position: "absolute",
          left: x - ring / 2,
          top: y - ring / 2,
          width: ring,
          height: ring,
          borderRadius: ring,
          border: `2px solid ${color}`,
          opacity: opacity * 0.7,
          pointerEvents: "none",
          zIndex: 26,
          boxSizing: "border-box",
        }}
      />
      {/* tiny specular flash at contact */}
      <div
        style={{
          position: "absolute",
          left: x - 8,
          top: y - 8,
          width: 16,
          height: 16,
          borderRadius: 16,
          background: "rgba(255,255,255,0.55)",
          opacity: Math.max(0, 1 - p * 2.2) * 0.7,
          pointerEvents: "none",
          zIndex: 27,
        }}
      />
    </>
  );
};
