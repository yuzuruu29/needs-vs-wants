# Physical Device QA Test Matrix — Google Play Launch

> **Target:** Needs vs Wants Release Candidates (`directFullRelease` APK / `playFullRelease` AAB via Play Internal Testing)  
> **Hardware:** Physical Android Devices running Android 7.0 (API 24) to Android 15/16 (API 35/36)  
> **Status:** `OPERATIONAL` Test Script

---

## 1. Test Environment Setup

* **Google Account**: Google Play License Testing account configured in Play Console.
* **Track**: Google Play Console → **Internal Testing** Track.
* **Test Cards**: Google Play License Test card (Always approves, Always declines, Slow test card).

---

## 2. Comprehensive Test Scenarios

### Section A: Fresh Install & First Launch

| ID | Test Scenario | Steps | Expected Result | Pass/Fail |
|---|---|---|---|---|
| **A1** | Fresh install & onboarding | 1. Install app from Play Store internal testing link.<br>2. Open app. | Splash screen displays crisp Need/Want seal with exit crossfade.<br>Progressive onboarding overlay appears with "What's your spending goal?" selector. | [ ] |
| **A2** | Free tier initialization | 1. Complete onboarding.<br>2. Verify Summary & Input screens. | Free tier default (5 logs/day quota, empty ledger sheet).<br>No crash, no login required. | [ ] |
| **A3** | Offline diary invariant | 1. Turn on Airplane mode.<br>2. Log 3 items (2 Need, 1 Want).<br>3. Verify Summary screen. | Entries seal instantly into Room SQLite database.<br>Need/Want donut renders correct percentages without network. | [ ] |

---

### Section B: Google Sign-In & Credential Manager

| ID | Test Scenario | Steps | Expected Result | Pass/Fail |
|---|---|---|---|---|
| **B1** | Google Sign-In with Play Signing | 1. Go to Settings → Tap "Account" or open Paywall.<br>2. Tap "Sign in with Google". | Google Credential Manager bottom sheet opens.<br>Selecting Google account successfully authenticates with Supabase backend. | [ ] |
| **B2** | Sign-Out isolation | 1. Sign in.<br>2. Tap "Sign Out" in Settings. | Warning dialog appears.<br>Signing out immediately clears local entitlement snapshot; reverts device cleanly to Free tier without deleting local diary entries. | [ ] |

---

### Section C: Google Play In-App Billing (Subscriptions)

| ID | Test Scenario | Steps | Expected Result | Pass/Fail |
|---|---|---|---|---|
| **C1** | Localized price display | 1. Open Paywall screen. | Monthly and Annual prices reflect Google Play Store localized currency (e.g. ₱49.00 / ₱490.00).<br>External payment selectors (PayPal/PayMongo/GCash) are **hidden** on Play builds. | [ ] |
| **C2** | Pro subscription purchase | 1. Select Pro (Monthly or Annual).<br>2. Complete Google Play test purchase. | Google Play bottom sheet succeeds.<br>App sends purchase token to `google_play_verify`.<br>App receives active grant and acknowledges purchase.<br>One-shot Pro activation seal appears; Pro features unlocked immediately. | [ ] |
| **C3** | Max subscription purchase | 1. Select Max (Monthly or Annual).<br>2. Complete Google Play test purchase. | Max tier activated. Financial Advisor tab and Pre-Seal Want coach unlocked. | [ ] |
| **C4** | Purchase cancellation / user dismiss | 1. Tap Subscribe on Paywall.<br>2. Dismiss Google Play billing sheet. | Billing flow cleanly cancels without hanging the paywall UI or crashing. | [ ] |
| **C5** | Pending purchase handling | 1. Use "Slow test card" (Pending). | App flags purchase as pending without premature entitlement grant. | [ ] |

---

### Section D: Restore Purchases

| ID | Test Scenario | Steps | Expected Result | Pass/Fail |
|---|---|---|---|---|
| **D1** | Restore on fresh install / reinstall | 1. Uninstall app with active subscription.<br>2. Reinstall from Play Store.<br>3. Open Paywall → Tap "Restore Purchases". | Active subscription is queried from Play BillingClient, verified by Supabase backend, and Pro/Max access is restored. | [ ] |
| **D2** | Restore after data clear | 1. Clear app storage in Android Settings.<br>2. Open app and tap "Restore Purchases". | Active subscription restored without issues. | [ ] |

---

### Section E: Pro/Max Features & OCR Receipt Scanner

| ID | Test Scenario | Steps | Expected Result | Pass/Fail |
|---|---|---|---|---|
| **E1** | On-device ML Kit Receipt OCR | 1. On Pro/Max account, tap "Scan Receipt" on Log tab.<br>2. Take photo or select sample receipt. | Camera/storage permission prompted appropriately.<br>Text parsed into line-items with prices.<br>ReceiptSorterModal opens with Need/Want sorting.<br>Batch seal writes all items to sheet. | [ ] |
| **E2** | Free tier gate on Receipt OCR | 1. On Free account, tap "Scan Receipt". | ReceiptProGateDialog appears explaining feature is Pro/Max exclusive with CTA to Paywall. Camera is NOT opened. | [ ] |

---

### Section F: Accessibility & UI Polish

| ID | Test Scenario | Steps | Expected Result | Pass/Fail |
|---|---|---|---|---|
| **F1** | Extra-large text scale QA | 1. Go to Settings → Text size → Extra large (1.18×).<br>2. Verify Log ledger, Summary donut, History list, and Paywall. | No text clipping, no ellipsis on money figures, layout scales gracefully. | [ ] |
| **F2** | Android 15/16 Edge-to-Edge | 1. Verify top status bar and bottom navigation bar padding. | No UI elements obscured behind navigation pills or camera cutouts. | [ ] |
