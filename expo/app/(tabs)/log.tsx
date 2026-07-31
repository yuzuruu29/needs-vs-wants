import {
  Alert,
  KeyboardAvoidingView,
  Platform,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { Ionicons } from "@expo/vector-icons";
import { Eyebrow } from "@/components/Eyebrow";
import { LedgerHeader } from "@/components/LedgerHeader";
import { LedgerRow } from "@/components/LedgerRow";
import { PrimaryButton } from "@/components/PrimaryButton";
import { typography } from "@/design/typography";
import { useThemeColors } from "@/design/theme";
import * as Haptics from "@/design/Haptics";
import { useRepository } from "@/state/RepositoryContext";
import { todayCaption, useLogForm } from "@/state/useLogForm";

export default function LogScreen() {
  const colors = useThemeColors();
  const { currency, entries, repo, refresh } = useRepository();
  const form = useLogForm();

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
      <KeyboardAvoidingView
        style={styles.flex}
        behavior={Platform.OS === "ios" ? "padding" : undefined}
      >
        <ScrollView contentContainerStyle={styles.scroll} keyboardShouldPersistTaps="handled">
          <View style={styles.header}>
            <View style={styles.headerRow}>
              <Eyebrow>{`TODAY · ${todayCaption()}`}</Eyebrow>
              <Eyebrow>{`SHEET ${form.sheetCount} / 20`}</Eyebrow>
            </View>
            <Text style={[typography.displayMedium, { color: colors.textPrimary }]}>LOG</Text>
          </View>

          {form.isSheetFull ? (
            <View
              style={[
                styles.sheetFull,
                { backgroundColor: colors.surfaceCard, borderColor: colors.gold },
              ]}
            >
              <Ionicons name="file-tray-full-outline" size={32} color={colors.gold} />
              <Text style={[typography.displaySmall, { color: colors.textPrimary }]}>Sheet is full</Text>
              <Text style={[styles.sheetFullBody, { color: colors.textSecondary }]}>
                You&apos;ve logged 20 entries. Start a new sheet to continue.
              </Text>
              <PrimaryButton label="Start new sheet" onPress={() => void form.startNewSheet()} />
            </View>
          ) : (
            <View
              style={[
                styles.card,
                { backgroundColor: colors.surfaceCard, borderColor: colors.divider },
              ]}
            >
              <Field label="ITEM">
                <TextInput
                  value={form.item}
                  onChangeText={form.setItem}
                  placeholder="What did you buy?"
                  placeholderTextColor={colors.textSecondary}
                  style={[styles.input, { color: colors.textPrimary, borderColor: colors.divider }]}
                  returnKeyType="next"
                  accessibilityLabel="Item name"
                />
              </Field>
              <Field label="COST">
                <TextInput
                  value={form.costText}
                  onChangeText={form.setCostText}
                  placeholder="0.00"
                  placeholderTextColor={colors.textSecondary}
                  keyboardType="decimal-pad"
                  style={[styles.input, { color: colors.textPrimary, borderColor: colors.divider }]}
                  accessibilityLabel="Cost"
                />
              </Field>
              <Field label="TYPE">
                <View style={styles.typeRow}>
                  <TypeChip
                    label="Need"
                    selected={form.type === "NEED"}
                    color={colors.need}
                    onPress={() => {
                      form.pickType("NEED");
                    }}
                  />
                  <TypeChip
                    label="Want"
                    selected={form.type === "WANT"}
                    color={colors.want}
                    onPress={() => {
                      form.pickType("WANT");
                    }}
                  />
                </View>
              </Field>
            </View>
          )}

          <View style={styles.ledger}>
            <Eyebrow>SEALED ENTRIES</Eyebrow>
            {entries.length === 0 ? (
              <Text style={[styles.emptyLedger, { color: colors.textSecondary }]}>
                Sealed entries appear here.
              </Text>
            ) : (
              entries.map((entry) => (
                <View key={entry.id}>
                  <LedgerRow
                    entry={entry}
                    currency={currency}
                    onDelete={(id) => confirmDelete(id)}
                  />
                  <View style={[styles.divider, { backgroundColor: colors.divider }]} />
                </View>
              ))
            )}
          </View>
        </ScrollView>
      </KeyboardAvoidingView>
    </SafeAreaView>
  );
}

function Field({ label, children }: { label: string; children: React.ReactNode }) {
  return (
    <View style={styles.field}>
      <Eyebrow>{label}</Eyebrow>
      {children}
    </View>
  );
}

function TypeChip({
  label,
  selected,
  color,
  onPress,
}: {
  label: string;
  selected: boolean;
  color: string;
  onPress: () => void;
}) {
  return (
    <Pressable
      onPress={onPress}
      accessibilityRole="button"
      accessibilityState={{ selected }}
      accessibilityLabel={label}
      style={[
        styles.chip,
        {
          backgroundColor: selected ? color : `${color}1F`,
        },
      ]}
    >
      <Text style={[styles.chipLabel, { color: selected ? "#FFFFFF" : color }]}>{label}</Text>
    </Pressable>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1 },
  flex: { flex: 1 },
  scroll: { paddingHorizontal: 20, paddingBottom: 40 },
  header: { marginBottom: 20, gap: 4 },
  headerRow: { flexDirection: "row", justifyContent: "space-between" },
  card: {
    borderRadius: 14,
    borderWidth: 1,
    padding: 16,
    gap: 16,
    marginBottom: 20,
  },
  field: { gap: 8 },
  input: {
    borderWidth: 1,
    borderRadius: 8,
    paddingHorizontal: 12,
    paddingVertical: 10,
    fontSize: 16,
  },
  typeRow: { flexDirection: "row", gap: 10 },
  chip: {
    flex: 1,
    paddingVertical: 12,
    borderRadius: 10,
    alignItems: "center",
  },
  chipLabel: { fontSize: 14, fontWeight: "600" },
  sheetFull: {
    borderRadius: 14,
    borderWidth: 1,
    padding: 24,
    alignItems: "center",
    gap: 12,
    marginBottom: 20,
  },
  sheetFullBody: { ...typography.body, textAlign: "center" },
  ledger: { marginTop: 4 },
  emptyLedger: { ...typography.caption, textAlign: "center", paddingVertical: 30 },
  divider: { height: StyleSheet.hairlineWidth },
});
