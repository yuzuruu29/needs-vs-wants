# GitHub Actions — CI for Needs vs Wants

Three workflows, each path-filtered to the surface it covers. The legacy
`ios.yml` workflow was removed when the SwiftUI port was archived (the source
lives on the `archive/ios-swiftui-port` branch).

| Workflow | File | Triggers on | What it runs |
|---|---|---|---|
| Android Build | `android.yml` | `app/**`, Gradle files | `:app:testFullDebugUnitTest`, `:app:lintFullDebug`, `:app:assembleFullDebug`; plus a signed `:app:assembleFullRelease` job |
| Backend (Supabase) | `backend.yml` | `supabase/**` | `deno test supabase/functions/_shared/` (pure helper unit tests) |
| Website locks | `website.yml` | `website/**` | `node website/_pad-parts/check.js` (locked-decision checks + mirror byte-compare) |

All three also support manual runs via **Actions → Run workflow**
(`workflow_dispatch`).

## Flavor-qualified Gradle tasks

The app has an `experience` flavor dimension (`full` = production, `plain` =
free side-by-side test build), so bare task names like `testDebugUnitTest` no
longer exist. CI builds the `full` flavor only.

## Release build job (`android.yml` → `release-build`)

Runs after `test-and-assemble` on pushes to `main`/`master` and manual runs,
and **only** when the signing secrets are configured. GitHub does not allow
`secrets` in a job-level `if:`, so the job exposes the presence check as a job
env var (`HAS_SIGNING_SECRETS`) and every step gates on it; without secrets
every step is skipped and the job stays green.

Required repository secrets (Settings → Secrets and variables → Actions):

| Secret | Value |
|---|---|
| `RELEASE_KEYSTORE_BASE64` | base64 of the release keystore file |
| `RELEASE_STORE_PASSWORD` | keystore password |
| `RELEASE_KEY_ALIAS` | key alias |
| `RELEASE_KEY_PASSWORD` | key password |

The workflow decodes the keystore to `$RUNNER_TEMP/release.keystore` and passes
`RELEASE_STORE_FILE` + the three credential vars through the environment — the
same env-var fallback `app/build.gradle.kts` (`signingConfigs.release`) already
documents. Never commit the keystore or passwords; see the release-signing
section of `AGENTS.md` (D86).

The signed APK is uploaded as the `app-full-release` artifact (30-day
retention) with its SHA-256 in the job summary.
