# Google Play Launch Readiness & Architecture Report

> **Project:** Needs vs Wants  
> **Package Name:** `com.needsvswants.app`  
> **Target SDK:** 36 (Android 16) | **Compile SDK:** 36 | **Min SDK:** 24 (Android 7.0)  
> **Date:** 2026-08-20  
> **Readiness Status:** Technical Engineering Phase `IMPLEMENTED` & `VERIFIED`

---

## 1. Executive Summary & Dual-Distribution Model

Needs vs Wants is now architected as a **dual-distribution Android application**:
1. **Direct Distribution (Sideload APK)**: Preserves existing direct website APK distribution, PayPal subscription billing (with 3-day trial on Pro), PayMongo one-time manual renewal checkout (GCash, Maya, cards), deep-link redirect returns, and Supabase webhook entitlement sync.
2. **Google Play Distribution (AAB)**: Complies with Google Play Payments Policy by utilizing official Google Play Billing Library (`BillingClient` v9.0.0) for subscriptions (`needsvswants_pro` and `needsvswants_max`), localized store pricing, server-authoritative verification via Supabase Edge Function `google_play_verify` (Google Play Developer API `subscriptionsv2`), purchase acknowledgement, and purchase restoration.

---

## 2. Hard Invariants Preserved

- [x] **Zero-Cloud Diary Privacy**: All diary entries, item names, costs, timestamps, and budget configurations remain 100% on the user's physical device in local Room SQLite storage.
- [x] **On-Device Receipt OCR**: Google ML Kit Text Recognition processes receipt bitmaps in memory on the device. Receipt photos and text snippets are **never** uploaded.
- [x] **No Account Required for Core Free Use**: Users can log up to 5 entries/day with carry-forward, view 30-day history, and set daily budgets without creating an account or logging in.
- [x] **Supermarket Premium Brand**: All typography, ruled paper textures, Need/Want green/crimson semantic colors, and motion tokens are preserved.
- [x] **Existing Subscribers Preserved**: Subscriptions remain account-scoped in Supabase. Users who subscribed via direct billing retain their entitlements when signing into the app.

---

## 3. Toolchain & Android 16 (API 36) Compatibility

| Component | Target Version | Verification Status | Notes |
|---|---|---|---|
| **compileSdk** | `36` | Verified | Targets Android 16 APIs |
| **targetSdk** | `36` | Verified | Complies with Google Play API 36+ mandate |
| **minSdk** | `24` | Verified | Supports Android 7.0+ devices without regressions |
| **Google Play Billing** | `9.0.0` | Verified | PBL 9 APIs: PendingPurchasesParams, QueryProductDetailsResult with unfetchedProductList |
| **Edge-to-Edge** | Enforced | Verified | Padding handles system bars & display cutouts |
| **Predictive Back** | Enabled | Verified | PredictiveBackHandler in PaywallScreen & Modals |

---

## 4. Variant Matrix & Artifacts

| Variant | Distribution | Build Type | Output Artifact | Primary Billing Provider |
|---|---|---|---|---|
| `directFullDebug` | Direct (APK) | Debug | APK | Mock / PayPal / PayMongo |
| `directFullRelease` | Direct (APK) | Release (Signed) | `app-direct-full-release.apk` | PayPal & PayMongo |
| `playFullDebug` | Google Play | Debug | APK | Google Play Billing (Sandbox) |
| `playFullRelease` | Google Play | Release (Signed) | `app-play-full-release.aab` | Google Play Billing (Production) |

---

## 5. Next Steps for Launch

1. **Owner Configuration in Play Console**: Follow [`docs/google-play/PLAY_CONSOLE_CHECKLIST.md`](./PLAY_CONSOLE_CHECKLIST.md) to register app, configure `needsvswants_pro` and `needsvswants_max` subscriptions, and link the Google Play Service Account JSON to Supabase.
2. **Internal Testing**: Upload `app-play-full-release.aab` to Play Console Internal Testing track and execute test cases in [`docs/google-play/PHYSICAL_DEVICE_QA.md`](./PHYSICAL_DEVICE_QA.md).
3. **Google Sign-In OAuth Fingerprint**: Register the Play App Signing SHA-1 fingerprint in Google Cloud Console.
