# Migrate historical APKs to GitHub Releases

**Status: PREPARED, NOT EXECUTED.** Run manually, by the repository owner only.

Moves the 22 APKs currently committed under `website/public/downloads/` (and
mirrored in `website/downloads/`) into GitHub Releases, then deletes everything
except the current version (**2.0.14**) from both directories. This shrinks the
working tree/deploy upload but does **not** shrink git history — that is the
separate, destructive step in `history-purge-instructions.md` (do this
migration FIRST so no artifact is lost).

## ⚠️ Before deleting anything

1. **Update any site links first.** As of 2026-08-13 `website/public/index.html`
   and its mirror only link `needs-vs-wants-2.0.14.apk` (hero button + footer
   `#apkDownload`), which is kept — but re-verify before deleting:

   ```bash
   grep -rn "downloads/needs-vs-wants" website/ --include=*.html --include=*.json --include=*.js
   ```

   Any hit on a version other than 2.0.14 must be repointed (e.g. to the
   GitHub Release asset URL) before the file is deleted.
2. Old public URLs like `https://needs-vs-wants.vercel.app/downloads/needs-vs-wants-1.4.0.apk`
   will 404 after the next deploy. If anything in the wild (old QR codes, chat
   messages, TikTok comments) links a specific old version, decide whether you
   care before deleting.
3. `gh auth status` must show you authenticated to `yuzuruu29/needs-vs-wants`;
   run everything from the repo root.

## Signing-key note (include in release notes)

Versions **1.0.0–1.5.0** were signed with the old (burned, rotated 2026-08-07,
D86) key. Versions **1.5.1 and later** use the current key. Android will refuse
to install across the key boundary without uninstalling first.

## Step 1 — create one release per historical APK

Tags are created at the current default-branch HEAD (the historical source
snapshots were never tagged, so all tags will point at the same commit — the
release is just an artifact shelf).

```bash
# --- old signing key (1.0.0–1.5.0) ---
gh release create v1.0.0 "website/public/downloads/needs-vs-wants-1.0.0.apk" --title "v1.0.0" --notes "Historical APK. Signed with the old (rotated 2026-08-07) key - cannot update in place to 1.5.1+."
gh release create v1.1.0 "website/public/downloads/needs-vs-wants-1.1.0.apk" --title "v1.1.0" --notes "Historical APK. Signed with the old (rotated 2026-08-07) key - cannot update in place to 1.5.1+."
gh release create v1.2.0 "website/public/downloads/needs-vs-wants-1.2.0.apk" --title "v1.2.0" --notes "Historical APK. Signed with the old (rotated 2026-08-07) key - cannot update in place to 1.5.1+."
gh release create v1.3.0 "website/public/downloads/needs-vs-wants-1.3.0.apk" --title "v1.3.0" --notes "Historical APK. Signed with the old (rotated 2026-08-07) key - cannot update in place to 1.5.1+."
gh release create v1.4.0 "website/public/downloads/needs-vs-wants-1.4.0.apk" --title "v1.4.0" --notes "Historical APK. Signed with the old (rotated 2026-08-07) key - cannot update in place to 1.5.1+."
gh release create v1.5.0 "website/public/downloads/needs-vs-wants-1.5.0.apk" --title "v1.5.0" --notes "Historical APK. Signed with the old (rotated 2026-08-07) key - cannot update in place to 1.5.1+."

# --- current signing key (1.5.1+) ---
gh release create v1.5.1  "website/public/downloads/needs-vs-wants-1.5.1.apk"  --title "v1.5.1"  --notes "Historical APK (first release on the current signing key)."
gh release create v2.0.0  "website/public/downloads/needs-vs-wants-2.0.0.apk"  --title "v2.0.0"  --notes "Historical APK."
gh release create v2.0.1  "website/public/downloads/needs-vs-wants-2.0.1.apk"  --title "v2.0.1"  --notes "Historical APK."
gh release create v2.0.2  "website/public/downloads/needs-vs-wants-2.0.2.apk"  --title "v2.0.2"  --notes "Historical APK."
gh release create v2.0.3  "website/public/downloads/needs-vs-wants-2.0.3.apk"  --title "v2.0.3"  --notes "Historical APK."
gh release create v2.0.4  "website/public/downloads/needs-vs-wants-2.0.4.apk"  --title "v2.0.4"  --notes "Historical APK."
gh release create v2.0.5  "website/public/downloads/needs-vs-wants-2.0.5.apk"  --title "v2.0.5"  --notes "Historical APK."
gh release create v2.0.6  "website/public/downloads/needs-vs-wants-2.0.6.apk"  --title "v2.0.6"  --notes "Historical APK."
gh release create v2.0.7  "website/public/downloads/needs-vs-wants-2.0.7.apk"  --title "v2.0.7"  --notes "Historical APK."
gh release create v2.0.8  "website/public/downloads/needs-vs-wants-2.0.8.apk"  --title "v2.0.8"  --notes "Historical APK."
gh release create v2.0.9  "website/public/downloads/needs-vs-wants-2.0.9.apk"  --title "v2.0.9"  --notes "Historical APK."
gh release create v2.0.10 "website/public/downloads/needs-vs-wants-2.0.10.apk" --title "v2.0.10" --notes "Historical APK."
gh release create v2.0.11 "website/public/downloads/needs-vs-wants-2.0.11.apk" --title "v2.0.11" --notes "Historical APK."
gh release create v2.0.12 "website/public/downloads/needs-vs-wants-2.0.12.apk" --title "v2.0.12" --notes "Historical APK."
gh release create v2.0.13 "website/public/downloads/needs-vs-wants-2.0.13.apk" --title "v2.0.13" --notes "Historical APK."

# --- current version, marked latest ---
gh release create v2.0.14 "website/public/downloads/needs-vs-wants-2.0.14.apk" --title "v2.0.14" --latest --notes "Current release (also served from https://needs-vs-wants.vercel.app)."
```

Verify all 22: `gh release list --limit 30`.

## Step 2 — delete the superseded APKs from BOTH download dirs

Keep **only** `needs-vs-wants-2.0.14.apk` in each directory (21 deletions per
dir). From Git Bash at the repo root:

```bash
for v in 1.0.0 1.1.0 1.2.0 1.3.0 1.4.0 1.5.0 1.5.1 \
         2.0.0 2.0.1 2.0.2 2.0.3 2.0.4 2.0.5 2.0.6 2.0.7 \
         2.0.8 2.0.9 2.0.10 2.0.11 2.0.12 2.0.13; do
  git rm "website/public/downloads/needs-vs-wants-$v.apk" \
         "website/downloads/needs-vs-wants-$v.apk"
done
git commit -m "chore: move historical APKs to GitHub Releases, keep only 2.0.14 in downloads"
git push
```

Then redeploy the site (`vercel deploy --prod` from `website/`, re-alias) so
the deployed `downloads/` matches.

## Step 3 (optional, later)

Once the releases exist, git history can be purged of the APK blobs — see
`history-purge-instructions.md`. **Do not run that without reading its
warnings.**
