/** Currency text — formats cents in the active currency. Port of CurrencyText.swift. */
import { memo } from "react";
import { StyleSheet, Text } from "react-native";
import { formatCents } from "../data/CurrencyFormatter";
import { type CurrencyCode } from "../data/schema";
import { useThemeColors } from "../design/theme";

interface Props {
  cents: number;
  currency: CurrencyCode;
  small?: boolean;
  color?: string;
}

export const CurrencyText = memo(function CurrencyText({ cents, currency, small, color }: Props) {
  const colors = useThemeColors();
  return (
    <Text style={[styles.text, small ? styles.small : styles.large, { color: color ?? colors.textPrimary }]}>
      {formatCents(cents, currency)}
    </Text>
  );
});

const styles = StyleSheet.create({
  text: { fontVariant: ["tabular-nums"], fontWeight: "600" },
  large: { fontSize: 17 },
  small: { fontSize: 14 },
});
