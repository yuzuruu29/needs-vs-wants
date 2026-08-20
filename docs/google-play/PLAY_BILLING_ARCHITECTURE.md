# Google Play Billing Architecture — Needs vs Wants

> **Package:** `com.needsvswants.app`  
> **Target Library:** Google Play Billing Library (`com.android.billingclient:billing:9.0.0`)  
> **Backend:** Supabase Edge Function (`google_play_verify`) + Google Play Developer API subscriptionsv2  
> **Status:** `VERIFIED` & `IMPLEMENTED`

---

## 1. High-Level Architecture Overview

```text
┌────────────────────────────────────────────────────────────────────────┐
│                          NEEDS VS WANTS CLIENT                         │
│                                                                        │
│  ┌───────────────────────┐              ┌───────────────────────────┐  │
│  │   Direct Distribution │              │  Play Store Distribution  │  │
│  │   (APK Sideload)      │              │  (AAB Play Release)       │  │
│  │                       │              │                           │  │
│  │   PayPal / PayMongo   │              │   GooglePlayBilling       │  │
│  │   Web Checkout        │              │   Controller (PBL 9.0.0)  │  │
│  └───────────┬───────────┘              └─────────────┬─────────────┘  │
└──────────────┼────────────────────────────────────────┼────────────────┘
               │                                        │ (purchaseToken,
               │ (custom_id, webhook)                   │  kind="subscription")
               ▼                                        ▼
┌────────────────────────────────────────────────────────────────────────┐
│                        SUPABASE EDGE FUNCTIONS                         │
│                                                                        │
│  ┌───────────────────────┐              ┌───────────────────────────┐  │
│  │ paypal_webhook /      │              │    google_play_verify     │  │
│  │ paymongo_webhook      │              │   (subscriptionsv2)       │  │
│  └───────────┬───────────┘              └─────────────┬─────────────┘  │
└──────────────┼────────────────────────────────────────┼────────────────┘
               │                                        │ (Service Account JWT
               │                                        │  OAuth2 Token)
               │                                        ▼
               │                          ┌───────────────────────────┐
               │                          │  Google Play Developer    │
               │                          │  API subscriptionsv2      │
               │                          └─────────────┬─────────────┘
               │                                        │ (verified status &
               │                                        │  authoritative expiry)
               ▼                                        ▼
┌────────────────────────────────────────────────────────────────────────┐
│                    SUPABASE POSTGRES (entitlements)                    │
│                                                                        │
│          user_id | is_pro | tier (pro/max) | paid_until | provider     │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Google Play Product & Subscription Catalog

| Subscription Product ID | Base Plan ID | Billing Period | Direct Price (Reference) | Recommended Play Price |
|---|---|---|---|---|
| `needsvswants_pro` | `pro-monthly` | 1 Month recurring | ₱49.00 / month | ₱49.00 / month (or local currency) |
| `needsvswants_pro` | `pro-annual` | 1 Year recurring | ₱490.00 / year | ₱490.00 / year (or local currency) |
| `needsvswants_max` | `max-monthly` | 1 Month recurring | ₱99.00 / month | ₱99.00 / month (or local currency) |
| `needsvswants_max` | `max-annual` | 1 Year recurring | ₱990.00 / year | ₱990.00 / year (or local currency) |

---

## 3. Client Flow: Subscription Purchase & Acknowledgement

1. **ProductDetails Querying**:
   - `GooglePlayBillingController` connects to `BillingClient` on app startup / Paywall screen open.
   - Calls `queryProductDetailsAsync` for `needsvswants_pro` and `needsvswants_max` (Type: `BillingClient.ProductType.SUBS`).
   - Uses PBL 9 `QueryProductDetailsResult` with `productDetailsList` / `unfetchedProductList`.
   - Retrieves `subscriptionOfferDetails` to obtain the active base plan and formatted localized pricing strings (e.g. `₱49.00`).
2. **Launch Billing Flow**:
   - User taps "Subscribe" on Pro or Max card.
   - App constructs `BillingFlowParams.ProductDetailsParams` with `selectedOfferToken`.
   - Invokes `BillingClient.launchBillingFlow(activity, params)`.
3. **Purchase Update & Server Verification**:
   - `PurchasesUpdatedListener.onPurchasesUpdated` receives `BillingResponseCode.OK` with `List<Purchase>`.
   - For each purchase with `PurchaseState.PURCHASED`:
     - App extracts `purchase.purchaseToken`.
     - App signs in or uses existing Supabase session access token.
     - POSTs to Supabase Edge Function `google_play_verify` with `package_name`, `purchase_token`, and `kind = "subscription"`.
4. **Server Verification & Entitlement Grant**:
   - Server mints RS256 JWT using `GOOGLE_PLAY_SERVICE_ACCOUNT_JSON` secret.
   - Obtains OAuth token for scope `https://www.googleapis.com/auth/androidpublisher`.
   - Calls `purchases.subscriptionsv2.tokens.get` on the Android Publisher API.
   - Verifies: package name matches `com.needsvswants.app`, `subscriptionState` is `ACTIVE` or `IN_GRACE_PERIOD`, `expiryTime` > now.
   - Derives tier server-authoritatively from Google's verified `productId`.
   - Upserts `entitlements` table for authenticated `user_id` with `tier` (`pro` or `max`), `is_pro = true`, `paid_until = expiryTime`, `provider = 'google'`.
5. **Purchase Acknowledgement**:
   - Upon receiving `{ success: true, valid: true }` from `google_play_verify`, client calls `BillingClient.acknowledgePurchase(AcknowledgePurchaseParams)`.
   - Purchase is marked acknowledged within the required 3-day Google Play window.

---

## 4. Restore Purchases

* **Scenario**: App reinstall, new device, or manual "Restore Purchases" tap on Paywall.
* **Process**:
  1. Client queries `BillingClient.queryPurchasesAsync(QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.SUBS).build())`.
  2. For every active purchase returned by Play Store cache:
     - Sends `purchaseToken` to backend `google_play_verify`.
     - Backend verifies status and refreshes `entitlements` row.
  3. Client refreshes local entitlement store via `EntitlementRepository.refreshFromRemote()`.
  4. Access is restored immediately.

---

## 5. Security & Invariant Guarantees

1. **Client Never Grants Entitlements Directly**: The client *never* self-grants Pro or Max simply because `BillingClient` returned a purchase. Entitlements are strictly granted by the Supabase database via the authenticated `google_play_verify` edge function.
2. **Idempotency**: `purchase_token` is unique and idempotent. Repeated verifications for the same token update the same entitlement row without duplicate charges or side effects.
3. **Account Binding**: Purchases are tied to the authenticated user's account UUID in Supabase.
