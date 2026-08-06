import React from "react";
import { useCurrentFrame } from "remotion";

/**
 * Camera stage for the phone. Everything inside uses canvas coordinates
 * (0..1440 x 0..2560); the stage scales/rotates/blurs the whole group around
 * `origin` (canvas px). Perspective is applied for rotateY shots.
 *
 * Idle micro-drift keeps the hero never static (editorial float).
 * Optional `float` multiplies the idle amplitude; `rackFocus` softens edges
 * during optical rack-focus (hook macro).
 */
export const PhoneStage: React.FC<{
  scale: number;
  originX: number;
  originY: number;
  rotateY?: number;
  rotateX?: number;
  blur?: number;
  z?: number;
  /** Multiplier for idle float (0 = static). Default 1. */
  float?: number;
  /** 0..1 optical rack-focus intensity — soft vignette + bokeh falloff feel. */
  rackFocus?: number;
  children: React.ReactNode;
}> = ({
  scale,
  originX,
  originY,
  rotateY = 0,
  rotateX = 0,
  blur = 0,
  z = 10,
  float = 1,
  rackFocus = 0,
  children,
}) => {
  const frame = useCurrentFrame();

  // Subtle idle float — never bouncy; slow sine drift
  const driftY = Math.sin(frame * 0.045) * 5.5 * float;
  const driftX = Math.sin(frame * 0.031 + 1.2) * 2.2 * float;
  const driftRot = Math.sin(frame * 0.028) * 0.35 * float;

  // Soft contact shadow breathes with float (phone lifts = lighter shadow)
  const shadowLift = 1 + Math.sin(frame * 0.045) * 0.06 * float;

  const filters: string[] = [];
  if (blur > 0.35) {
    // Optical bokeh: slightly softer than a hard Gaussian
    filters.push(`blur(${blur}px)`);
  }
  if (rackFocus > 0.02) {
    // Subtle brightness/contrast falloff to sell focal-plane shift
    const sat = 1 - rackFocus * 0.08;
    const bright = 1 + rackFocus * 0.04;
    filters.push(`saturate(${sat}) brightness(${bright})`);
  }

  return (
    <div
      style={{
        position: "absolute",
        inset: 0,
        perspective: 1800,
        perspectiveOrigin: `${originX}px ${originY}px`,
        zIndex: z,
      }}
    >
      {/* soft floor shadow under the phone — independent of bezel */}
      <div
        style={{
          position: "absolute",
          left: originX - 220,
          top: originY + 520 * scale * 0.55 + driftY,
          width: 440,
          height: 90,
          borderRadius: "50%",
          background:
            "radial-gradient(ellipse at center, rgba(40,32,18,0.22) 0%, rgba(40,32,18,0.08) 45%, rgba(40,32,18,0) 72%)",
          transform: `scale(${scale * shadowLift}, ${scale * 0.85})`,
          transformOrigin: "center center",
          pointerEvents: "none",
          zIndex: 0,
          filter: "blur(8px)",
        }}
      />

      <div
        style={{
          width: 1440,
          height: 2560,
          transformOrigin: `${originX}px ${originY}px`,
          transform: `translate(${driftX}px, ${driftY}px) scale(${scale}) rotateY(${rotateY + driftRot}deg) rotateX(${rotateX}deg)`,
          filter: filters.length ? filters.join(" ") : undefined,
          transformStyle: "preserve-3d",
          willChange: "transform, filter",
        }}
      >
        {children}
      </div>

      {/* optical rack-focus edge falloff (soft vignette on the stage) */}
      {rackFocus > 0.02 ? (
        <div
          style={{
            position: "absolute",
            inset: 0,
            pointerEvents: "none",
            background: `radial-gradient(ellipse 55% 48% at ${originX}px ${originY}px, rgba(250,250,247,0) 0%, rgba(250,250,247,${0.15 * rackFocus}) 55%, rgba(242,238,226,${0.55 * rackFocus}) 100%)`,
            zIndex: z + 1,
          }}
        />
      ) : null}
    </div>
  );
};
