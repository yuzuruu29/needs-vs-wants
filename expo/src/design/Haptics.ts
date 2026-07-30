/**
 * Haptic feedback — faithful port of Haptics.swift.
 *
 * seal()   → medium impact (entry sealed)
 * warn()   → notification warning (sheet full)
 * success()→ notification success (new sheet started)
 *
 * Uses expo-haptics, which is Expo-Go-compatible.
 */
import * as Haptics from "expo-haptics";

export async function seal(): Promise<void> {
  try {
    await Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
  } catch {
    // Haptics unavailable (e.g. simulator) — no-op.
  }
}

export async function warn(): Promise<void> {
  try {
    await Haptics.notificationAsync(Haptics.NotificationFeedbackType.Warning);
  } catch {
    // no-op
  }
}

export async function success(): Promise<void> {
  try {
    await Haptics.notificationAsync(Haptics.NotificationFeedbackType.Success);
  } catch {
    // no-op
  }
}
