# Git history purge — `git filter-repo`

**Status: PREPARED, NOT EXECUTED.**

> # ⚠️⚠️ DESTRUCTIVE — READ EVERYTHING FIRST ⚠️⚠️
>
> **This REWRITES the entire git history of the public repo and requires a
> force-push. Every existing clone, fork, open PR, and commit link breaks.
> It must only be run deliberately, by the repository owner, with explicit
> approval, after backups. There is no undo besides the backup bundle.**

## What gets purged and why

| Path pattern | Why |
|---|---|
| `*.apk` (all of history) | ~22 APKs × 2 dirs of binary blobs bloating clones; preserved as GitHub Releases first (see `migrate-apks-to-releases.md`) |
| `*.tgz` | `website-deploy.tgz` deploy artifact |
| `.android-sdk/` | An entire Android SDK accidentally committed early on |
| `app/release.keystore` | The **burned** original release keystore (public since 2026-07-29, rotated 2026-08-07, D86) |

Purging the keystore removes it from casual view but it stays **compromised
forever** — the key rotation (D86) is the real mitigation, and stays mandatory
regardless of this purge.

## Prerequisites

1. **Artifacts safe:** `migrate-apks-to-releases.md` executed — all 22 APKs
   exist as GitHub Release assets (Release assets are NOT part of git history
   and survive the purge).
2. **git-filter-repo installed:** `pip install git-filter-repo` (or
   `scoop install git-filter-repo`). Verify: `git filter-repo --version`.
3. **Everything pushed:** no unpushed commits/branches/stashes on ANY machine.
   Check `git status`, `git stash list`, `git log --branches --not --remotes`.
4. **Fresh clone:** filter-repo (rightly) refuses to run in a working repo.
   The purge happens in a brand-new clone, never in `C:\Needs vs Wants`.
5. **Backup bundle:** made below, kept off-machine until you are sure.
6. **Coordinate the force-push:** solo project, but that still means every
   machine/agent worktree of this repo must stop pushing until re-cloned.

## Commands (Git Bash)

```bash
cd /c/tmp   # anywhere OUTSIDE C:\Needs vs Wants

# 1. Fresh mirror-style clone + full backup bundle (KEEP THIS SAFE)
git clone --no-local https://github.com/yuzuruu29/needs-vs-wants.git nvw-purge
cd nvw-purge
git bundle create ../needs-vs-wants-pre-purge-$(date +%Y%m%d).bundle --all

# 2. The purge (removes matching paths from EVERY commit, including HEAD)
git filter-repo \
  --invert-paths \
  --path-glob '*.apk' \
  --path-glob '*.tgz' \
  --path .android-sdk \
  --path app/release.keystore

# 3. Verify nothing matching survives, and check the size win
git rev-list --objects --all | grep -Ei '\.(apk|tgz)$|release\.keystore|^.{41}\.android-sdk/' || echo "clean"
git count-objects -vH

# 4. filter-repo strips the remote on purpose — re-add and FORCE-push
git remote add origin https://github.com/yuzuruu29/needs-vs-wants.git
git push origin --force --all
git push origin --force --tags
```

## Post-purge steps (mandatory)

1. **`*.apk` purge also deletes the CURRENT `needs-vs-wants-2.0.14.apk` from
   the tree.** Restore it in a fresh commit (copy from the GitHub Release asset
   or a local build), to both `website/public/downloads/` and
   `website/downloads/`, then push and redeploy the site.
2. **Every clone everywhere must be re-cloned** — including
   `C:\Needs vs Wants` itself and any agent worktrees. A stale clone that
   pushes can resurrect the old history. Delete old clones, `git clone` fresh.
3. GitHub keeps cached/unreachable objects (old commit URLs, PR refs) for a
   while — for the keystore specifically you can ask GitHub Support to run
   garbage collection on the repo, but again: that key is already treated as
   burned.
4. Re-run CI once and spot-check the site deploy from the fresh clone.
5. Record the purge as a dated decision in the Second Brain
   (`Projects/Needs vs Wants/Decisions.md`).
