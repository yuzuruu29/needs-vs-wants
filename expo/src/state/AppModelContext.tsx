/**
 * AppModel — root navigation coordinator.
 * Port of AppModel.swift; switchToLog / onboarding use expo-router.
 */
import { useRouter } from "expo-router";
import { createContext, useCallback, useContext, useMemo, useState, type ReactNode } from "react";

export type Tab = "summary" | "log" | "history" | "settings";

interface AppModel {
  selectedTab: Tab;
  showOnboarding: boolean;
  setSelectedTab: (tab: Tab) => void;
  switchToLog: () => void;
  openOnboarding: () => void;
  setShowOnboarding: (show: boolean) => void;
}

const AppModelContext = createContext<AppModel | null>(null);

const TAB_ROUTES: Record<Tab, string> = {
  summary: "/(tabs)",
  log: "/(tabs)/log",
  history: "/(tabs)/history",
  settings: "/(tabs)/settings",
};

export function AppModelProvider({ children }: { children: ReactNode }) {
  const router = useRouter();
  const [selectedTab, setSelectedTab] = useState<Tab>("summary");
  const [showOnboarding, setShowOnboarding] = useState(false);

  const switchToLog = useCallback(() => {
    setSelectedTab("log");
    router.navigate("/(tabs)/log");
  }, [router]);

  const openOnboarding = useCallback(() => {
    setShowOnboarding(true);
    router.push("/onboarding");
  }, [router]);

  const value = useMemo<AppModel>(
    () => ({
      selectedTab,
      showOnboarding,
      setSelectedTab,
      switchToLog,
      openOnboarding,
      setShowOnboarding,
    }),
    [selectedTab, showOnboarding, switchToLog, openOnboarding],
  );

  return <AppModelContext.Provider value={value}>{children}</AppModelContext.Provider>;
}

export function tabRouteFor(tab: Tab): string {
  return TAB_ROUTES[tab];
}

export function useAppModel(): AppModel {
  const ctx = useContext(AppModelContext);
  if (!ctx) throw new Error("useAppModel must be used within AppModelProvider");
  return ctx;
}
