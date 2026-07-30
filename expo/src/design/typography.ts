/**
 * Typography tokens — port of AppTypography.swift.
 * Playfair Display SC for display; system for body/UI.
 */
import { TextStyle } from "react-native";

export const FONT_DISPLAY_BOLD = "PlayfairDisplaySC-Bold";
export const FONT_DISPLAY_REGULAR = "PlayfairDisplaySC-Regular";

export const typography = {
  displayLarge: {
    fontFamily: FONT_DISPLAY_BOLD,
    fontSize: 34,
  } satisfies TextStyle,
  displayMedium: {
    fontFamily: FONT_DISPLAY_BOLD,
    fontSize: 22,
  } satisfies TextStyle,
  displaySmall: {
    fontFamily: FONT_DISPLAY_REGULAR,
    fontSize: 17,
  } satisfies TextStyle,
  displayVs: {
    fontFamily: FONT_DISPLAY_REGULAR,
    fontSize: 18,
  } satisfies TextStyle,
  body: {
    fontSize: 17,
  } satisfies TextStyle,
  caption: {
    fontSize: 12,
  } satisfies TextStyle,
};
