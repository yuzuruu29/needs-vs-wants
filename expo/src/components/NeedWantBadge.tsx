/** Need/Want badge — colored pill. Port of NeedWantBadge.swift. */
import { memo } from "react";
import { StyleSheet, Text, View } from "react-native";
import { type EntryType } from "../data/schema";
import { useThemeColors } from "../design/theme";

export const NeedWantBadge = memo(function NeedWantBadge({ type }: { type: EntryType }) {
  const colors = useThemeColors();
  const isNeed = type === "NEED";
  const bg = isNeed ? colors.need : colors.want;
  return (
    <View style={[styles.badge, { backgroundColor: bg }]}>
      <Text style={styles.label}>{isNeed ? "N" : "W"}</Text>
    </View>
  );
});

const styles = StyleSheet.create({
  badge: {
    width: 28,
    height: 22,
    borderRadius: 6,
    alignItems: "center",
    justifyContent: "center",
  },
  label: {
    color: "#FFFFFF",
    fontSize: 10,
    fontWeight: "700",
  },
});
