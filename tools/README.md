# tools/

Operational tooling for the Needs vs Wants repo.

| File | Purpose |
|---|---|
| `release.mjs` | Automates the documented release ritual (dry-run by default) |
| `migrate-apks-to-releases.md` | **Prepared, not executed** — commands to move historical APKs to GitHub Releases |
| `history-purge-instructions.md` | **Prepared, not executed** — `git filter-repo` purge of APKs/keystore from history (DESTRUCTIVE) |
| `sfx-src/` | Pre-existing UI sound-effect sources (Kenney UI audio pack; see its `License.txt`) |

---

## release.mjs

```bash
node tools/release.mjs --dry-run 2.0.15   # DEFAULT mode: prints the full plan, modifies nothing, runs nothing
node tools/release.mjs --yes 2.0.15       # actually executes all 8 steps
```

The only positional argument is the new `versionName`; `versionCode` is always
the current value + 1 (parsed live from `app/build.gradle.kts`). Node ≥ 18, no
npm dependencies.

### Steps

| # | Step | Dry-run behaviour |
|---|---|---|
| 1 | Bump `versionCode`/`versionName` in `app/build.gradle.kts` | prints old → new |
| 2 | `gradlew :app:testFullDebugUnitTest`, then `:app:assembleFullRelease` | prints commands |
| 3 | SHA-256 of `app/build/outputs/apk/full/release/app-full-release.apk` | prints path |
| 4 | Copy APK to `website/public/downloads/` **and** `website/downloads/` as `needs-vs-wants-<version>.apk` | prints copies |
| 5 | Lists every occurrence of the old version in `website/public/index.html` (the ~11+ hand-edited markers), pauses until you confirm the hand-edits, then runs `_pad-parts/apply.js` + `check.js` and **aborts unless `ALL CHECKS PASSED`**; re-scans for stale markers afterwards | prints the marker checklist only |
| 6 | Writes `website/public/version.json` (`versionName`, `versionCode`, `apkUrl`, `sha256`, `updatedAt`) | prints the JSON |
| 7 | `vercel deploy --prod` from `website/` (never the monorepo root), then `vercel alias set <deployment-url> needs-vs-wants.vercel.app` | prints exact commands |
| 8 | `git tag v<version>`, `git push origin v<version>`, `gh release create v<version> <apk> --notes ...` | prints exact commands |

### Why step 5 pauses

The version strings inside the landing-page copy (hero chip, updates section,
about panel, stamp ring, download buttons, upgrade note, QR caption, footer, …)
are **hand-edited by design** — the script never rewrites HTML copy beyond what
`apply.js` does. It lists each marker with its line number, waits for you to
edit them, then lets `check.js` be the gate. Edit **only**
`website/public/index.html`; `apply.js` regenerates the byte-identical
`website/index.html` mirror.

Heads-up: `website/_pad-parts/apply.js` prints an informational `apk:` check
that hardcodes the previous APK filename in its `checks` output. It does not
fail the run; update it when convenient.

### Prerequisites for `--yes`

- JDK 17 + Android SDK configured (`local.properties`).
- Release signing credentials available to Gradle — `RELEASE_STORE_FILE`,
  `RELEASE_STORE_PASSWORD`, `RELEASE_KEY_ALIAS`, `RELEASE_KEY_PASSWORD` as
  Gradle properties or environment variables (see the release-signing section
  of `AGENTS.md`; never commit them).
- `vercel` CLI logged in, `website/` linked to the project.
- `gh` CLI authenticated against `yuzuruu29/needs-vs-wants`.
- Run from a real terminal (step 5 needs interactive confirmation).

After a `--yes` run: commit the version bump + site changes, and update the
Second Brain notes (`Tasks.md`, `Decisions.md`).

---

## Prepared-but-not-executed docs

`migrate-apks-to-releases.md` and `history-purge-instructions.md` are runbooks
for the repository owner. Neither has been executed. The history purge in
particular **rewrites git history and force-pushes** — read the warnings inside
before touching it.
