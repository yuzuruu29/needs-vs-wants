# Needs vs Wants

A 30-day spending trainer for the Philippine market: every expense you log is a
single binary call — **Need or Want** — and the app holds you to it. Native
Android (Kotlin + Jetpack Compose, offline-first Room storage), distributed as
a **sideloaded APK** from the official site, not from the Play Store.

- **Live site + APK download:** https://needs-vs-wants.vercel.app
- **Status:** solo-developer project; source is public for transparency but
  **not licensed for reuse** — see [LICENSE](LICENSE).

## Repository layout

| Path | What it is |
|---|---|
| `app/` | The Android app (Kotlin, Compose, Room, Hilt). Flavor dimension `experience`: `full` = production, `plain` = free side-by-side test build |
| `website/` | Soft-launch landing page deployed to Vercel (`website/public/index.html` is canonical; `website/index.html` is a byte-identical mirror) |
| `supabase/` | Pro-subscription backend: SQL migration + Deno edge functions (entitlements, PayPal/PayMongo/Play/App Store verification) |
| `docs/` | Project docs; `docs/archive/` holds historical handoffs/plans (do not trust for current state); `docs/assets/brand/` holds app-icon sources |
| `tools/` | Release automation + operational runbooks — see [tools/README.md](tools/README.md) |
| `.github/workflows/` | CI — see [.github/workflows/README.md](.github/workflows/README.md) |

`ios/` (an experimental SwiftUI port) is **archived** on the
`archive/ios-swiftui-port` branch and removed from `main`. The abandoned
`expo/` prototype directory was removed entirely.

## Building the app

Requires JDK 17 and the Android SDK (point `sdk.dir` at it in
`local.properties`; see `local.properties.example`).

```bash
./gradlew :app:assembleFullDebug     # debug APK (production flavor)
./gradlew :app:assemblePlainDebug    # free-only side-by-side test flavor
```

Release builds (`:app:assembleFullRelease`) are signed with credentials that
live **outside the repo**; without them the build fails at signing. See the
release-signing section of `AGENTS.md`.

## Tests and checks

```bash
./gradlew :app:testFullDebugUnitTest        # app unit tests
./gradlew :app:lintFullDebug                # Android lint
deno test supabase/functions/_shared/       # backend helper unit tests
node website/_pad-parts/check.js            # website locked-decision checks
```

CI runs the same commands, path-filtered per surface (Android / Supabase /
website), plus a signed release build when signing secrets are configured —
details in [.github/workflows/README.md](.github/workflows/README.md).

## Releasing

The release ritual (version bump → build → checksum → site update → deploy →
tag) is automated by `tools/release.mjs`, dry-run by default:

```bash
node tools/release.mjs --dry-run 2.0.15
```

See [tools/README.md](tools/README.md) for the full step list and
prerequisites.

## Security

Release signing uses a keystore and passwords that are **never committed**; the
original keystore was exposed and has been rotated and burned (decision D86).
Do not add keystores, passwords, or API secrets anywhere in this repo — the
release-signing section of `AGENTS.md` documents the setup.

## License

Proprietary — all rights reserved. The code is published for transparency and
reading only; no reuse, redistribution, or derivative works. See
[LICENSE](LICENSE).
