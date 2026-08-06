import React from "react";
import { COLORS, FONT_STACK } from "../theme";
import { LAYOUT } from "../layout";

/**
 * The floating black smartphone. Renders the bezel, status bar and clips the
 * screen content. Positioned at LAYOUT.PHONE_LEFT / PHONE_TOP.
 */
export const PhoneFrame: React.FC<{
  children: React.ReactNode;
  reflect?: boolean;
}> = ({ children, reflect = true }) => {
  const { PHONE_W, PHONE_H, PHONE_TOP, PHONE_LEFT, PHONE_RADIUS, BEZEL_X, BEZEL_TOP, BEZEL_BOTTOM, STATUS_H } = LAYOUT;
  return (
    <div
      style={{
        position: "absolute",
        left: PHONE_LEFT,
        top: PHONE_TOP,
        width: PHONE_W,
        height: PHONE_H,
        borderRadius: PHONE_RADIUS,
        background: COLORS.phoneBezel,
        boxShadow: [
          "0 48px 100px -36px rgba(26,26,26,0.48)",
          "0 18px 40px -20px rgba(40,32,18,0.22)",
          "0 0 0 1px rgba(255,255,255,0.07) inset",
          "0 1px 0 0 rgba(255,255,255,0.08) inset",
        ].join(", "),
        overflow: "hidden",
      }}
    >
      {/* screen, inset inside the bezel */}
      <div
        style={{
          position: "absolute",
          left: BEZEL_X,
          top: BEZEL_TOP,
          width: PHONE_W - BEZEL_X * 2,
          height: PHONE_H - BEZEL_TOP - BEZEL_BOTTOM,
          borderRadius: PHONE_RADIUS - 10,
          overflow: "hidden",
          background: COLORS.background,
        }}
      >
        {/* status bar */}
        <div
          style={{
            position: "absolute",
            top: 0,
            left: 0,
            right: 0,
            height: STATUS_H,
            display: "flex",
            alignItems: "center",
            justifyContent: "space-between",
            padding: "0 34px",
            fontFamily: FONT_STACK,
            fontSize: 30,
            fontWeight: 600,
            color: COLORS.ink,
            zIndex: 5,
          }}
        >
          <span>9:41</span>
          <span
            style={{
              width: 120,
              height: 26,
              borderRadius: 13,
              background: COLORS.ink,
              opacity: 0.9,
            }}
          />
          <span>100%</span>
        </div>

        {/* screen content — starts below the status bar */}
        <div
          style={{
            position: "absolute",
            left: 0,
            top: STATUS_H,
            width: PHONE_W - BEZEL_X * 2,
            height: PHONE_H - BEZEL_TOP - BEZEL_BOTTOM - STATUS_H,
          }}
        >
          {children}
        </div>

        {/* faint warm reflection */}
        {reflect ? (
          <div
            style={{
              position: "absolute",
              inset: 0,
              pointerEvents: "none",
              background:
                "linear-gradient(115deg, rgba(255,255,255,0.10) 0%, rgba(255,255,255,0) 30%, rgba(255,255,255,0) 55%, rgba(255,255,255,0.04) 100%)",
              zIndex: 6,
            }}
          />
        ) : null}
      </div>
    </div>
  );
};