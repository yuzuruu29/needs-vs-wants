/**
 * Adaptive supermarket-premium palette (D7) — faithful port of AppColors.swift.
 *
 * Light values reproduce D7 exactly (crimson #C8102E, market green #0B6B3A,
 * gold #E8A92A, warm cream #FAFAF7). Dark values are tuned variants — same
 * brand identity, readable on black.
 */
import { useColorScheme } from "react-native";

export interface AppColors {
  surface: string;
  surfaceCard: string;
  surfaceRaised: string;
  surfaceSunken: string;
  crimson: string;
  crimsonDeep: string;
  marketGreen: string;
  gold: string;
  divider: string;
  textPrimary: string;
  textSecondary: string;
  // Semantic: Need = green "go", Want = red "stop"
  need: string;
  want: string;
  accent: string;
}

const LIGHT: AppColors = {
  surface: "#FAFAF7",
  surfaceCard: "#FFFFFF",
  surfaceRaised: "#F3F1EA",
  surfaceSunken: "#F7F4EC",
  crimson: "#C8102E",
  crimsonDeep: "#A40E25",
  marketGreen: "#0B6B3A",
  gold: "#E8A92A",
  divider: "#E8E5DC",
  textPrimary: "#1A1A1A",
  textSecondary: "#6B6B6B",
  need: "#0B6B3A",
  want: "#C8102E",
  accent: "#C8102E",
};

const DARK: AppColors = {
  surface: "#1A1A1A",
  surfaceCard: "#292929",
  surfaceRaised: "#333335",
  surfaceSunken: "#212122",
  crimson: "#E0384D",
  crimsonDeep: "#C72838",
  marketGreen: "#38B873",
  gold: "#E8BD54",
  divider: "#474747",
  textPrimary: "#F5F5F5",
  textSecondary: "#A0A0A0",
  need: "#38B873",
  want: "#E0384D",
  accent: "#E0384D",
};

export function useThemeColors(): AppColors {
  const scheme = useColorScheme();
  return scheme === "dark" ? DARK : LIGHT;
}

export { LIGHT, DARK };
