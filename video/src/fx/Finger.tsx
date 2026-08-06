import React from "react";
import { COLORS } from "../theme";

/**
 * A drawn index finger (no faces, no bodies — per brief). Tip lands at
 * (tipX, tipY); `angle` tilts it around the tip (negative = lean left).
 * `press` (0..1) compresses the finger and darkens the fingertip shadow.
 * `motionBlur` adds a soft directional blur for fast taps.
 */
export const Finger: React.FC<{
  tipX: number;
  tipY: number;
  angle?: number;
  press?: number;
  scale?: number;
  opacity?: number;
  /** 0..1 soft motion blur on fast moves */
  motionBlur?: number;
}> = ({
  tipX,
  tipY,
  angle = -18,
  press = 0,
  scale = 1,
  opacity = 1,
  motionBlur = 0,
}) => {
  const tip = { x: 70, y: 328 };
  const pressY = press * 9;
  const pressScaleY = 1 - press * 0.04;
  const blurPx = motionBlur > 0.05 ? motionBlur * 4.5 : 0;

  return (
    <div
      style={{
        position: "absolute",
        left: tipX - tip.x * scale,
        top: tipY - tip.y * scale,
        width: 140 * scale,
        height: 340 * scale,
        pointerEvents: "none",
        zIndex: 30,
        opacity,
        transform: `scale(${scale}) translateY(${pressY}px) scaleY(${pressScaleY})`,
        filter: blurPx > 0.3 ? `blur(${blurPx}px)` : undefined,
      }}
    >
      <svg
        width={140}
        height={340}
        viewBox="0 0 140 340"
        style={{
          transform: `rotate(${angle}deg)`,
          transformOrigin: "70px 328px",
          overflow: "visible",
        }}
      >
        <defs>
          <linearGradient id="nvwSkin" x1="0" y1="0" x2="1" y2="0">
            <stop offset="0" stopColor="#E8B994" />
            <stop offset="0.22" stopColor="#DDB088" />
            <stop offset="0.5" stopColor={COLORS.skin} />
            <stop offset="0.78" stopColor="#C48B5C" />
            <stop offset="1" stopColor={COLORS.skinShade} />
          </linearGradient>
          <linearGradient id="nvwSkinTip" x1="0.5" y1="0" x2="0.5" y2="1">
            <stop offset="0" stopColor="#E4B08A" />
            <stop offset="1" stopColor="#C8895A" />
          </linearGradient>
          <radialGradient id="nvwTipShadow" cx="0.5" cy="0.5" r="0.55">
            <stop offset="0" stopColor="rgba(26,26,26,0.55)" />
            <stop offset="0.55" stopColor="rgba(26,26,26,0.18)" />
            <stop offset="1" stopColor="rgba(26,26,26,0)" />
          </radialGradient>
          {/* soft ambient occlusion under the nail bed */}
          <linearGradient id="nvwKnuckleShade" x1="0" y1="0" x2="0" y2="1">
            <stop offset="0" stopColor="rgba(120,70,40,0)" />
            <stop offset="1" stopColor="rgba(120,70,40,0.12)" />
          </linearGradient>
        </defs>

        {/* fingertip contact shadow; darkens + spreads slightly on press */}
        <ellipse
          cx={70}
          cy={332}
          rx={38 + press * 10}
          ry={12 + press * 5}
          fill="url(#nvwTipShadow)"
          opacity={0.32 + press * 0.52}
        />

        {/* tapered finger body — wider at base, narrower at tip */}
        <path
          d="M70 22
             C 92 22 100 38 100 68
             L 104 248
             C 104 278 92 302 70 308
             C 48 302 36 278 36 248
             L 40 68
             C 40 38 48 22 70 22 Z"
          fill="url(#nvwSkin)"
          stroke="#A9744D"
          strokeWidth="1.4"
          strokeOpacity="0.32"
        />

        {/* volume highlight along the left edge */}
        <path
          d="M62 30 C 48 38 44 60 44 90 L 40 240 C 40 260 48 280 58 292"
          fill="none"
          stroke="rgba(255,236,214,0.55)"
          strokeWidth="9"
          strokeLinecap="round"
          opacity="0.48"
        />

        {/* right-side shade for roundness */}
        <path
          d="M92 40 C 98 50 100 70 100 100 L 103 240 C 103 265 96 285 86 298"
          fill="none"
          stroke="rgba(90,50,25,0.18)"
          strokeWidth="10"
          strokeLinecap="round"
          opacity="0.55"
        />

        {/* knuckle creases */}
        <path
          d="M48 118 C 60 128 80 128 92 118"
          fill="none"
          stroke="rgba(120,70,40,0.32)"
          strokeWidth="2.6"
          strokeLinecap="round"
        />
        <path
          d="M46 168 C 60 178 80 178 94 168"
          fill="none"
          stroke="rgba(120,70,40,0.28)"
          strokeWidth="2.6"
          strokeLinecap="round"
        />
        <path
          d="M48 218 C 60 226 80 226 92 218"
          fill="none"
          stroke="rgba(120,70,40,0.22)"
          strokeWidth="2.2"
          strokeLinecap="round"
        />

        {/* fingertip pad */}
        <ellipse
          cx={70}
          cy={300}
          rx={28}
          ry={22}
          fill="url(#nvwSkinTip)"
          opacity={0.55}
        />

        {/* nail — slightly tapered, glossy */}
        <ellipse
          cx={70}
          cy={268}
          rx={20}
          ry={16}
          fill="#F7E5D4"
          stroke="rgba(150,90,55,0.38)"
          strokeWidth="1.4"
        />
        <path
          d="M70 254 C 78 254 85 258 87 265 C 83 261 77 258 70 258 Z"
          fill="rgba(255,255,255,0.88)"
        />
        {/* nail cuticle line */}
        <path
          d="M54 278 C 62 284 78 284 86 278"
          fill="none"
          stroke="rgba(150,90,55,0.25)"
          strokeWidth="1.5"
          strokeLinecap="round"
        />
      </svg>
    </div>
  );
};
