import React from "react";
import { COLORS, FONT_STACK, SERIF_STACK } from "../theme";
export const SERIF_FONT = SERIF_STACK;

/**
 * UI primitives mirroring app/src/main/java/.../ui/theme/Components.kt.
 * All units are physical px on the 1440-wide canvas.
 */

export const fontFamily = FONT_STACK;
export const serifFamily = SERIF_STACK;

export const Eyebrow: React.FC<
  React.PropsWithChildren<{
    text?: string;
    color?: string;
    size?: number;
    letterSpacing?: number;
    align?: React.CSSProperties["textAlign"];
  }>
> = ({ text, color = COLORS.crimson, size = 26, letterSpacing = 3, align = "left", children }) => {
  return (
    <div
      style={{
        fontFamily: FONT_STACK,
        fontSize: size,
        fontWeight: 600,
        letterSpacing,
        color,
        textTransform: "uppercase",
        textAlign: align,
        lineHeight: 1.2,
      }}
    >
      {text ?? children}
    </div>
  );
};

export const GiltRule: React.FC<{ width?: number; height?: number; color?: string }> = ({
  width = 46,
  height = 2.5,
  color = COLORS.gold,
}) => {
  return <div style={{ width, height, background: color, borderRadius: 2 }} />;
};

export const PremiumSurface: React.FC<
  React.PropsWithChildren<{
    raised?: boolean;
    goldEdge?: boolean;
    radius?: number;
    style?: React.CSSProperties;
  }>
> = ({ children, raised = true, goldEdge = true, radius = 26, style }) => {
  return (
    <div
      style={{
        position: "relative",
        width: "100%",
        background: raised ? COLORS.card : COLORS.sunken,
        borderRadius: radius,
        border: `1px solid ${goldEdge ? "rgba(232,169,42,0.32)" : COLORS.divider}`,
        boxShadow: raised ? "0 12px 26px -18px rgba(26,26,26,0.35)" : undefined,
        overflow: "hidden",
        ...style,
      }}
    >
      {raised ? (
        <div
          style={{
            position: "absolute",
            inset: 0,
            background:
              "linear-gradient(180deg, rgba(255,255,255,0.6) 0%, rgba(255,255,255,0) 42%)",
            pointerEvents: "none",
          }}
        />
      ) : null}
      <div style={{ position: "relative" }}>{children}</div>
    </div>
  );
};

export const GiltButton: React.FC<{
  label: string;
  height?: number;
  fontSize?: number;
  pressed?: boolean;
  style?: React.CSSProperties;
}> = ({ label, height = 64, fontSize = 30, pressed = false, style }) => {
  return (
    <div
      style={{
        height,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        background: COLORS.crimson,
        color: COLORS.card,
        borderRadius: 20,
        fontFamily: FONT_STACK,
        fontWeight: 600,
        fontSize,
        letterSpacing: 2,
        textTransform: "uppercase",
        boxShadow: pressed ? "0 4px 10px -6px rgba(200,16,46,0.6)" : "0 12px 24px -14px rgba(200,16,46,0.55)",
        transform: pressed ? "scale(0.975)" : "scale(1)",
        ...style,
      }}
    >
      {label}
    </div>
  );
};

export const TypeChip: React.FC<{
  label: string;
  color: string;
  selected: boolean;
  pressed?: boolean;
  width?: number;
  fontSize?: number;
}> = ({ label, color, selected, pressed = false, width = 148, fontSize = 30 }) => {
  return (
    <div
      style={{
        width,
        height: 62,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        borderRadius: 15,
        background: selected ? `${color}2E` : "transparent",
        border: `2px solid ${selected ? color : COLORS.dividerStrong}`,
        color: selected ? color : COLORS.sub,
        fontFamily: FONT_STACK,
        fontWeight: selected ? 600 : 500,
        fontSize,
        letterSpacing: 1.5,
        transform: pressed ? "scale(0.95)" : selected ? "scale(1.04)" : "scale(1)",
      }}
    >
      {label}
    </div>
  );
};

