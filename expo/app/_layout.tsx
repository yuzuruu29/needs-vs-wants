import { Stack } from "expo-router";
import * as SplashScreen from "expo-splash-screen";
import { useEffect, useRef } from "react";
import { useFonts } from "expo-font";
import { AppModelProvider, useAppModel } from "@/state/AppModelContext";
import { RepositoryProvider, useRepository } from "@/state/RepositoryContext";
import { FONT_DISPLAY_BOLD, FONT_DISPLAY_REGULAR } from "@/design/typography";

SplashScreen.preventAutoHideAsync();

function OnboardingGate({ children }: { children: React.ReactNode }) {
  const { loading, hasOnboarded } = useRepository();
  const { openOnboarding } = useAppModel();
  const didPrompt = useRef(false);

  useEffect(() => {
    if (!loading && !hasOnboarded && !didPrompt.current) {
      didPrompt.current = true;
      openOnboarding();
    }
  }, [loading, hasOnboarded, openOnboarding]);

  return children;
}

function RootNavigation() {
  return (
    <Stack screenOptions={{ headerShown: false }}>
      <Stack.Screen name="(tabs)" />
      <Stack.Screen
        name="onboarding"
        options={{
          presentation: "fullScreenModal",
          animation: "fade",
        }}
      />
    </Stack>
  );
}

export default function RootLayout() {
  const [fontsLoaded, fontError] = useFonts({
    [FONT_DISPLAY_BOLD]: require("../assets/fonts/PlayfairDisplaySC-Bold.ttf"),
    [FONT_DISPLAY_REGULAR]: require("../assets/fonts/PlayfairDisplaySC-Regular.ttf"),
  });

  useEffect(() => {
    if (fontsLoaded || fontError) {
      SplashScreen.hideAsync();
    }
  }, [fontsLoaded, fontError]);

  if (!fontsLoaded && !fontError) {
    return null;
  }

  return (
    <RepositoryProvider>
      <AppModelProvider>
        <OnboardingGate>
          <RootNavigation />
        </OnboardingGate>
      </AppModelProvider>
    </RepositoryProvider>
  );
}
