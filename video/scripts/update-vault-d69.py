from pathlib import Path
import re

tasks = Path(r"C:/Obsidian Vault/Second Brain/Projects/Needs vs Wants/Tasks.md")
dec = Path(r"C:/Obsidian Vault/Second Brain/Projects/Needs vs Wants/Decisions.md")
summary = Path(r"C:/Obsidian Vault/Second Brain/Projects/Needs vs Wants/Summary.md")

t = tasks.read_text(encoding="utf-8")
if "D69" not in t and "promo-45s" not in t:
    entry = (
        "## Progress Log\n"
        "- [x] 2026-08-05 - Grok: **45s marketing promo rewrite (D69)** — rebuilt "
        "`video/src/NeedsVsWantsPromo.tsx` from abstract 38s brand ad into a purpose + how-to "
        "marketing film (1920x1080 @30fps, **1350f / 45s**). Narrative: Hook → Purpose "
        "(trainer not ledger + NEED/WANT) → 01 See (Summary donut + Log CTA) → 02 Seal "
        "(type Coffee/cost, WANT seal, stack rows) → 03 Guard (daily meter + Log anyway?) → "
        "Benefits (4 cards) → Close (lockup + CTA + URL). Shared `promoTiming.ts`; re-timed "
        "`PromoAudioBed`; Root duration uses PROMO_TOTAL_FRAMES. Paper wipe between See→Seal. "
        "Verified: tsc clean; stills all 7 scenes; rendered `video/out/needs-vs-wants-promo-45s.mp4` "
        "(~6.3 MB h264). See [[Decisions]] **D69**.\n"
    )
    t = t.replace("## Progress Log\n", entry, 1)
    tasks.write_text(t, encoding="utf-8")
    print("Tasks.md updated")
else:
    print("Tasks.md already has D69/45s")

d = dec.read_text(encoding="utf-8")
nums = [int(x) for x in re.findall(r"## D(\d+)", d)]
next_n = max(nums) + 1 if nums else 69
print("next decision", next_n)
label = f"D{next_n}"
if f"## {label}" not in d:
    block = f"""

## {label} — 45s marketing promo: purpose + how-to (rewrite of D67/D68 structure)
**Context:** User asked to improve `video/src/NeedsVsWantsPromo.tsx` in all ways for a marketing video that explains how to use the app and its purpose. Prior 38s cut (D67/D68) was brand-forward but weak on pedagogy (demo = three sealing rows only; no Summary flow, no typing, no budget dialog).
**Decision:** Expand to **45s / 1350 frames @ 30fps 16:9** with a seven-beat marketing arc:
1. Hook (4s) — decide after they spend
2. Purpose (5s) — trainer not ledger; NEED vs WANT chips
3. How 01 See (6s) — phone Summary donut + Log a Purchase
4. How 02 Seal (13s) — type item/cost, classify WANT, seal rows stack
5. How 03 Guard (6s) — daily budget meter + Log anyway? confirm
6. Benefits (6s) — four gold-edged cards (seal / budget / 35-day / offline)
7. Close (5s) — brand lockup, Get the app, needs-vs-wants.vercel.app
Shared timing module `video/src/promoTiming.ts`; audio bed re-synced; paper wipe at See→Seal. Brand tokens stay D7. Output: `video/out/needs-vs-wants-promo-45s.mp4`.
**Rationale:** Marketing ads convert when viewers learn the product in one watch. Step badges + real UI beats beat abstract feature cards alone.
**Verified:** `npx tsc --noEmit` clean; stills for all scenes; h264 render 1350/1350 (~6.3 MB).
**Agent:** Grok
**Date:** 2026-08-05
"""
    dec.write_text(d.rstrip() + block + "\n", encoding="utf-8")
    print(f"Decisions.md appended {label}")
else:
    print(f"{label} already present")

s = summary.read_text(encoding="utf-8")
if "promo-45s" not in s:
    needle = "See D67, D68."
    insert = (
        "See D67, D68.\n"
        f"- [x] **45s marketing how-to promo (2026-08-05, {label})** — "
        "`NeedsVsWantsPromo` rewritten to Hook/Purpose/See/Seal/Guard/Benefits/Close "
        "(1350f, 45s); purpose + 3-step how-to with real Summary/Log/Budget UI; rendered "
        f"`video/out/needs-vs-wants-promo-45s.mp4`. See {label}."
    )
    if needle in s:
        summary.write_text(s.replace(needle, insert, 1), encoding="utf-8")
        print("Summary.md updated")
    else:
        print("Summary needle missing")
else:
    print("Summary already mentions 45s")