export const LedgerField: React.FC<{
  label: string;
  value: string;
  focused?: boolean;
  error?: boolean;
  height?: number;
}> = ({ label, value, focused = false, error = false, height = 66 }) => {
  const ruleColor = error ? COLORS.crimson : focused ? COLORS.gold : COLORS.dividerStrong;
  return (
    <div style={{ width: "100%" }}>
      <div
        style={{
          fontFamily: FONT_STACK,
          fontSize: 24,
          fontWeight: 600,
          letterSpacing: 2,
          color: error ? COLORS.crimson : focused ? COLORS.crimson : COLORS.muted,
        }}
      >
        {label.toUpperCase()}
      </div>
      <div
        style={{
          marginTop: 7,
          height,
          background: COLORS.sunken,
          borderTopLeftRadius: 12,
          borderTopRightRadius: 12,
          display: "flex",
          alignItems: "center",
          padding: "0 18px",
          fontFamily: FONT_STACK,
          fontSize: 34,
          color: value ? COLORS.ink : "rgba(26,26,26,0.42)",
          fontVariantNumeric: "tabular-nums",
        }}
      >
        {value || "Enter " + label.toLowerCase()}
      </div>
      <div style={{ height: 3, background: ruleColor, marginTop: 0 }} />
    </div>
  );
};

export const PremiumDialog: React.FC<
  React.PropsWithChildren<{
    eyebrow: string;
    eyebrowColor?: string;
    title: string;
    buttons: Array<{ label: string; danger?: boolean; ghost?: boolean }>;
    width?: number;
    anim?: { opacity: number; scale: number };
  }>
> = ({ eyebrow, eyebrowColor = COLORS.crimson, title, buttons, width = 470, anim, children }) => {
  const cardOpacity = anim?.opacity ?? 1;
  const cardScale = anim?.scale ?? 1;
  return (
    <div
      style={{
        position: "absolute",
        inset: 0,
        display: "flex",
        alignItems: "center",
        justifyContent: "center",
        background: `rgba(26,26,26,${0.26 * cardOpacity})`,
        zIndex: 10,
        opacity: cardOpacity,
      }}
    >
      <div
        style={{
          width,
          background: COLORS.card,
          borderRadius: 30,
          border: `1.5px solid rgba(232,169,42,0.4)`,
          boxShadow: "0 40px 70px -30px rgba(26,26,26,0.5)",
          padding: "34px 32px 26px",
          fontFamily: FONT_STACK,
          transform: `scale(${cardScale}) translateY(${(1 - cardOpacity) * -12}px)`,
        }}
      >
        <Eyebrow text={eyebrow} color={eyebrowColor} size={24} />
        <div
          style={{
            marginTop: 12,
            fontSize: 46,
            fontWeight: 700,
            fontFamily: SERIF_FONT,
            color: COLORS.ink,
            lineHeight: 1.1,
          }}
        >
          {title}
        </div>
        <GiltRule width={40} />
        <div style={{ marginTop: 18 }}>{children}</div>
        <div style={{ marginTop: 26, display: "flex", gap: 10 }}>
          {buttons.map((b) =>
            b.ghost ? (
              <div
                key={b.label}
                style={{
                  flex: 1,
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  height: 58,
                  color: COLORS.muted,
                  fontSize: 28,
                  fontWeight: 500,
                }}
              >
                {b.label}
              </div>
            ) : (
              <div
                key={b.label}
                style={{
                  flex: 1,
                  display: "flex",
                  alignItems: "center",
                  justifyContent: "center",
                  height: 58,
                  borderRadius: 16,
                  background: b.danger ? "rgba(200,16,46,0.16)" : COLORS.crimson,
                  color: b.danger ? COLORS.crimson : COLORS.card,
                  border: b.danger ? `2px solid ${COLORS.crimson}` : "none",
                  fontSize: 28,
                  fontWeight: 600,
                }}
              >
                {b.label}
              </div>
            )
          )}
        </div>
      </div>
    </div>
  );
};
