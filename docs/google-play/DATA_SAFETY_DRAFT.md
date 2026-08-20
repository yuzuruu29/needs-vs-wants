# Google Play Data Safety Declaration Draft — Needs vs Wants

> **Application ID:** `com.needsvswants.app`  
> **Source-verified:** 2026-08-20  
> **Target:** Google Play Console Data Safety Questionnaire  
> **Status:** `VERIFIED` against current repository code

---

## Executive Summary

Needs vs Wants is built with a strict **offline-first diary architecture**. All financial transactions, item names, costs, dates, and receipt OCR scans remain **100% on the user's physical device** in local Room database / DataStore files. They are **never** transmitted to project servers or third parties.

Network access is restricted to:
1. **Google Play Billing** (Play subscription purchases, acknowledgement, restore)
2. **Supabase Auth & Entitlements** (optional sign-in to sync Pro/Max status across devices)
3. **Google User Messaging Platform (UMP) & AdMob** (optional rewarded ad consent and ad display on the Free tier)
4. **Sentry** (privacy-lean crash reporting, active only when a non-empty `SENTRY_DSN` is configured)

---

## 1. Data Collection & Sharing Overview

| Question in Play Console | Answer | Code Evidence / Justification |
|---|---|---|
| **Does your app collect or share any of the required user data types?** | **Yes** | App collects email (if user signs in for Pro/Max sync), user IDs (Supabase auth UUID), device IDs / ad info (AdMob/UMP if active), and crash logs (Sentry, if DSN set). |
| **Is all of the user data collected by your app encrypted in transit?** | **Yes** | All network traffic uses HTTPS/TLS (`HttpJsonClient.kt`, Supabase, Google Play API, Sentry). Cleartext HTTP is disabled. |
| **Do you provide a way for users to request that their data be deleted?** | **Yes** | Users can wipe all local diary data at any time via **Settings → Wipe Data** (`AppPreferences.kt`, `AppDatabase.kt`), and sign out / delete account via Supabase auth. |

---

## 2. Category-by-Category Data Safety Declarations

### A. Financial Info

| Data Type | Collected? | Shared? | Purpose | Optional? | Code Evidence |
|---|---|---|---|---|---|
| **User payment info** (credit card, bank account) | **No** | **No** | Handled directly by Google Play Billing on Play builds. App never touches card numbers. | N/A | `GooglePlayBillingController.kt` |
| **Purchase history** (in-app subscription status) | **Yes** (Collected) | **No** | **App functionality, Account management** | Optional (only for paid Pro/Max members) | Purchase token verified server-side in `google_play_verify/index.ts` to grant `is_pro` status in `entitlements` table. |
| **Personal financial data** (diary expenses, budget, Need/Want transactions) | **No** (On-device only) | **No** | Stored locally in SQLite Room DB (`AppDatabase.kt`). Never collected or sent to servers. | N/A | `data/db/AppDatabase.kt`, `data/db/EntryDao.kt` |

> [!NOTE]
> **Play Console guidance on on-device data:** Data that is processed exclusively on the user's device and never leaves it is classified as **NOT collected**. Therefore, user diary entries and budget logs must NOT be declared as collected.

---

### B. Personal Info

| Data Type | Collected? | Shared? | Purpose | Optional? | Code Evidence |
|---|---|---|---|---|---|
| **Email address** | **Yes** (Collected) | **No** | **App functionality, Account management** | **Yes** (Optional — Free core app requires no account; sign-in only for Pro/Max entitlement sync) | `data/auth/AuthRepository.kt`, `data/remote/SupabaseAuth.kt` |
| **User IDs** | **Yes** (Collected) | **No** | **App functionality, Account management** (Supabase `user_id` UUID) | **Yes** (Only when user creates/signs into an account) | `data/auth/AuthSessionStore.kt`, `supabase/migrations/` |

---

### C. Photos and Videos

| Data Type | Collected? | Shared? | Purpose | Optional? | Code Evidence |
|---|---|---|---|---|---|
| **Photos / Receipt Images** | **No** (On-device only) | **No** | **App functionality** (Receipt OCR line-item sorter) | **Yes** (User-initiated camera/gallery scan) | ML Kit `play-services-mlkit-text-recognition:19.0.1` processes bitmap memory buffers entirely on-device. Images are never saved to disk or uploaded to any server. (`ReceiptParser.kt`, `ReceiptSorterModal.kt`). |

---

### D. Device or Other Identifiers

| Data Type | Collected? | Shared? | Purpose | Optional? | Code Evidence |
|---|---|---|---|---|---|
| **Device or other IDs** (Advertising ID) | **Yes** (Collected) | **Yes** (Shared with Google AdMob/UMP) | **Advertising, Analytics** | **Yes** (Only when rewarded ads are active on Free tier; user gives consent via UMP) | `ads/ConsentHelper.kt`, `ads/AdMobRewardedAdGateway.kt` |

---

### E. App Info and Performance

| Data Type | Collected? | Shared? | Purpose | Optional? | Code Evidence |
|---|---|---|---|---|---|
| **Crash logs & Diagnostics** | **Yes** (Collected) | **No** (Third-party processor: Sentry) | **Analytics, Developer communications, App functionality** | **Yes** (Disabled in debug builds and when `SENTRY_DSN` is empty) | `diagnostics/CrashReporting.kt`, `sentry-android:8.53.0` |

---

## 3. Play Console Submission Checklist for Owner

1. [ ] Log in to **Google Play Console** → Select **Needs vs Wants** → **App content** → **Data safety**.
2. [ ] Mark **Yes** to data collection.
3. [ ] Mark **Yes** to data encrypted in transit.
4. [ ] Mark **Yes** to user deletion mechanism (provide account deletion / data wipe URL: `https://needs-vs-wants.vercel.app/terms#account-deletion`).
5. [ ] Select the 4 declared data types:
   - **Personal info → Email address** (Collected, Not shared, Ephemeral/Persistent for account, Optional)
   - **Personal info → User IDs** (Collected, Not shared, Account management, Optional)
   - **Financial info → Purchase history** (Collected, Not shared, App functionality, Optional)
   - **Device or other IDs → Device or other IDs** (Collected, Shared with AdMob, Advertising/Analytics, Optional)
   - *(If Sentry DSN is populated)* **App info and performance → Crash logs** (Collected, App performance, Optional)
6. [ ] Confirm that **Photos and Financial transactions (diary entries)** are marked as **NOT collected** because processing is 100% on-device.
