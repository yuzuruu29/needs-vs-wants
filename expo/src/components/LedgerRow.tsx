/** Ledger row — shared geometry across Log + History. Port of LedgerRow.swift. */
import { memo } from "react";
import { Pressable, StyleSheet, Text, View } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { type CurrencyCode, type Entry, entryType } from "../data/schema";
import { timeLabel } from "../data/StatsEngine";
import { useThemeColors } from "../design/theme";
import { CurrencyText } from "./CurrencyText";
import { NeedWantBadge } from "./NeedWantBadge";

interface Props {
  entry: Entry;
  currency: CurrencyCode;
  onDelete?: (id: string) => void;
}

export const LedgerRow = memo(function LedgerRow({ entry, currency, onDelete }: Props) {
  const colors = useThemeColors();
  return (
    <View style={styles.row} accessibilityRole="none">
      <Text style={[styles.time, { color: colors.textSecondary }]}>{timeLabel(entry.dateUtc)}</Text>
      <Text style={[styles.item, { color: colors.textPrimary }]} numberOfLines={1}>
        {entry.item}
      </Text>
      <View style={styles.costCol}>
        <CurrencyText cents={entry.costCents} currency={currency} small />
      </View>
      <View style={styles.typeCol}>
        <NeedWantBadge type={entryType(entry)} />
      </View>
      {onDelete ? (
        <Pressable
          onPress={() => onDelete(entry.id)}
          style={styles.deleteCol}
          accessibilityLabel={`Delete ${entry.item}`}
          accessibilityHint="Removes this entry from the diary"
        >
          <Ionicons name="trash-outline" size={16} color={`${colors.crimson}B3`} />
        </Pressable>
      ) : (
        <View style={styles.deleteCol} />
      )}
    </View>
  );
});

const styles = StyleSheet.create({
  row: {
    flexDirection: "row",
    alignItems: "center",
    paddingVertical: 8,
  },
  time: { width: 48, fontSize: 11, fontWeight: "500" },
  item: { flex: 1, fontSize: 13, paddingRight: 4 },
  costCol: { width: 88, alignItems: "flex-end" },
  typeCol: { width: 42, alignItems: "center" },
  deleteCol: { width: 32, alignItems: "center", justifyContent: "center" },
});
