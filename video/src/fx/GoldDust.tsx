import React from "react";

const seed = (i: number) => {
  const x = Math.sin(i * 127.1 + 311.7) * 43758.5453;
  return x - Math.floor(x);
};

/**
 * Tiny gold dust motes drifting upward with a soft sine drift. Deterministic
 * per mote index so frames are stable.
 */
export const GoldDust: React.FC<{
  frame: number;
  count?: number;
  area: { x: number; y: number; w: number; h: number };
  opacity?: number;
  scale?: number;
}> = ({ frame, count = 16, area, opacity = 1, scale = 1 }) => {
  const motes = Array.from({ length: count }, (_, i) => {
    const s1 = seed(i);
    const s2 = seed(i + 97);
    const s3 = seed(i + 199);
    const speed = 0.22 + s1 * 0.45; // px per frame
    const total = area.h + 80;
    const travel = (frame * speed + s2 * total) % total;
    const y = area.y + area.h - travel;
    const x = area.x + s2 * area.w + Math.sin(frame * 0.018 + s3 * 6.28) * 42;
    const tw = Math.sin(frame * 0.09 + s3 * 6.28) * 0.5 + 0.5; // 0..1
    const motOpacity = (0.18 + tw * 0.42) * opacity;
    const size = (6 + s1 * 12) * scale;
    return { x, y, size, motOpacity };
  });

  return (
    <div style={{ position: "absolute", inset: 0, pointerEvents: "none", overflow: "hidden" }}>
      {motes.map((m, i) => (
        <div
          key={i}
          style={{
            position: "absolute",
            left: m.x,
            top: m.y,
            width: m.size,
            height: m.size,
            borderRadius: m.size,
            background: `radial-gradient(circle, rgba(244,201,104,0.95) 0%, rgba(232,169,42,0.55) 45%, rgba(232,169,42,0) 72%)`,
            opacity: m.motOpacity,
            filter: "blur(0.5px)",
          }}
        />
      ))}
    </div>
  );
};