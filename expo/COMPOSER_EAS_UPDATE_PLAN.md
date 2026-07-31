# Composer 2.5 — Publish to EAS Update (test on iPhone with PC off)

## Goal
Publish the `expo/` app to **EAS Update** so the user can open it in **App Store Expo Go 54.0.2** on a physical iPhone **without leaving Metro / this PC running**.

Flow after setup:
1. (Once, while PC is on) run `eas update` → bundle uploads to Expo’s servers  
2. Open that published update in Expo Go (QR / dashboard link)  
3. Turn PC off → app still loads from Expo cloud on next open (cached / re-fetched)

## Prerequisites (must be true before / during this plan)
| Check | Status / action |
|-------|-----------------|
| `expo/` is **SDK 54** | Verify `package.json` has `"expo": "~54.x"`. If still SDK 57, **stop** and finish `expo/COMPOSER_SDK54_PLAN.md` first. |
| Free **Expo account** | User signs up at https://expo.dev/signup if needed |
| Expo Go **54.0.2** on iPhone | Already confirmed by user |
| Same Expo account logged into **Expo Go** on the phone | Required: Expo Go only loads EAS updates for projects you own |
| Internet on the phone | Required to download the published update (first open / refresh) |

**Not required:** Apple Developer Program, Mac, EAS Build, TestFlight, development builds.

## Non-goals
- Do not touch `app/` (Android), `ios/`, or `ios-native/`
- Do not create an EAS **Build** / IPA / APK unless publish-to-Expo-Go fails and user asks
- Do not change product features / UI / data model
- Do not commit unless the user asks
- Do not put secrets in the repo (`.env` stays gitignored)

## How this differs from `npx expo start`
| | Metro (`expo start`) | EAS Update (this plan) |
|--|----------------------|-------------------------|
| PC must stay on | Yes | No (after publish) |
| Live reload | Yes | No — republish to ship changes |
| Host | Your LAN / tunnel | Expo cloud (`u.expo.dev`) |
| Open in Expo Go | Scan local QR | Browse / scan published update QR |

