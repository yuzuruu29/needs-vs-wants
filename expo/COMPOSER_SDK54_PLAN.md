# Composer 2.5 — Downgrade Expo to SDK 54 (physical iPhone via Expo Go)

## Goal
Make `expo/` runnable on a **physical iPhone** using **App Store Expo Go 54.0.2**.

Current state is **SDK 57** (`expo ~57.0.9`, RN `0.86.2`). App Store Expo Go only loads **SDK 54** projects. Downgrade in place; do **not** rewrite app features.

## Non-goals
- Do not touch `app/` (Android), `ios/`, or `ios-native/`
- Do not add EAS / development builds / Apple Developer Program flow
- Do not change product behavior, UI, or data model
- Do not commit unless the user asks

## Why this works
App code already uses Expo-Go-safe APIs that exist on SDK 54:
- `expo-sqlite` (`openDatabaseAsync`, `execAsync`, `runAsync`, `getFirstAsync`)
- `expo-router` (`Stack`, `Tabs`, `useRouter`)
- `expo-font`, `expo-splash-screen`, `expo-haptics`
- `react-native-svg`, `react-native-safe-area-context`
- `@expo/vector-icons`

No custom native modules. No camera / notifications / etc.

## Target stack (SDK 54 / Expo Go 54)
From Expo `sdk-54` `bundledNativeModules.json`:

| Package | Target |
|---------|--------|
| `expo` | `~54.0.0` |
| `react` / `react-dom` | `19.1.0` |
| `react-native` | `0.81.5` |
| `expo-router` | `~6.0.24` |
| `expo-font` | `~14.0.12` |
| `expo-haptics` | `~15.0.8` |
| `expo-splash-screen` | `~31.0.13` |
| `expo-sqlite` | `~16.0.10` |
| `expo-status-bar` | `~3.0.9` |
| `expo-system-ui` | `~6.0.9` |
| `@expo/vector-icons` | `^15.0.3` |
| `react-native-gesture-handler` | `~2.28.0` |
| `react-native-reanimated` | `~4.1.1` |
| `react-native-worklets` | `0.5.1` (required peer of Reanimated 4 on SDK 54) |
| `react-native-safe-area-context` | `~5.6.0` |
| `react-native-screens` | `~4.16.0` |
| `react-native-svg` | `15.12.1` |
| `react-native-web` | `~0.21.0` |
| `buffer` | keep `^6.0.3` (JS-only) |
| `@types/react` | pin to SDK 54 via `expo install` |
| `typescript` | use whatever `npx expo install` / Expo 54 template expects (likely `~5.9.x`, not `~6.0.3`) |

**Do not hand-edit every version if avoidable.** Prefer the install flow below so Expo CLI pins correctly.

---

## Execution steps (do in order)

### 0. Working directory
All commands run from:

```text
C:\Needs vs Wants\expo
```

### 1. Clean install artifacts
Remove stale SDK 57 lock/install state:

```powershell
Remove-Item -Recurse -Force node_modules -ErrorAction SilentlyContinue
Remove-Item -Force package-lock.json -ErrorAction SilentlyContinue
Remove-Item -Recurse -Force .expo -ErrorAction SilentlyContinue
```

### 2. Point `package.json` at SDK 54 core
Edit `package.json` dependencies to at least:

```json
{
  "dependencies": {
    "@expo/vector-icons": "^15.0.3",
    "buffer": "^6.0.3",
    "expo": "~54.0.0",
    "expo-font": "~14.0.12",
    "expo-haptics": "~15.0.8",
    "expo-router": "~6.0.24",
    "expo-splash-screen": "~31.0.13",
    "expo-sqlite": "~16.0.10",
    "expo-status-bar": "~3.0.9",
    "expo-system-ui": "~6.0.9",
    "react": "19.1.0",
    "react-dom": "19.1.0",
    "react-native": "0.81.5",
    "react-native-gesture-handler": "~2.28.0",
    "react-native-reanimated": "~4.1.1",
    "react-native-worklets": "0.5.1",
    "react-native-safe-area-context": "~5.6.0",
    "react-native-screens": "~4.16.0",
    "react-native-svg": "15.12.1",
    "react-native-web": "~0.21.0"
  },
  "devDependencies": {
    "@types/react": "~19.1.0",
    "typescript": "~5.9.2"
  }
}
```

Keep existing `"scripts"`, `"main": "expo-router/entry"`, `"private": true`, `"name"`, `"version"`.

### 3. Install + let Expo fix peers
```powershell
npm install
npx expo install --fix
```

If `expo install --fix` changes versions, **keep its pins** (they are authoritative for SDK 54).

### 4. Babel for Reanimated 4
`babel.config.js` must include the Reanimated plugin **last**:

```js
module.exports = function (api) {
  api.cache(true);
  return {
    presets: ["babel-preset-expo"],
    plugins: ["react-native-reanimated/plugin"],
  };
};
```

If Metro complains about worklets, ensure `react-native-worklets` is installed (step 2).

