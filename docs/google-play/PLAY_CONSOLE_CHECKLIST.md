# Google Play Console Setup Checklist — Owner Action Guide

> **Project:** Needs vs Wants (`com.needsvswants.app`)  
> **Target Track:** Internal Testing → Production  
> **Status:** `OWNER ACTION` Required

---

## Phase 1: Google Play Console Account & App Creation

1. [ ] **Log in to Google Play Console**: [https://play.google.com/console](https://play.google.com/console).
2. [ ] **Create App**:
   - **App Name**: `Needs vs Wants: Spending Coach`
   - **Default Language**: English (United States) or English (Philippines)
   - **App or Game**: App
   - **Free or Paid**: Free (contains in-app subscriptions)
   - **Declarations**: Accept Developer Program Policies and US export laws.
3. [ ] **Set Up Google Play App Signing**:
   - In Play Console → **App Integrity** → **Play App Signing**.
   - Choose: **Use Google Play App Signing** (Google manages signing key; you upload the signed AAB using the upload keystore at `.local/release.keystore`).
4. [ ] **Copy the Play App Signing SHA-1 Fingerprint**:
   - Once Play App Signing is enabled, copy the **SHA-1 certificate fingerprint of the app signing key**.
   - Go to **Google Cloud Console** → **APIs & Services** → **Credentials**.
   - Edit the Android OAuth 2.0 Client ID for `com.needsvswants.app` and register this SHA-1 fingerprint.  
   *(CRITICAL: Without this step, Google Sign-In via Credential Manager will fail for users who install the app from Google Play).*

---

## Phase 2: In-App Subscriptions & Base Plans

1. [ ] Go to **Monetize** → **Products** → **Subscriptions**.
2. [ ] Create Subscription 1:
   - **Product ID**: `needsvswants_pro`
   - **Name**: `Needs vs Wants Pro`
   - **Base Plan 1**: `pro-monthly`
     - Type: Auto-renewing
     - Billing Period: 1 Month
     - Price: ₱49.00 PHP (or auto-converted local pricing)
   - **Base Plan 2**: `pro-annual`
     - Type: Auto-renewing
     - Billing Period: 1 Year
     - Price: ₱490.00 PHP
3. [ ] Create Subscription 2:
   - **Product ID**: `needsvswants_max`
   - **Name**: `Needs vs Wants Max (Financial Advisor)`
   - **Base Plan 1**: `max-monthly`
     - Type: Auto-renewing
     - Billing Period: 1 Month
     - Price: ₱99.00 PHP
   - **Base Plan 2**: `max-annual`
     - Type: Auto-renewing
     - Billing Period: 1 Year
     - Price: ₱990.00 PHP
4. [ ] Activate all base plans.

---

## Phase 3: Supabase Backend Google Service Account Link

1. [ ] In Google Cloud Console, create a Service Account with role `Google Play Android Developer` (or grant access in Play Console → Users and permissions).
2. [ ] Create and download a JSON Private Key for the service account.
3. [ ] Set the secret in Supabase:
   ```bash
   supabase secrets set GOOGLE_PLAY_SERVICE_ACCOUNT_JSON='{"type":"service_account",...}'
   ```
4. [ ] Deploy `google_play_verify` edge function.

---

## Phase 4: Store Presence & Declarations

1. [ ] **Main Store Listing**:
   - Follow [`docs/google-play/PLAY_STORE_LISTING_DRAFT.md`](./PLAY_STORE_LISTING_DRAFT.md) for Title, Short Description, and Full Description.
   - Upload 512×512 App Icon (`app_icon.png`).
   - Upload 1024×500 Feature Graphic.
   - Upload Phone Screenshots (minimum 2; captured from real device).
2. [ ] **App Content & Policy Declarations**:
   - **Privacy Policy**: `https://needs-vs-wants.vercel.app/privacy.html` (bare `/privacy` also redirects)
   - **Ads**: Check **Yes, my app contains ads** (for optional rewarded ads on the Free tier).
   - **App Access**: All functionality is available without special access restrictions (free tier available to all; optional paid subscriptions).
   - **Content Rating**: Complete questionnaire (Expected rating: PEGI 3 / Everyone / 3+).
   - **Target Audience**: 18 and older (or General Audience).
   - **Financial Features**: Personal finance / budgeting tracker (not a loan/credit or banking institution).
   - **Data Safety**: Complete using [`docs/google-play/DATA_SAFETY_DRAFT.md`](./DATA_SAFETY_DRAFT.md).

---

## Phase 5: Internal Testing Track Launch

1. [ ] Go to **Testing** → **Internal testing**.
2. [ ] Create new release and upload the signed App Bundle (`app-play-full-release.aab`).
3. [ ] Add internal testers (email list).
4. [ ] Add developer Google accounts to **License Testing** (Settings → License testing) to test purchases with test credit cards without real money charges.
5. [ ] Release to Internal Testing and follow [`docs/google-play/PHYSICAL_DEVICE_QA.md`](./PHYSICAL_DEVICE_QA.md) for verification.
