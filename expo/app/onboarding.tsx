import { useRef, useState } from "react";
import {
  Dimensions,
  FlatList,
  Pressable,
  StyleSheet,
  Text,
  View,
  type NativeScrollEvent,
  type NativeSyntheticEvent,
} from "react-native";
import { SafeAreaView } from "react-native-safe-area-context";
import { useRouter } from "expo-router";
import { Ionicons } from "@expo/vector-icons";
import { typography } from "@/design/typography";
import { useThemeColors } from "@/design/theme";
import { useRepository } from "@/state/RepositoryContext";

const { width: SCREEN_WIDTH } = Dimensions.get("window");

const PAGES = [
  {
    icon: "cart-outline" as const,
    title: "Every expense is a Need or a Want",
    body: "Each purchase forces a single binary choice. You confront impulse spending in real time, not at month-end.",
  },
  {
    icon: "calendar-outline" as const,
    title: "The diary keeps 35 days",
    body: "Entries older than 35 days are automatically removed. This is a trainer, not an archive.",
  },
  {
    icon: "checkmark-circle-outline" as const,
    title: "Rows seal themselves",
    body: "The moment you enter item, cost, and type, the row seals — stamped with the current time and saved instantly.",
  },
];

export default function OnboardingScreen() {
  const colors = useThemeColors();
  const router = useRouter();
  const { setHasOnboarded } = useRepository();
  const [page, setPage] = useState(0);
  const listRef = useRef<FlatList>(null);

  const finish = async () => {
    await setHasOnboarded(true);
    router.back();
  };

  const onScroll = (e: NativeSyntheticEvent<NativeScrollEvent>) => {
    const idx = Math.round(e.nativeEvent.contentOffset.x / SCREEN_WIDTH);
    setPage(idx);
  };

  return (
    <SafeAreaView style={[styles.safe, { backgroundColor: colors.surface }]}>
      <View style={styles.toolbar}>
        <View style={{ flex: 1 }} />
        <Pressable onPress={() => void finish()} hitSlop={12}>
          <Text style={[styles.gotIt, { color: colors.accent }]}>Got it</Text>
        </Pressable>
      </View>

      <FlatList
        ref={listRef}
        data={PAGES}
        keyExtractor={(_, i) => String(i)}
        horizontal
        pagingEnabled
        showsHorizontalScrollIndicator={false}
        onScroll={onScroll}
        scrollEventThrottle={16}
        renderItem={({ item }) => (
          <View style={[styles.page, { width: SCREEN_WIDTH }]}>
            <Ionicons name={item.icon} size={56} color={colors.crimson} />
            <Text style={[styles.title, { color: colors.textPrimary }]}>{item.title}</Text>
            <Text style={[styles.body, { color: colors.textSecondary }]}>{item.body}</Text>
          </View>
        )}
      />

      <View style={styles.dots}>
        {PAGES.map((_, i) => (
          <View
            key={i}
            style={[
              styles.dot,
              {
                backgroundColor: i === page ? colors.accent : colors.divider,
              },
            ]}
          />
        ))}
      </View>
    </SafeAreaView>
  );
}

const styles = StyleSheet.create({
  safe: { flex: 1 },
  toolbar: {
    flexDirection: "row",
    alignItems: "center",
    paddingHorizontal: 20,
    paddingVertical: 12,
  },
  gotIt: { fontSize: 15, fontWeight: "600" },
  page: {
    flex: 1,
    alignItems: "center",
    justifyContent: "center",
    paddingHorizontal: 32,
    gap: 24,
    minHeight: 420,
  },
  title: {
    ...typography.displayMedium,
    textAlign: "center",
  },
  body: {
    ...typography.body,
    textAlign: "center",
    lineHeight: 24,
  },
  dots: {
    flexDirection: "row",
    justifyContent: "center",
    gap: 8,
    paddingBottom: 32,
  },
  dot: {
    width: 8,
    height: 8,
    borderRadius: 4,
  },
});
