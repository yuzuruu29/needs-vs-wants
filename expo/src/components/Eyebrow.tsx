/** Eyebrow label — small-caps section header. Port of Eyebrow.swift. */
import { memo } from "react";
import { StyleSheet, Text } from "react-native";
import { useThemeColors } from "../design/theme";

export const Eyebrow = memo(function Eyebrow({ children }: { children: string }) {
  const colors = useThemeColors();
  return <Text style={[styles.text, { color: colors.textSecondary }]}>{children}</Text>;
});

const styles = StyleSheet.create({
  text: {
    fontSize: 11,
    fontWeight: "600",
    textTransform: "uppercase",
    letterSpacing: 0.8,
  },
});
