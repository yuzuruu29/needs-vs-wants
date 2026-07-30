/** Hairline need/want ratio bar for stat cards. Port of ShareBar.swift. */
import { memo } from "react";
import { StyleSheet, View } from "react-native";
import { needPct, wantPct, type SummaryStats, totalCents } from "../data/schema";
import { useThemeColors } from "../design/theme";

interface Props {
  stats: SummaryStats;
}

export const ShareBar = memo(function ShareBar({ stats }: Props) {
  const colors = useThemeColors();
  const total = totalCents(stats);
  const needFraction = total > 0 ? needPct(stats) : 0;

  return (
    <View
      style={[styles.track, { backgroundColor: `${colors.want}40` }]}
      accessibilityLabel={`Need ${Math.round(needPct(stats) * 100)} percent, want ${Math.round(wantPct(stats) * 100)} percent`}
    >
      {needFraction > 0 && (
        <View style={[styles.need, { backgroundColor: colors.need, flex: needFraction }]} />
      )}
      {needFraction < 1 && <View style={{ flex: 1 - needFraction }} />}
    </View>
  );
});

const styles = StyleSheet.create({
  track: {
    flexDirection: "row",
    height: 4,
    borderRadius: 2,
    overflow: "hidden",
    width: "100%",
    marginTop: 4,
  },
  need: {
    borderRadius: 2,
  },
});
