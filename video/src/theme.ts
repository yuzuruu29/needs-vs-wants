import { Easing } from "remotion";

/**
 * Needs vs Wants — "Supermarket premium" design tokens.
 * Mirrors app/src/main/java/com/needsvswants/app/ui/theme/Color.kt + Summary.md D7.
 */

export const COLORS = {
  background: "#FAFAF7", // warm off-white app background
  card: "#FFFFFF", // cards, primary surface
  raised: "#F3F1EA", // chips, raised surfaces
  sunken: "#F7F4EC", // inputs, sunken wells
  divider: "#E8E5DC", // hairline divider
  dividerStrong: "#D6D2C6",
  crimson: "#C8102E", // Puregold red — primary accent, Want
  crimsonDeep: "#A40E25",
  crimsonSoft: "#E25C6F",
  green: "#0B6B3A", // Robinsons green — secondary accent, Need
  greenDeep: "#084F2A",
  greenSoft: "#3E9D6E",
  gold: "#E8A92A", // warm gold — premium trim, totals
  goldSoft: "#F4C968",
  goldDeep: "#B9881E",
  danger: "#C8102E",
  ink: "#1A1A1A",
  sub: "#5A5A5A",
  muted: "#8A8A8A",
  phoneBezel: "#0C0D0F",
  skin: "#D8A075", // warm skin tone for the finger
  skinShade: "#C0845A",
} as const;

/** Power-ease used across the promo — known, editorial, never bouncy. */
export const EASE_EDIT = Easing.bezier(0.32, 0.72, 0.25, 1);
/** Softer standard ease. */
export const EASE_SOFT = Easing.bezier(0.4, 0, 0.2, 1);
/** Strong exit ease for seals/impacts. */
export const EASE_SEAL = Easing.bezier(0.16, 1, 0.3, 1);

/** Typography scale for the in-phone UI (physical px at 1440-wide canvas). */
export const UI = {
  eyebrow: 26, // uppercase micro heading with wide letter-spacing
  eyebrowLetter: 3,
  h1: 84, // LOG / NEEDS vs WANTS display
  h2: 46,
  body: 34,
  bodySm: 28,
  label: 30,
  ledger: 30,
  money: 34,
  moneyBig: 60,
  chip: 26,
} as const;

export const FONT_STACK =
  '"Inter", "Inter Fallback", "Helvetica Neue", Arial, sans-serif';
export const SERIF_STACK =
  '"Playfair Display SC", "Playfair Display SC Fallback", Georgia, "Times New Roman", serif';
