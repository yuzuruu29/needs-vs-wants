from pathlib import Path
import re

base = Path(r"C:/Obsidian Vault/Second Brain/Projects/Needs vs Wants")
tasks = base / "Tasks.md"
dec = base / "Decisions.md"
summary = base / "Summary.md"

# --- Tasks.md ---
t = tasks.read_text(encoding="utf-8")
if "Soft boutique promo audio" not in t and "soft boutique promo audio" not in t.lower():
    line = (
        "- [x] 2026-08-06 - Grok: **Soft boutique promo audio (D70)** — replaced harsh FFmpeg sine SFX with sparse real samples "
        "(`public/audio/soft/`: Remotion CDN via @remotion/sfx + Mixkit UI + quiet Pixabay bed + warm pad). "
        "Design rules: one hit per beat, no type/stamp spam, low volumes, master ~-19 LUFS / LRA ~4.6. "
        "Method recorded in [[Decisions]] **D70**. Re-rendered `video/out/needs-vs-wants-promo-45s.mp4`.\n"
    )
    t = t.replace("## Progress Log\n", "## Progress Log\n" + line, 1)
    tasks.write_text(t, encoding="utf-8")
    print("Tasks.md: progress line added")
else:
    print("Tasks.md: already logged")

# --- Decisions.md ---
d = dec.read_text(encoding="utf-8")
nums = [int(x) for x in re.findall(r"## D(\d+)", d)]
next_n = max(nums) + 1 if nums else 70
label = f"D{next_n}"
print("next decision", label)

if f"## {label}" not in d:
    block = f"""

## {label} — Soft boutique audio for 45s marketing promo (how we fixed harsh SFX)
**Context:** After the D69 45s how-to rewrite, the user said video looked good but audio sounded awful / bothersome. The bed used offline FFmpeg-generated sine stems (fake piano, synthetic pad, tick/stamp/paper noise). Those read as cheap UI beeps — loud, dense, fatiguing. A first polish (48s pad + denser SFX + loudnorm to ~-16 LUFS) kept the bed alive for 45s but still sounded synthetic. User then asked what other sounds/plugins/MCPs exist and for softer, optimized audio.

**Problem diagnosis (verified):**
- Pad/room stems were originally **~15s** on a **45s** composition → bed died after first third (segment mean ~-43 dB at 16s).
- Remaining SFX were **procedural sine beeps**, not real UI samples.
- Mix was **busy** (type taps x6, triple stamps, paper, chimes) → fatiguing on repeat watch.

**Research / options considered:**
| Source | Role | Used? |
|--------|------|-------|
| `@remotion/sfx` (official, installed `@remotion/sfx@4.0.506`) | CDN soft hits: mouseClick, switch, pageTurn, ding, whoosh (skip meme pack) | Yes |
| Mixkit free interface SFX | Soft click / success / UI previews (royalty-free) | Yes |
| Pixabay audio CDN | Continuous soft music bed (processed quieter + lowpass) | Yes |
| Freesound MCP | Search/download community SFX | Not installed (needs free API key) |
| ElevenLabs SFX skill | Prompt-generated custom SFX | Available on machine; not used this pass |
| Kenney Interface Sounds (CC0) | Soft UI pack | Download URL 404 from this PC |

**Decision — soft boutique mix (shipped):**
1. Stop relying on raw FFmpeg beeps as the primary SFX palette for marketing cuts.
2. Place real soft samples under `video/public/audio/soft/` with `ATTRIBUTION.md`.
3. **Sparse design rules** (locked for this promo):
   - Quiet continuous **music bed** + almost-felt **warm pad** under everything
   - **One soft hit per beat** max — no type spam, no triple stamps, no paper scrapes
   - Soft envelope on every hit (fade in/out 2–8 frames) so nothing pops
   - Bed volume ~0.12–0.24; SFX ~0.20–0.34; post-master ~**-19 LUFS**, true peak ~-2.5 dBTP, LRA ~4–5 (even, not aggressive)
4. Keep `PromoAudioBed.tsx` as the single timeline; assets via `staticFile('audio/soft/...')`.
5. Optional follow-ups if user wants better still: Freesound API key MCP, ElevenLabs custom seal/chime, or user-supplied Artlist/Uppbeat exports.

**How we did it (agent recipe for future sessions):**
1. Diagnose with `ffprobe` / `volumedetect` / `ebur128` on the rendered MP4 by time segment.
2. Inventory free sources (Remotion docs list freesound / kenney / soundcn / ElevenLabs; Mixkit interface page).
3. `npm install @remotion/sfx@4.0.506` (match project remotion version).
4. Download soft samples into `video/public/audio/soft/`; normalize with ffmpeg `volume=…` + `alimiter=limit=0.45`.
5. Build quiet `soft-pad.wav` (low-frequency partials, heavy lowpass/reverb) and quiet `soft-music.wav` from bed.
6. Rewrite `PromoAudioBed` sparse: `SoftHit` helper, no chord/stamp spam.
7. Re-render composition; soft loudnorm (~-20 target → ~-19 LUFS output); verify segment means stay even.

**Key files:**
- `video/src/audio/PromoAudioBed.tsx` — timeline
- `video/public/audio/soft/*` — soft samples + `ATTRIBUTION.md`
- `video/src/promoTiming.ts` — scene frame anchors
- Output: `video/out/needs-vs-wants-promo-45s.mp4`

**Verified:**
- Render 1350/1350 h264
- Segment means roughly even (~-19 to -23 dB); no dead mid after 15s
- User confirmed: "sounds better than before" (2026-08-06)

**Agent:** Grok
**Date:** 2026-08-06
"""
    dec.write_text(d.rstrip() + block + "\n", encoding="utf-8")
    print(f"Decisions.md: appended {label}")
else:
    print(f"{label} already present")
    # still need label for summary if D70 exists
    if "## D70" in d:
        label = "D70"

# --- Summary.md ---
s = summary.read_text(encoding="utf-8")
if label not in s and "soft boutique" not in s.lower():
    needle = "See D69."
    insert = (
        "See D69.\n"
        f"- [x] **Soft boutique promo audio (2026-08-06, {label})** — harsh FFmpeg sine SFX replaced by sparse "
        f"Remotion/Mixkit/Pixabay soft samples under `video/public/audio/soft/`; quiet bed + few hits; master ~-19 LUFS. "
        f"Method in Decisions **{label}**."
    )
    if needle in s:
        summary.write_text(s.replace(needle, insert, 1), encoding="utf-8")
        print("Summary.md: updated")
    else:
        print("Summary.md: needle 'See D69.' not found — skip")
else:
    print("Summary.md: already has soft audio entry")
