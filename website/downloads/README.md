# Soft-launch downloads

Production download CTA on the site points at GitHub Releases:

https://github.com/yuzuruu29/needs-vs-wants/releases/latest/download/needs-vs-wants-1.0.0.apk

## Upload the asset

1. Build: `./gradlew assembleRelease` (or use existing `app-release.apk`).
2. Rename/copy to `needs-vs-wants-1.0.0.apk`.
3. Attach to the `v1.0.0` GitHub Release (or create a newer release and update the site href).

Optional local/static copy for Vercel path `/downloads/…`:

```bash
cp app-release.apk website/downloads/needs-vs-wants-1.0.0.apk
git add -f website/downloads/needs-vs-wants-1.0.0.apk
```

(`*.apk` is gitignored; `-f` is required. Prefer Releases hosting for large binaries.)
