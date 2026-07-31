# Needs vs Wants — Official Website

Static soft-launch / preview site for the Needs vs Wants expense trainer.

## Local preview

Open `index.html` in a browser, or from the repo root:

```bash
npx serve website
```

## Vercel deploy

1. Import `yuzuruu29/needs-vs-wants` in Vercel.
2. Set **Root Directory** to `website` (root `vercel.json` also sets `outputDirectory` to `website`).
3. Framework Preset: **Other** (no build).
4. Deploy.

APK downloads point at GitHub Releases:

`https://github.com/yuzuruu29/needs-vs-wants/releases/latest/download/needs-vs-wants-1.0.0.apk`

Upload `needs-vs-wants-1.0.0.apk` to a GitHub Release named `v1.0.0` (or latest) before announcing the download CTA as live. Optionally also place a copy under `downloads/` for local/static hosting (gitignored by default via `*.apk`; use `git add -f` if you want it in the repo).

## Mobile / desktop

- Sticky blur header + hamburger menu under 900px
- Hero stacks under 940px; Log ledger columns compact under 640px
- Phone mockups single-column under 880px
- Safe-area insets for notched phones
