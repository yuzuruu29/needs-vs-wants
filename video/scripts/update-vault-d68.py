from pathlib import Path

tasks = Path(r"C:\Obsidian Vault\Second Brain\Projects\Needs vs Wants\Tasks.md")
decisions = Path(r"C:\Obsidian Vault\Second Brain\Projects\Needs vs Wants\Decisions.md")
summary = Path(r"C:\Obsidian Vault\Second Brain\Projects\Needs vs Wants\Summary.md")

progress_line = (
    "- [x] 2026-08-05 - Grok: **38s brand promo craft polish (D68)** — elevated D67 "
    "`NeedsVsWantsPromo` to D66-class craft without changing narrative structure "
    "(Hook/Problem/Demo/Features/Closing, 1920×1080 @30fps, 1140f). Paper atmosphere + "
    "gold dust, scene crossfades, premium phone chrome with status bar + day-budget meter "
    "+ NEED/WANT seal chips, month-end statement problem beat, gold-edged feature cards "
    "with SVG icons, brand lockup + crimson CTA + platform chips. New "
    "`video/src/audio/PromoAudioBed.tsx` (pad/room + stamp/whoosh/chime/chord). Rendered "
    "`video/out/needs-vs-wants-promo-38s.mp4` (~5 MB h264); stills QA for all five scenes. "
    "See [[Decisions]] **D68**.\n"
)

t = tasks.read_text(encoding="utf-8")
marker = "## Progress Log\n"
if "D68" not in t or "38s brand promo craft polish" not in t:
    if marker in t:
        t = t.replace(marker, marker + progress_line, 1)
    else:
        t = t + "\n" + progress_line
    tasks.write_text(t, encoding="utf-8")
    print("Tasks.md updated")
else:
    print("Tasks.md already has D68 entry")

d = decisions.read_text(encoding="utf-8")
if "## D68" not in d:
    entry = """
## D68 — 38s brand promo craft polish (D67 elevated)
**Context:** D67 shipped a blueprint-faithful silent 38s 16:9 brand ad. User asked to enhance design, features, and overall craft of `video/src/NeedsVsWantsPromo.tsx`. The vertical How It Works promo already had D66 craft (paper grain, audio, phone fidelity); the landscape ad lagged.
**Decision:** Full craft pass on the existing composition — **no timeline/structure change** (Hook 0–5s, Problem 5–11s, Demo 11–26s, Features 26–33s, Closing 33–38s; 1920×1080 @30fps). Added paper void + gold dust, soft scene fades, landscape phone chrome (status bar, idle float, seal flash, type chips, day budget meter), problem-scene month-end receipt with strike, gold-edged feature cards with SVG icons + serif section head, closing Playfair lockup + crimson CTA pill + platform chips. New offline `PromoAudioBed` over existing FFmpeg stems. Brand tokens stay D7 (Inter / Playfair SC, Need green, Want crimson, gold trim).
**Verified:** `tsc --noEmit` clean; stills at f75/240/480/860/1060; `out/needs-vs-wants-promo-38s.mp4` ~5 MB h264.
**Agent:** Grok
**Date:** 2026-08-05
"""
    decisions.write_text(d.rstrip() + "\n" + entry, encoding="utf-8")
    print("Decisions.md D68 appended")
else:
    print("Decisions.md already has D68")

s = summary.read_text(encoding="utf-8")
old = (
    "- [x] **38s 16:9 brand ad (2026-08-05, D67)** — second composition `NeedsVsWantsPromo` "
    "(1920×1080 @30fps) renders `video/out/needs-vs-wants-promo-38s.mp4`: Hook/Problem/Demo/"
    "Features/Closing narrative per pasted blueprint, D7-branded (Inter/Playfair, Need green/"
    "Want crimson). No audio layer yet. See D67."
)
new = (
    "- [x] **38s 16:9 brand ad (2026-08-05, D67 + D68 craft polish)** — second composition "
    "`NeedsVsWantsPromo` (1920×1080 @30fps) renders `video/out/needs-vs-wants-promo-38s.mp4`: "
    "Hook/Problem/Demo/Features/Closing; D7 brand + paper atmosphere, premium phone demo, "
    "feature cards, CTA lockup, offline audio bed. See D67, D68."
)
if old in s:
    summary.write_text(s.replace(old, new), encoding="utf-8")
    print("Summary.md updated")
elif "D68 craft polish" in s:
    print("Summary.md already has D68")
elif "38s 16:9 brand ad" in s and "D68" not in s:
    s2 = s.replace(
        "No audio layer yet. See D67.",
        "Audio bed + craft polish (D68). See D67, D68.",
    )
    summary.write_text(s2, encoding="utf-8")
    print("Summary.md patched via fallback")
else:
    print("Summary.md: no change applied")