Official note: [expo-updates](https://docs.expo.dev/versions/latest/sdk/updates/) — *“To test the content of an update in Expo Go, run `eas update` and then browse to the update in Expo Go.”* Only Expo-Go-compatible libraries (this app qualifies).

---

## Execution steps (do in order)

### 0. Working directory
```text
C:\Needs vs Wants\expo
```

### 1. Verify SDK 54
```powershell
node -p "require('./package.json').dependencies.expo"
```
Must print something like `~54.0.0` or `54.x.x`. If `57`, abort and run SDK 54 plan first.

Optional health check:
```powershell
npx expo-doctor
```
Fix only blockers that would break publish.

### 2. Install / use EAS CLI
Prefer no global install pollution:
```powershell
npx eas-cli@latest --version
```
All later commands: `npx eas-cli@latest …` **or** `npx eas …` if available.

### 3. Log in to Expo (interactive — user must complete)
```powershell
npx eas-cli@latest login
npx eas-cli@latest whoami
```
If login opens a browser / asks for credentials, **pause and tell the user** to finish login, then re-run `whoami`.

### 4. Link / create EAS project
```powershell
npx eas-cli@latest init
```
- Accept creating/linking project for slug `needs-vs-wants` (or existing)
- This writes `extra.eas.projectId` into `app.json` / `app.config`

If `init` already done (projectId present), skip.

### 5. Install `expo-updates` (SDK 54 pin)
```powershell
npx expo install expo-updates
```
Expect something near `expo-updates ~29.0.x` for SDK 54 (trust `expo install` pins).

### 6. Configure EAS Update
```powershell
npx eas-cli@latest update:configure
```

Expected `app.json` additions under `expo` (exact values from CLI):
```json
{
  "expo": {
    "runtimeVersion": {
      "policy": "appVersion"
    },
    "updates": {
      "url": "https://u.expo.dev/<PROJECT_ID>"
    },
    "extra": {
      "eas": {
        "projectId": "<PROJECT_ID>"
      }
    }
  }
}
```

Also create/update `eas.json` if the CLI generates one. Minimal acceptable:
```json
{
  "cli": {
    "version": ">= 16.0.0",
    "appVersionSource": "remote"
  },
  "build": {
    "development": {
      "developmentClient": true,
      "distribution": "internal"
    },
    "preview": {
      "distribution": "internal",
      "channel": "preview"
    },
    "production": {
      "channel": "production"
    }
  },
  "update": {
    "preview": {
      "channel": "preview"
    },
    "production": {
      "channel": "production"
    }
  }
}
```
Do **not** run `eas build` as part of this plan.

### 7. Add npm scripts (convenience)
In `package.json` scripts, add:
```json
"update:preview": "eas update --channel preview --message \"preview update\"",
"update:prod": "eas update --channel production --message \"production update\""
```
(Use `npx eas-cli@latest update …` in scripts if `eas` is not on PATH.)

### 8. Typecheck before first publish
```powershell
npx tsc --noEmit
```
Fix only publish-blocking errors.

### 9. Publish first update
```powershell
npx eas-cli@latest update --channel preview --message "Initial Needs vs Wants Expo Go publish"
```

Notes:
- SDK 54: `--environment` is **not** required (that flag is for SDK 55+). Omit it unless CLI insists.
- Command runs `expo export`, uploads JS + assets. Needs network.
- On success, CLI prints a **dashboard URL** and often a **QR / preview** link — **copy these into the final report for the user**.

If publish fails on `runtimeVersion` / Expo Go messaging: keep `runtimeVersion.policy: "appVersion"` (standard). Expo Go can still **browse** the update for compatible projects; do not remove `runtimeVersion` unless CLI/docs for the installed eas-cli version explicitly require it for Go preview.

### 10. Open on physical iPhone (user steps — document clearly in handoff)
Composer cannot scan the phone. Instruct the user:

1. Open **Expo Go** on iPhone  
2. Log in with the **same Expo account** used in `eas login`  
3. Open the **preview / update link** from the CLI output **or** go to https://expo.dev → project **needs-vs-wants** → **Updates** → select the latest update → **Preview** → scan QR with Camera / Expo Go  
4. App should load (onboarding → tabs) **without** Metro running  
5. Confirm: force-close Expo Go, turn off PC (or stop Metro), reopen the same update → still works  

Smoke checklist (same as SDK 54 plan §9):
- Onboarding, auto-seal log, donut, periods, history, currency, wipe, persistence

### 11. Document “how to ship a change later”
Add a short section at the top of this file’s sibling note **or** leave in the agent’s final message:

```powershell
cd "C:\Needs vs Wants\expo"
# make JS/TS changes...
npx tsc --noEmit
npx eas-cli@latest update --channel preview --message "Describe the change"
```

Then reopen / refresh the update in Expo Go. PC only needs to be on for the publish command.

---

## Failure playbook

| Symptom | Likely cause | Fix |
|---------|--------------|-----|
| Incompatible with Expo Go / wrong SDK | Project still SDK 57 | Finish `COMPOSER_SDK54_PLAN.md` |
| “Project not found” / can’t open update | Different Expo account in Expo Go | Log into same account in Expo Go |
| `eas login` blocked in agent | Interactive auth | Ask user to run login in their terminal; continue after `whoami` works |
| Publish fails missing projectId | `eas init` skipped | Run step 4 |
| Publish fails dependency / export | Type error or bad import | Fix `tsc` / export errors; re-run update |
| Update opens but crashes on native API | Non–Expo-Go library | Shouldn’t apply to this app; check recent deps |
| Works only while Metro runs | User opened **local** QR, not **published** update | Use Expo dashboard Update Preview QR |

Nuclear fallback (only if Expo Go cannot load published updates for this project):
1. Report clearly that Expo Go path failed and why (paste error)  
2. Do **not** silently start EAS Build (needs Apple credentials for iOS device)  
3. Ask user whether to fall back to **PC-on Metro** testing only  

---

## Done definition
- [ ] `expo` is SDK 54  
- [ ] `eas whoami` shows logged-in account  
- [ ] `app.json` has `extra.eas.projectId`, `updates.url`, `runtimeVersion`  
- [ ] `expo-updates` installed via `npx expo install`  
- [ ] `eas update --channel preview` succeeds  
- [ ] User has dashboard / QR link to open in Expo Go  
- [ ] Handoff explains: PC off OK after publish; republish for changes  
- [ ] No Apple Developer / EAS Build required for this path  

## Files expected to change
- `expo/app.json` (EAS projectId, updates URL, runtimeVersion)
- `expo/eas.json` (created/updated)
- `expo/package.json` (+ lockfile) — `expo-updates`, optional scripts
- Possibly `expo/package-lock.json` only otherwise  
- Do **not** generate native `ios/` / `android/` folders for this plan

## Agent constraints (Composer 2.5)
- Stay inside `expo/`
- Prefer `npx expo install` / `npx eas-cli@latest` over guessing versions  
- Minimal diffs; no feature refactors  
- If auth is required, **stop and ask the user** — do not invent credentials  
- No git commit unless user asks  
- Final report must include: Expo username, project URL, update URL/QR instructions, and exact republish command  

## Relationship to other plans
1. `expo/COMPOSER_SDK54_PLAN.md` — make Expo Go **compatible** (SDK match)  
2. **This plan** — make testing **PC-independent** via cloud publish  

Run SDK 54 plan first if not already done; then this plan.
