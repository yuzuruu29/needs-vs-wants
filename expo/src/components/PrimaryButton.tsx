/** Primary CTA button — crimson fill, white label. Port of PrimaryButton.swift. */
import { memo } from "react";
import { Pressable, StyleSheet, Text } from "react-native";
import { useThemeColors } from "../design/theme";

interface Props {
  label: string;
  onPress: () => void;
  disabled?: boolean;
  destructive?: boolean;
}

export const PrimaryButton = memo(function PrimaryButton({ label, onPress, disabled, destructive }: Props) {
  const colors = useThemeColors();
  const bg = destructive ? colors.crimsonDeep : colors.accent;
  return (
    <Pressable
      onPress={onPress}
      disabled={disabled}
      style={({ pressed }) => [
        styles.button,
        { backgroundColor: disabled ? colors.divider : bg, opacity: pressed ? 0.85 : 1 },
      ]}
    >
      <Text style={[styles.label, { color: disabled ? colors.textSecondary : "#FFFFFF" }]}>{label}</Text>
    </Pressable>
  );
});

const styles = StyleSheet.create({
  button: {
    paddingVertical: 14,
    borderRadius: 12,
    alignItems: "center",
    justifyContent: "center",
  },
  label: {
    fontSize: 16,
    fontWeight: "700",
    letterSpacing: 0.3,
  },
});