### 5. `app.json` sanity (minimal changes)
Keep current config. Confirm:
- `"name"`, `"slug"`, `"scheme"` unchanged
- `"plugins"` still includes `expo-router` and `expo-splash-screen`
- `"newArchEnabled": true` is fine on SDK 54 (New Arch default path)
- Do **not** add extra native plugins

Optional (only if Expo CLI warns): add `"sdkVersion"` is **not** required in modern Expo — omit it.

### 6. App source compatibility sweep
Scan and fix **only if** typecheck/runtime fails. Expected mostly no-ops:

| Area | File(s) | What to check |
|------|---------|---------------|
| SQLite | `src/data/db.ts`, `EntryRepository.ts` | `openDatabaseAsync` / async API still valid on `expo-sqlite ~16` |
| Router | `app/_layout.tsx`, `app/(tabs)/_layout.tsx`, screens | `Stack` / `Tabs` / modal presentation still work on `expo-router ~6` |
| Fonts | `app/_layout.tsx` | `useFonts` + `SplashScreen` hide still work |
| Haptics | `src/design/Haptics.ts` | `expo-haptics` import paths unchanged |
| SVG | `src/components/DonutChart.tsx` | `react-native-svg` Path API unchanged |
| Icons | tab layout | `@expo/vector-icons` Ionicons names still valid |

Do **not** refactor for style. Surgical fixes only.

### 7. Typecheck
```powershell
npx tsc --noEmit
```

Fix any SDK/type breaks with minimal diffs. Common risk: `@types/react` / RN type mismatches after downgrade — resolve via `npx expo install` pins, not loose `any`.

### 8. Start Metro for Expo Go
```powershell
npx expo start
```

Success criteria in terminal:
- Bundler starts without dependency version errors
- QR code shown
- Banner / doctor should report **SDK 54** (or Expo Go-compatible), **not** 57
- No “Project is incompatible with this version of Expo Go” once scanned

Tunnel tip if LAN QR fails on phone:
```powershell
npx expo start --tunnel
```

### 9. Physical iPhone verification checklist
User has Expo Go **54.0.2** on App Store. After scan:

1. App loads (splash → UI)
2. Onboarding shows on first launch; dismissible
3. Log a purchase (item + cost + Need/Want) → **auto-seals** (no submit)
4. Summary donut updates
5. Period Day / Week / All works
6. History shows entry; swipe/delete if implemented
7. Settings currency switch (PHP default; JPY no decimals)
8. Settings wipe / “How it works” re-opens onboarding
9. Restart app → data persists (sqlite)

If Expo Go still says wrong SDK: re-check `node_modules/expo/package.json` version starts with `54.` and restart Metro with cache clear:

```powershell
npx expo start -c
```

---

## Failure playbook

| Symptom | Likely cause | Fix |
|---------|--------------|-----|
| “SDK 57 project” / incompatible Expo Go | Still on expo 57 or stale `.expo` | Re-run clean steps 1–3; `npx expo start -c` |
| Peer dependency / version mismatch | Mixed SDK packages | `npx expo install --fix` only; no manual cherry-picks |
| Reanimated / worklets babel error | Missing plugin or `react-native-worklets` | Step 4 + ensure worklets dep |
| Metro resolve `@/` fails | tsconfig paths / babel | Keep `tsconfig` paths; ensure `babel-preset-expo` present |
| SQLite API missing | Wrong `expo-sqlite` major | Must be `~16.0.x` for SDK 54 |
| Phone can’t reach Metro | LAN/firewall | `npx expo start --tunnel` |

Nuclear fallback (only if install is hopeless):
1. Move current `expo/src`, `expo/app`, `expo/assets` aside
2. `npx create-expo-app@latest . --template blank-typescript` then pin with Expo Go–compatible **sdk-54** template if prompted, or create in temp and copy
3. Re-copy app source + fonts
4. Re-add deps with `npx expo install expo-router expo-sqlite expo-font expo-haptics expo-splash-screen expo-system-ui react-native-svg react-native-safe-area-context react-native-gesture-handler react-native-reanimated react-native-screens`

Prefer in-place downgrade first.

---

## Done definition
- [ ] `expo/package.json` resolves to **Expo SDK 54** (`expo` ~54)
- [ ] `npm install` + `npx expo install --fix` succeed
- [ ] `npx tsc --noEmit` passes
- [ ] `npx expo start` shows QR
- [ ] Physical iPhone Expo Go **54.0.2** opens the app without SDK mismatch
- [ ] Smoke checklist in §9 passes
- [ ] Vault update (optional, if agent has vault access): append decision that Expo port targets SDK 54 for App Store Expo Go parity

## Files expected to change
- `expo/package.json`
- `expo/package-lock.json` (regenerated)
- `expo/babel.config.js` (reanimated plugin)
- Possibly tiny fixes in `expo/app/**` or `expo/src/**` only if compile/runtime requires
- Do **not** create new feature files

## Agent constraints (Composer 2.5)
- Stay inside `expo/`
- Prefer `npx expo install` over guessing versions
- Minimal diffs; no drive-by refactors
- No git commit unless user asks
- After success, report: expo version, how to start, and any files changed
