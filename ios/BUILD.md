# Build & Ship — Needs vs. Wants (iOS)

> This project was authored on a Windows host without macOS/Xcode, so it has
> **not** been compiled here. After these steps it will build and run on a Mac.

## Prerequisites (on your Mac)
- macOS 14+ with Xcode 15+ (iOS 17 SDK)
- [XcodeGen](https://github.com/yonaskolb/XcodeGen): `brew install xcodegen`
- An Apple Developer account for TestFlight (Phase 10)

## 1. Generate the Xcode project
```bash
cd ios
xcodegen generate          # reads project.yml → NeedsVsWants.xcodeproj
open NeedsVsWants.xcodeproj
```

## 2. Run on a simulator
- Select an iPhone (SE 3rd gen, 15, 15 Pro Max).
- Cmd-R. The app launches to Summary; first launch auto-shows the instructions.

## 3. Optional: bundled fonts
The Android app uses **Playfair Display SC** (display) + **Inter** (body). iOS
falls back to the system serif/sans today. To match exactly:
1. Add the `.ttf`/`.otf` files to `NeedsVsWants/Resources/`.
2. Register them in `Info.plist` (`UIAppFonts`).
3. Swap `AppFont` (`Theme/Typography.swift`) to `Font.custom("PlayfairDisplaySC", ...)`.

## 4. QA matrix (acceptance)
- [ ] iPhone SE / 13 / 15 / 15 Pro Max render without overflow
- [ ] Light mode, Dynamic Type (large text) stay readable
- [ ] Fresh install → instructions auto-show, dismiss persists
- [ ] Log: item+cost+type auto-seals & persists; kill app mid-sheet → row survives
- [ ] Sheet counter hits `20 / 20`; active card replaced by "Start new sheet"
- [ ] TYPE never wraps; cost uses tabular digits; Row stable at SE width
- [ ] History groups by day; day Need/Want totals correct; delete confirm
- [ ] Summary: Day / Week / All (35d) rotor + range caption; donut + stat cards
- [ ] Settings: currency switch reformats all screens; cents unchanged
- [ ] Wipe diary (confirm) clears entries + resets settings
- [ ] Relaunch persistence (data + currency survive)
- [ ] 35-day retention: entries older than 35 days auto-purge on launch

## 5. Phase 10 — TestFlight
1. In Signing & Capabilities, set your **Team** (edit `DEVELOPMENT_TEAM` in
   `project.yml` or set it in Xcode) and a unique bundle id.
2. Add an **App Icon** set named `AppIcon` in an asset catalog
   (the Android `app_icon.jpg` can be resized as a starting point).
3. Product → Archive → Distribute App → TestFlight (Internal Only is fastest).
4. Add internal testers in App Store Connect; for external testers, submit for
   the brief TestFlight review.

## Notes / known parity decisions
- The 20-cap counts **total** entries (mirrors the Android `InputViewModel`,
  which used `dao.observeAll().size`). See `IOS_IMPLEMENTATION_PLAN.md` §2.
- Currency is display-only; `costCents` (Int64) is never mutated on switch.
- No third-party dependencies: SwiftData + SwiftUI `Canvas` only.
