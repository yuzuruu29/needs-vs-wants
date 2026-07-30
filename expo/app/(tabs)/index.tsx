import { useMemo, useState } from "react";
import { Pressable, ScrollView, StyleSheet, Text, View } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { Ionicons } from "@expo/vector-icons";
import { DonutChart } from "@/components/DonutChart";
import { CurrencyText } from "@/components/CurrencyText";
import { Eyebrow } from "@/components/Eyebrow";
import { PrimaryButton } from "@/components/PrimaryButton";
import { ShareBar } from "@/components/ShareBar";
import { needPct, PERIOD_LABELS, PERIOD_LIST, totalCents, type Period } from "@/data/schema";
import { StatsEngine } from "@/data/StatsEngine";
import { typography } from "@/design/typography";
import { useThemeColors } from "@/design/theme";
import { useAppModel } from "@/state/AppModelContext";
import { useRepository } from "@/state/RepositoryContext";

export default function SummaryScreen() {
  const colors = useThemeColors();
  const { currency, entries } = useRepository();
  const { switchToLog, openOnboarding } = useAppModel();
  const [period, setPeriod] = useState<Period>("day");

  const engine = useMemo(() => new StatsEngine(), []);
  const stats = useMemo(() => engine.stats(period, entries), [engine, period, entries]);
  const rangeCaption = useMemo(() => engine.rangeCaption(period), [engine, period]);
  const total = totalCents(stats);

  return (
    <SafeAreaView style={[styles.safe, { backgroundColor: colors.surface }]} edges={["top"]}>
      <ScrollView contentContainerStyle={styles.scroll}>
        <View style={styles.toolbar}>
          <View style={{ flex: 1 }} />
          <Pressable
            onPress={openOnboarding}
            accessibilityLabel="How it works"
            hitSlop={12}
            style={styles.helpBtn}
          >
            <Ionicons name="help-circle-outline" size={24} color={colors.textSecondary} />
          </Pressable>
        </View>

        <View style={styles.header}>
          <View style={styles.titleRow}>
            <Text style={[typography.displayMedium, { color: colors.textPrimary }]}>NEEDS</Text>
            <Text style={[typography.displayVs, { color: colors.textSecondary }]}>vs</Text>
            <Text style={[typography.displayMedium, { color: colors.crimson }]}>WANTS</Text>
          </View>
          <Text style={[styles.subtitle, { color: colors.textSecondary }]}>Expense Tracker</Text>
        </View>

        <View style={styles.periodBlock}>
          <View style={[styles.segmented, { backgroundColor: colors.surfaceRaised }]}>
            {PERIOD_LIST.map((p) => {
              const active = p === period;
              return (
                <Pressable
                  key={p}
                  onPress={() => setPeriod(p)}
                  style={[
                    styles.segment,
                    active && { backgroundColor: colors.surfaceCard },
                  ]}
                >
                  <Text
                    style={[
                      styles.segmentLabel,
                      { color: active ? colors.textPrimary : colors.textSecondary },
                    ]}
                  >
                    {PERIOD_LABELS[p]}
                  </Text>
                </Pressable>
              );
            })}
          </View>
          <Text style={[styles.rangeCaption, { color: colors.textSecondary }]}>{rangeCaption}</Text>
        </View>

        <View style={styles.donutWrap}>
          {total > 0 ? (
            <DonutChart stats={stats} currency={currency} size={180} />
          ) : (
            <View style={[styles.emptyDonut, { borderColor: colors.divider }]}>
              <Eyebrow>TOTAL</Eyebrow>
              <Text style={[styles.emptyDonutText, { color: colors.textSecondary }]}>
                Log your first expense to start the diary.
              </Text>
            </View>
          )}
        </View>

        <View style={styles.statRow}>
          <StatCard
            title="NEEDS"
            cents={stats.needsTotalCents}
            color={colors.need}
            count={stats.needsCount}
            currency={currency}
            stats={stats}
          />
          <StatCard
            title="WANTS"
            cents={stats.wantsTotalCents}
            color={colors.want}
            count={stats.wantsCount}
            currency={currency}
            stats={stats}
          />
          <StatCard
            title="NEED %"
            pct={Math.round(needPct(stats) * 100)}
            color={colors.accent}
            stats={stats}
          />
        </View>

        <PrimaryButton label="Log an expense" onPress={switchToLog} />
      </ScrollView>
    </SafeAreaView>
  );
}

function StatCard({
  title,
  cents,
  pct,
  color,
  count,
  currency,
  stats,
}: {
  title: string;
  cents?: number;
  pct?: number;
  color: string;
  count?: number;
  currency?: import("@/data/schema").CurrencyCode;
  stats: import("@/data/schema").SummaryStats;
}) {
  const colors = useThemeColors();
  return (
    <View
      style={[
        styles.statCard,
        { backgroundColor: colors.surfaceCard, borderColor: colors.divider },
      ]}
    >
      <Eyebrow>{title}</Eyebrow>
      {pct != null ? (
        <Text style={[styles.statMoney, { color }]}>{pct}%</Text>
      ) : (
        currency != null &&
        cents != null && <CurrencyText cents={cents} currency={currency} color={color} />
      )}
      {count != null && (
        <Text style={[styles.statCount, { color: colors.textSecondary }]}>{count} entries</Text>
      )}
      <ShareBar stats={stats} />
    </View>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1 },
  scroll: { paddingHorizontal: 20, paddingBottom: 40 },
  toolbar: { flexDirection: "row", alignItems: "center", paddingTop: 4 },
  helpBtn: { padding: 4 },
  header: { alignItems: "center", marginTop: 4, marginBottom: 24 },
  titleRow: { flexDirection: "row", alignItems: "baseline", gap: 8 },
  subtitle: { ...typography.body, marginTop: 4 },
  periodBlock: { marginBottom: 24, gap: 6 },
  segmented: { flexDirection: "row", borderRadius: 8, padding: 2 },
  segment: { flex: 1, paddingVertical: 8, borderRadius: 6, alignItems: "center" },
  segmentLabel: { fontSize: 13, fontWeight: "600" },
  rangeCaption: { ...typography.caption, textAlign: "center" },
  donutWrap: { alignItems: "center", marginBottom: 24 },
  emptyDonut: {
    width: 180,
    height: 180,
    borderRadius: 90,
    borderWidth: 2,
    alignItems: "center",
    justifyContent: "center",
    paddingHorizontal: 24,
    gap: 8,
  },
  emptyDonutText: { ...typography.caption, textAlign: "center" },
  statRow: { flexDirection: "row", gap: 12, marginBottom: 20 },
  statCard: {
    flex: 1,
    borderRadius: 12,
    borderWidth: 1,
    paddingVertical: 14,
    paddingHorizontal: 10,
    alignItems: "center",
    gap: 8,
  },
  statMoney: { fontSize: 22, fontWeight: "700", fontVariant: ["tabular-nums"] },
  statCount: { ...typography.caption },
});
