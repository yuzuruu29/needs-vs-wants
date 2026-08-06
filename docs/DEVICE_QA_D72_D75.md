# Device QA — UI craft D72–D75

**Build:** debug APK `app/build/outputs/apk/debug/app-debug.apk`  
**App id:** `com.needsvswants.app` · version **1.4.0** (versionCode 5)  
**Scope:** Android only. Play billing / Google may be stubs on this build.

---

## Install

```powershell
# From repo root (device or emulator with USB debugging)
adb install -r "app\build\outputs\apk\debug\app-debug.apk"
```

Or copy the APK to the phone and open it (allow install from unknown sources).

**Fresh first-run test:** uninstall first so DataStore resets.

```powershell
adb uninstall com.needsvswants.app
adb install "app\build\outputs\apk\debug\app-debug.apk"
```

---

## Pass / fail checklist

Mark each item: **OK** · **Meh** · **Bad** + a short note.

### First run (fresh install)

| # | Check | Result |
|---|--------|--------|
| 1 | How It Works opens **before** the Pro/Max paywall | |
| 2 | How It Works: step chrome, Skip / Next, **Begin** is crimson primary | |
| 3 | After Begin/Skip, soft paywall appears (free user) | |
| 4 | Paywall is Free / Pro / Max cards (not crown + bullet list) | |
| 5 | Pro has “Most popular”; Max has seal; dual ₱ / $ prices | |
| 6 | Sticky CTA changes with selected tier | |
| 7 | **Continue free** dismisses paywall; Home is usable | |

### Home (Summary)

| # | Check | Result |
|---|--------|--------|
| 8 | Subtitle: “Seal every purchase…” (not “Expense Tracker”) | |
| 9 | Help / Share sit in gold-edge wells | |
| 10 | Period Day / Week / All: gold-edge bar; selection crimson | |
| 11 | Switching period crossfades chart + stats (not hard cut) | |
| 12 | Stat cards have gold hairline edges | |
| 13 | Primary CTA: **Log a purchase** | |
| 14 | After logging, donut + streak + one insight feel quiet (not gamified) | |

### Log

| # | Check | Result |
|---|--------|--------|
| 15 | NEED / WANT chips: tall, letter-spaced, clear selected state | |
| 16 | Seal still works (item + cost + type) | |
| 17 | Budget Change / Turn off look like quiet text, not stock buttons | |
| 18 | Overspend / delete dialogs use branded PremiumDialog | |

### History

| # | Check | Result |
|---|--------|--------|
| 19 | Empty: Need \| Want seal + “page waits for ink” | |
| 20 | With entries: day **total** is the hero number | |
| 21 | Need / Want split chips under the total | |
| 22 | Export CSV uses gold-edge share well | |
| 23 | Ledger columns still readable (TYPE not crushed) | |

### Advisor

| # | Check | Result |
|---|--------|--------|
| 24 | Free: Max locked gate (no Material crown) | |
| 25 | Free: “View Pro & Max plans” opens paywall | |
| 26 | If Max unlocked (or after fake grant): seal + Unlocked tag | |
| 27 | Chat bubbles / chips / send well feel on-brand | |

### Settings + nav

| # | Check | Result |
|---|--------|--------|
| 28 | Bottom nav icons: pie / edit / book / history / settings (no cart/bulb) | |
| 29 | All Settings blocks gold-edge panels | |
| 30 | About: short trainer lines + seal (no long fintech blurb) | |
| 31 | Free: **View Pro & Max plans** is solid crimson CTA | |
| 32 | Wipe still confirms with danger PremiumDialog | |

### Theme / polish

| # | Check | Result |
|---|--------|--------|
| 33 | Market light looks warm paper + crimson/green/gold | |
| 34 | Dark appearance still readable | |
| 35 | No em-dashes in visible copy you notice | |
| 36 | Reduced motion (system animator off) does not break UI | |

---

## Known non-goals on this build

- Live Play Billing / real prices may show “not configured”
- Google Sign-In only after Start trial / Max while signed out
- iOS not in this build
- Soft paywall once per process; force-stop app to see it again after Continue free

---

## Feedback template (paste back)

```
Device: 
Android version: 
Fresh install? yes/no

Top 3 likes:
1.
2.
3.

Top 3 dislikes / still slop:
1.
2.
3.

Must-fix before next craft:
-

Nice-to-have next:
-
```
