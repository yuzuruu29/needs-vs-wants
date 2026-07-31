import { Alert, Pressable, ScrollView, StyleSheet, Text, View } from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { Ionicons } from "@expo/vector-icons";
import { CURRENCY_LIST, type CurrencyCode } from "@/data/schema";
import { typography } from "@/design/typography";
import { useThemeColors } from "@/design/theme";
import * as Haptics from "@/design/Haptics";
import { useAppModel } from "@/state/AppModelContext";
import { useRepository } from "@/state/RepositoryContext";

export default function SettingsScreen() {
  const colors = useThemeColors();
  const { currency, setCurrency, repo, refresh } = useRepository();
  const { openOnboarding } = useAppModel();

  const wipeDiary = () => {
    Alert.alert("Wipe entire diary?", "This deletes all logged expenses. This cannot be undone.", [
      { text: "Cancel", style: "cancel" },
      {
        text: "Wipe all",
        style: "destructive",
        onPress: async () => {
          if (repo) {
            await repo.deleteAll();
            await Haptics.warn();
            await refresh();
          }
        },
      },
    ]);
  };

  return (
    <SafeAreaView style={[styles.safe, { backgroundColor: colors.surface }]} edges={["top"]}>
      <Text style={[styles.navTitle, { color: colors.textPrimary }]}>Settings</Text>
      <ScrollView contentContainerStyle={styles.scroll}>
        <Section title="Currency">
          {CURRENCY_LIST.map((c) => (
            <CurrencyRow
              key={c.code}
              code={c.code}
              label={c.displayName}
              selected={currency === c.code}
              onSelect={() => void setCurrency(c.code)}
            />
          ))}
        </Section>

        <Section title="Data">
          <Pressable
            onPress={wipeDiary}
            style={({ pressed }) => [styles.destructiveRow, { opacity: pressed ? 0.7 : 1 }]}
            accessibilityHint="Deletes all entries permanently"
          >
            <Ionicons name="trash-outline" size={20} color={colors.crimson} />
            <Text style={[styles.destructiveLabel, { color: colors.crimson }]}>Wipe diary</Text>
          </Pressable>
        </Section>

        <Section title="About">
          <Row label="Version" value="1.0.0" />
          <Pressable
            onPress={openOnboarding}
            style={({ pressed }) => [styles.linkRow, { opacity: pressed ? 0.7 : 1 }]}
          >
            <Ionicons name="help-circle-outline" size={20} color={colors.accent} />
            <Text style={[styles.linkLabel, { color: colors.textPrimary }]}>How it works</Text>
          </Pressable>
        </Section>
      </ScrollView>
    </SafeAreaView>
  );
}

function Section({ title, children }: { title: string; children: React.ReactNode }) {
  const colors = useThemeColors();
  return (
    <View style={styles.section}>
      <Text style={[styles.sectionTitle, { color: colors.textSecondary }]}>{title.toUpperCase()}</Text>
      <View style={[styles.sectionBody, { backgroundColor: colors.surfaceCard, borderColor: colors.divider }]}>
        {children}
      </View>
    </View>
  );
}

function CurrencyRow({
  code,
  label,
  selected,
  onSelect,
}: {
  code: CurrencyCode;
  label: string;
  selected: boolean;
  onSelect: () => void;
}) {
  const colors = useThemeColors();
  return (
    <Pressable
      onPress={onSelect}
      style={({ pressed }) => [
        styles.currencyRow,
        { borderBottomColor: colors.divider, opacity: pressed ? 0.7 : 1 },
      ]}
    >
      <Text style={[styles.rowLabel, { color: colors.textPrimary }]}>{label}</Text>
      {selected && <Ionicons name="checkmark" size={20} color={colors.accent} />}
    </Pressable>
  );
}

function Row({ label, value }: { label: string; value: string }) {
  const colors = useThemeColors();
  return (
    <View style={[styles.row, { borderBottomColor: colors.divider }]}>
      <Text style={[styles.rowLabel, { color: colors.textPrimary }]}>{label}</Text>
      <Text style={[styles.rowValue, { color: colors.textSecondary }]}>{value}</Text>
    </View>
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
  scroll: { paddingHorizontal: 20, paddingBottom: 40, gap: 24 },
  section: { gap: 8 },
  sectionTitle: { fontSize: 13, fontWeight: "600", letterSpacing: 0.5, paddingLeft: 4 },
  sectionBody: { borderRadius: 12, borderWidth: 1, overflow: "hidden" },
  currencyRow: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    paddingVertical: 14,
    paddingHorizontal: 16,
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  row: {
    flexDirection: "row",
    justifyContent: "space-between",
    paddingVertical: 14,
    paddingHorizontal: 16,
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  rowLabel: { ...typography.body },
  rowValue: { ...typography.body },
  destructiveRow: {
    flexDirection: "row",
    alignItems: "center",
    gap: 10,
    paddingVertical: 14,
    paddingHorizontal: 16,
  },
  destructiveLabel: { fontSize: 17, fontWeight: "500" },
  linkRow: {
    flexDirection: "row",
    alignItems: "center",
    gap: 10,
    paddingVertical: 14,
    paddingHorizontal: 16,
  },
  linkLabel: { fontSize: 17 },
});
