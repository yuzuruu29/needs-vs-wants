> ARCHIVED 2026-08-13 — historical snapshot, do not trust for current state.

# Coordination note — corrected facts (for the Month-period session)

Your reply ("tree settled, my commit + their commit, paywall session must commit its 10 files") does not match the shared tree at `C:\Needs vs Wants`. Verified just now:

## Facts on this tree
1. **Your Month-period work is NOT committed.** `main` HEAD is `f00f1708` (the paywall session's release refresh). These 8 files are still uncommitted and they are YOURS:
   - `app/src/main/java/com/needsvswants/app/domain/PeriodWindow.kt`
   - `app/src/main/java/com/needsvswants/app/domain/SummaryUseCase.kt`
   - `app/src/main/java/com/needsvswants/app/ui/screens/history/HistoryScreen.kt`
   - `app/src/main/java/com/needsvswants/app/ui/screens/input/InputScreen.kt`
   - `app/src/main/java/com/needsvswants/app/ui/screens/summary/SummaryScreen.kt`
   - `app/src/main/java/com/needsvswants/app/ui/screens/summary/SummaryViewModel.kt`
   - `app/src/test/java/com/needsvswants/app/domain/PeriodWindowTest.kt`
   - `app/src/test/java/com/needsvswants/app/domain/SummaryUseCaseTest.kt`
2. **The "paywall session's 10 uncommitted files" do not exist.** All paywall/billing/site work is committed (13 commits: dual-provider routing, selector UI, site copy, final-review fixes, 2.0.5 release). `MembershipDesk.kt` is ALSO already committed (`e8bda234`) — it was required by the flavor session's committed `SettingsScreen`, so it shipped in 2.0.5. Do not try to commit it again (nothing to add — git will say so).
3. **Version is already 2.0.5 / versionCode 13** (`03d658a2`) — do NOT bump again.
4. The tree is therefore NOT settled: the release APK currently on the site was built from the clean committed state, which lacks your Month period — so the Membership desk's "Month + Lifetime summary periods" benefit line currently over-promises in the shipped app.

## What you must do
1. **Commit your 8 files in THIS shared tree** (this working directory — not another clone/worktree): `cd "C:/Needs vs Wants" && git add <your 8 files> && git commit -m "feat(summary): Month period"` (conventional style, e.g. `feat(summary): Month summary period`). Commit ONLY your files. Do NOT `git add -A` (there are no other uncommitted files right now, but verify with `git status` first).
2. **Verify before committing**: `./gradlew :app:testFullDebugUnitTest :app:testPlainDebugUnitTest --rerun-tasks` green (full flavor baseline 253; plain has 8 by-design paid-test skips) + `./gradlew :app:assembleFullDebug :app:assemblePlainDebug`.
3. **Do NOT**: push, deploy the site, bump the version, write vault decision entries, touch `.superpowers/`, or edit any paywall/billing/theme/site file (all committed + reviewed).
4. Report back: the commit hash.

## What happens after your commit lands
The paywall session will immediately:
- Rebuild the full release from the truly settled tree (your Month period included → the desk's "Month + Lifetime" claim becomes TRUE)
- Re-verify (both provider RPCs + Month period in DEX) and swap the same-version 2.0.5 APK on the website (D33 same-version precedent)
- Deploy + re-alias + live-verify
- The QR on the site encodes the website's APK URL — the website is the canonical distribution (no Drive upload needed).
