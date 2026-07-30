import { useState } from "react";
import { Alert, ScrollView, StyleSheet, Text, View } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { Ionicons } from "@expo/vector-icons";
import { CurrencyText } from "@/components/CurrencyText";
import { LedgerHeader } from "@/components/LedgerHeader";
import { LedgerRow } from "@/components/LedgerRow";
import { PrimaryButton } from "@/components/PrimaryButton";
import { dayTotals, formatDayHeader, groupedByDay } from "@/data/HistoryHelpers";
import { typography } from "@/design/typography";
import { useThemeColors } from "@/design/theme";
import * as Haptics from "@/design/Haptics";
import { useAppModel } from "@/state/AppModelContext";
import { useRepository } from "@/state/RepositoryContext";

export default function HistoryScreen() {
  const colors = useThemeColors();
  const { entries, currency, repo, refresh } = useRepository();
  const { switchToLog } = useAppModel();
  const sections = groupedByDay(entries);

  const confirmDelete = (id: string) => {
    const entry = entries.find((e) => e.id === id);
    Alert.alert("Delete entry?", entry ? `Remove ${entry.item}?` : "Remove this entry?", [
      { text: "Cancel", style: "cancel" },
      {
        text: "Delete",
        style: "destructive",
        onPress: async () => {
          if (repo) {
            await repo.delete(id);
            await Haptics.warn();
            await refresh();
          }
        },
      },
    ]);
  };

  return (
    <SafeAreaView style={[styles.safe, { backgroundColor: colors.surface }]} edges={["top"]}>
      <Text style={[styles.navTitle, { color: colors.textPrimary }]}>History</Text>
      <ScrollView contentContainerStyle={styles.scroll}>
        {entries.length === 0 ? (
          <View style={styles.empty}>
            <View style={[styles.emptyRing, { borderColor: colors.divider }]}>
              <Ionicons name="book-outline" size={32} color={colors.textSecondary} />
            </View>
            <Text style={[typography.displaySmall, { color: colors.textSecondary }]}>
              The page waits for ink.
            </Text>
            <View style={styles.emptyCta}>
              <PrimaryButton label="Log an expense" onPress={switchToLog} />
            </View>
          </View>
        ) : (
          sections.map(({ key, entries: dayEntries }) => {
            const totals = dayTotals(dayEntries);
            return (
              <View
                key={key}
                style={[
                  styles.section,
                  { backgroundColor: colors.surfaceCard, borderColor: colors.divider },
                ]}
              >
                <View style={styles.sectionHeader}>
                  <View>
                    <Text style={[styles.dayTitle, { color: colors.textPrimary }]}>
                      {formatDayHeader(key)}
                    </Text>
                    <Text style={[styles.dayMeta, { color: colors.textSecondary }]}>
                      {dayEntries.length} entries
                    </Text>
                  </View>
                  <View style={styles.dayTotals}>
                    {totals.needs > 0 && (
                      <CurrencyText cents={totals.needs} currency={currency} color={colors.need} small />
                    )}
                    {totals.wants > 0 && (
                      <CurrencyText cents={totals.wants} currency={currency} color={colors.want} small />
                    )}
                  </View>
                </View>
                <LedgerHeader />
                {dayEntries.map((entry) => (
                  <View key={entry.id}>
                    <LedgerRow entry={entry} currency={currency} onDelete={confirmDelete} />
                    <View style={[styles.divider, { backgroundColor: colors.divider }]} />
                  </View>
                ))}
              </View>
            );
          })
        )}
      </ScrollView>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1 },
  navTitle: {
    fontSize: 34,
    fontWeight: "700",
    paddingHorizontal: 20,
    paddingTop: 8,
    paddingBottom: 8,
  },
  scroll: { paddingHorizontal: 20, paddingBottom: 40, gap: 16 },
  empty: { alignItems: "center", minHeight: 400, justifyContent: "center", gap: 16 },
  emptyRing: {
    width: 100,
    height: 100,
    borderRadius: 50,
    borderWidth: 2,
    alignItems: "center",
    justifyContent: "center",
  },
  emptyCta: { width: "100%", paddingHorizontal: 40 },
  section: {
    borderRadius: 12,
    borderWidth: 1,
    padding: 16,
  },
  sectionHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginBottom: 8,
  },
  dayTitle: { fontSize: 15, fontWeight: "600" },
  dayMeta: { ...typography.caption, marginTop: 2 },
  dayTotals: { alignItems: "flex-end", gap: 2 },
  divider: { height: StyleSheet.hairlineWidth },
});
