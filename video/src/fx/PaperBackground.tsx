import React from "react";
import { useCurrentFrame, interpolate } from "remotion";
import { COLORS } from "../theme";

const NOISE =
  "url(\"data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='160' height='160'%3E%3Cfilter id='n'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='0.9' numOctaves='2' stitchTiles='stitch'/%3E%3CfeColorMatrix type='saturate' values='0'/%3E%3C/filter%3E%3Crect width='100%25' height='100%25' filter='url(%23n)' opacity='0.5'/%3E%3C/svg%3E\")";

/**
 * Warm paper void: off-white base, soft daylight vignette toward the corners,
 * subtle paper grain that breathes. The phone floats on this.
 */
export const PaperBackground: React.FC<{ frame?: number }> = () => {
  const frame = useCurrentFrame();

  // Grain opacity breathes slowly (alive paper, never noisy)
  const grainOpacity = 0.042 + Math.sin(frame * 0.038) * 0.012;
  // Warm light drifts gently across the void
  const warmX = 28 + Math.sin(frame * 0.022) * 8;
  const warmY = 18 + Math.cos(frame * 0.018) * 6;
  // Vignette depth eases ever so slightly
  const vignette = 0.12 + Math.sin(frame * 0.015) * 0.018;

  // Soft bottom caption wash (stable, slightly deeper late in piece)
  const bottomWash = interpolate(frame, [0, 360, 449], [0.88, 0.9, 0.94], {
    extrapolateLeft: "clamp",
    extrapolateRight: "clamp",
  });

  return (
    <div
      style={{
        position: "absolute",
        inset: 0,
        background: `
          radial-gradient(120% 90% at 50% 42%, #FFFFFF 0%, ${COLORS.background} 46%, #F2EEE2 100%),
          radial-gradient(130% 110% at 50% 110%, rgba(79,70,49,${vignette}) 0%, rgba(79,70,49,0) 55%)
        `,
      }}
    >
      {/* paper grain — breathing multiply */}
      <div
        style={{
          position: "absolute",
          inset: 0,
          backgroundImage: NOISE,
          opacity: grainOpacity,
          mixBlendMode: "multiply",
        }}
      />
      {/* very soft warming drift so the void is alive but calm */}
      <div
        style={{
          position: "absolute",
          inset: 0,
          background: `radial-gradient(60% 50% at ${warmX}% ${warmY}%, rgba(232,169,42,0.055) 0%, rgba(232,169,42,0) 62%)`,
        }}
      />
      {/* secondary cool-shadow corner (daylight) */}
      <div
        style={{
          position: "absolute",
          inset: 0,
          background:
            "radial-gradient(70% 55% at 88% 12%, rgba(90,100,120,0.04) 0%, rgba(90,100,120,0) 55%)",
          pointerEvents: "none",
        }}
      />
      {/* bottom caption light */}
      <div
        style={{
          position: "absolute",
          left: 0,
          right: 0,
          bottom: 0,
          height: 560,
          background: `linear-gradient(180deg, rgba(250,250,247,0) 0%, rgba(250,250,247,${bottomWash}) 100%)`,
        }}
      />
    </div>
  );
};
