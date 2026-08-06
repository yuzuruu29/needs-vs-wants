#!/usr/bin/env bash
set -euo pipefail

DECISIONS="/c/Obsidian Vault/Second Brain/Projects/Needs vs Wants/Decisions.md"
TASKS="/c/Obsidian Vault/Second Brain/Projects/Needs vs Wants/Tasks.md"
SUMMARY="/c/Obsidian Vault/Second Brain/Projects/Needs vs Wants/Summary.md"

ls -la "$DECISIONS" "$TASKS" "$SUMMARY"

if grep -q "## D66" "$DECISIONS"; then
  echo "D66 already present"
else
  cat >> "$DECISIONS" << 'EOF'

## D66 — "How It Works" promo polish (craft pass, not concept change)
**Context:** The D65 Remotion promo was solid concept/UI but read as a competent app demo rather than an Apple-keynote boutique ledger commercial. User requested elevate execution only — concept, shot list, palette, copy, and one-phone composition locked.
**Decision:** Craft enhancement on the isolated `video/` Remotion project without changing the 5-scene structure or locked copy. **Audio (primary gap):** no ElevenLabs/FAL keys on this PC, so upgraded offline FFmpeg synthesis (`scripts/gen-audio.sh`) — layered piano partials + strike transient + resonance, harmonic pad, reverb/echo tails, richer SFX (tap noise edge, stamp paper, whoosh, chime, resolving chord). `AudioBed.tsx` ducks pad under typing/stamp/chime/chord; post-render master to ~-15 LUFS / true peak -3.1 dBFS. **Camera:** `PhoneStage` idle micro-float + floor shadow + real perspective origin; optical rack-focus (blur + sat/brightness + vignette falloff) on Hook; paper/ledger wipe with gold hairline + rules at S3 to S4. **Finger:** tapered body, volume shading, contact shadow that darkens/spreads on press, motion blur on travel, dual-layer ripple + specular. **Paper:** grain breathing + warm light drift. **Type/layout:** phone top 600 to 540 (less dead paper), captions rebalanced (`CAPTION_Y=2060`), tighter caption kerning, Day period letter-spacing. All motion still `useCurrentFrame` + `interpolate` + `Easing` only.
**Verified:** `npx remotion render HowItWorks out/how-it-works-15s.mp4` → 1440×2560 @30fps, ~15.14 s, h264+AAC stereo, ~6.9 MB. QA stills at 0.3/3/7.5/10.5/14.2 s: correct chips (Need green / Want crimson), captions spelled exactly, "Log anyway?", "Day" period, brand lockup + "Get the app". Loudness I≈-15.3 LUFS, TP -3.1 dBFS. D65 decision kept intact.
**Agent:** Grok
**Date:** 2026-08-05
EOF
  echo "D66 appended"
fi

python3 - << 'PY'
from pathlib import Path

tasks = Path("/c/Obsidian Vault/Second Brain/Projects/Needs vs Wants/Tasks.md")
text = tasks.read_text(encoding="utf-8")
entry = (
    '- [x] 2026-08-05 - Grok: **"How It Works" promo craft polish (D66)** — elevated D65 Remotion promo without concept change. '
    "Upgraded offline FFmpeg stems (piano partials+transient, pad reverb, richer SFX) + pad ducking in `AudioBed.tsx`; "
    "post-master ~-15.3 LUFS / TP -3.1 dBFS. Phone idle float + optical rack-focus + paper ledger wipe; finger taper/shading/press shadow/motion blur/dual ripples; "
    "paper grain breathing; layout PHONE_TOP 540 + caption rebalance. Render `video/out/how-it-works-15s.mp4` "
    "(1440×2560 @30fps, ~15.14s, ~6.9 MB). QA stills confirm locked copy + palette. See [[Decisions]] **D66**.\n"
)
marker = "## Progress Log\n"
if "promo craft polish (D66)" in text:
    print("Tasks already has D66 entry — skip")
else:
    if marker not in text:
        raise SystemExit("Progress Log marker not found")
    tasks.write_text(text.replace(marker, marker + entry, 1), encoding="utf-8")
    print("Tasks progress updated")

summary = Path("/c/Obsidian Vault/Second Brain/Projects/Needs vs Wants/Summary.md")
text2 = summary.read_text(encoding="utf-8")
old = (
    '- [x] **"How It Works" promo video (2026-08-05, D65)** — new isolated `video/` Remotion project renders a 15s 2K vertical '
    "(1440×2560 @30fps) tutorial promo `video/out/how-it-works-15s.mp4`; five scenes (Hook/Summary/Log/Daily budget/Close), "
    "real app UI inside a floating phone, offline FFmpeg-synthesized audio. See D65."
)
new = (
    '- [x] **"How It Works" promo video (2026-08-05, D65 + D66 craft polish)** — isolated `video/` Remotion project; '
    "15s 2K vertical (1440×2560 @30fps) `video/out/how-it-works-15s.mp4`; five locked scenes; craft pass adds "
    "studio-grade procedural audio (~-15 LUFS), idle float, optical rack-focus, paper wipe, finger realism, grain breathe. See D65, D66."
)
if "D66 craft polish" in text2:
    print("Summary already updated")
elif old in text2:
    summary.write_text(text2.replace(old, new), encoding="utf-8")
    print("Summary updated")
else:
    print("Summary: exact D65 line not found; leaving as-is")
PY

echo "--- D66 tail ---"
tail -n 20 "$DECISIONS"
echo "--- Tasks head of progress ---"
grep -n "D66\|Progress Log" "$TASKS" | head -n 8
echo "--- Summary how-it-works ---"
grep -n "How It Works" "$SUMMARY" | head -n 5
