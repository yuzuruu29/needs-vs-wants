# Needs vs Wants — Google Flows: UGC Ad Script Prompts

Paste-ready prompts for a **Google Flows** flow that turns **uploaded app screenshots**
into UGC ad scripts. The prompts are the portable part — node names may differ slightly
by Google Flows version, so wire them as described in **Flow wiring** below and keep the
prompt text intact.

---

## How the flow works (one minute read)

You upload screenshots of the Needs vs Wants app. The flow:

1. **Reads** the screenshots into a structured "app brief" (what screens exist, what the UI says, what the flow is).
2. **Generates** 4–6 UGC ad scripts in your chosen style, each with a locked hook → setup → demo → CTA structure, shot-by-shot, platform-native.
3. **Formats** the one you pick for TikTok / Reels / Shorts (duration, captions, on-screen text, hashtags).
4. **QA-checks** the script so it never lies about the app or breaks the brand.

### Flow wiring (suggested node chain)

```
[Start]
  └─ [Ask user]  "Upload your app screenshots + pick a style/brand-fit"
        │           (file/image upload node; allow multiple images)
        ▼
  [Generate / LLM node]  ← paste PROMPT B  (screenshot → app brief, JSON out)
        ▼
  [Conditional]  "style chosen?" ── no ──▶ [Generate] Prompt D (pick a style)
        │ yes
        ▼
  [Generate / LLM node]  ← paste PROMPT C + PROMPT D(style)  (N scripts, JSON out)
        ▼
  [Generate / LLM node]  ← paste PROMPT E  (format chosen script for platform)
        ▼
  [Generate / LLM node]  ← paste PROMPT F  (QA / brand-lock check)
        ▼
  [Output]  the final script + captions + shot list
```

Keep **PROMPT A** in the flow's description / system knowledge field so every node
inherits the product facts. Pass the output of each stage into the next node's context.

---

## PROMPT A — Product ground truth (flow-level / system prompt)

Paste this into the flow description or a "knowledge / system" field so all nodes share one
source of truth. Do not let the model invent features beyond this.

```
You write UGC (user-generated content) ad scripts for a real mobile app called
"Needs vs Wants". Ground every script in the facts below. If a fact is not listed,
do not invent it — say the specific claim is unverified and ask the user.

PRODUCT FACTS (locked):
- It is a personal spending trainer, not a traditional budgeting ledger. The core
  mechanic: the user logs every purchase and classifies it as either a NEED or a WANT.
  This single binary choice makes the user confront impulse spending in real time.
- Visual language: "supermarket premium". Need = green, Want = red/crimson, gold accents,
  warm off-white paper background, ledger/notepad feel. Looks like a real physical
  logbook, not a generic finance app.
- Key screens: a Summary landing page with a Need/Want donut chart and a "Log a Purchase"
  button; a Log sheet where you type item + price and pick Need or Want and "seal" it
  (a physical seal-stamp moment); a History ledger of the last 35 days; Settings
  (currency, text size, appearance themes).
- Daily budget: optional, set right on the Log screen. If a purchase would push you over
  the day's limit, the app asks "Log anyway?" before you seal it.
- Retention/feel: a streak counter, a share button, a satisfying seal-stamp on every log.
- Offline-first: no account required, no cloud, no analytics phoning home. Data stays
  on the device.
- Pricing: Free tier (core trainer), Pro ₱49/mo or ₱490/yr (unlimited, trial offered), Max ₱99/mo or ₱990/yr
  (adds an AI Financial Advisor). Website: needs-vs-wants.vercel.app. Android download.
- Brand voice: warm, honest, slightly witty, zero lecture-y finance jargon. Empowering,
  not shaming. "Trainer, not a ledger."

COPY THAT MUST BE USED VERBATIM where it appears on screen: "Log a Purchase",
"Log anyway?", "Need", "Want", and the day budget labels. Match screen text from the
uploaded screenshots exactly; never rename UI buttons in a script.

RULES:
- Talk like a real person filming on their phone, not a copywriter.
- No false claims, no medical/investment promises, no "guaranteed savings".
- Keep it under 60 seconds of spoken VO.
```

---

## PROMPT B — Screenshot → app brief (analysis node)

Use the uploaded screenshots as the visual source. Output strict JSON.

```
You are the "screenshot analyst" stage of a UGC script pipeline for the app
"Needs vs Wants".

Read ALL the uploaded screenshots carefully. For each screenshot, list:
- filename
- which screen it shows (Summary / Log / History / Settings / Paywall / Onboarding / other)
- the visible headline, button labels, and any on-screen copy EXACTLY as written
- the need/want colors and any chart, meter, or progress elements visible
- anything that looks like a daily budget, streak, seal, or share element

Then produce ONE JSON object with this shape:
{
  "app_name": "Needs vs Wants",
  "screens_present": ["Summary", "Log", ...],
  "ui_notes": { "<screen>": { "headline": "...", "buttons": ["..."], "copy": "..." } },
  "visual_language": "one line on the palette/feel you see",
  "demo_beats": ["ordered list of the strongest on-screen moments to show in an ad"],
  "pricing_visible": {"pro": "as shown", "max": "as shown", "trial": "as shown"},
  "gaps": ["screens or features you could NOT see in the screenshots"]
}

Be precise. Copy must match the screenshots. If a screenshot is unreadable, blurred, or
missing, put it in "gaps" — do not guess.
```

---

## PROMPT C — UGC script generator (main)

Feeds off the app brief (PROMPT B output) + a chosen style (PROMPT D). Output N scripts.

```
You are a top UGC ad copywriter for a finance app. Using the app brief in the context
(and the uploaded screenshots as the visual reference for the demo beat), write exactly
{n} scripts — 4 to 6 — in the selected style.

Each script MUST follow this locked structure:
1. HOOK (0–3s): a scroll-stopping first line, spoken to camera, with the on-screen text
   (OST) that should appear in the first frame.
2. SETUP (3–8s): relatable problem in one breath. No jargon.
3. DEMO (8–25s): the money shot. Walk through the real app using the screenshots as the
   shot-by-shot reference — show the donut chart, logging an item, picking WANT, the
   seal-stamp moment, and (if strong) the daily budget "Log anyway?" moment.
4. FLIP / PAYOFF (25–35s): the realization — "trainer, not a ledger."
5. CTA (~35–45s): one clear ask + the URL needs-vs-wants.vercel.app.

Deliver each script as a timestamped shot list with these columns:
| Time | Shot / camera | VO (spoken line) | On-screen text (OST) | Audio/SFX |
Include a "creator notes" line per script: who's talking (age/vibe), where they're
filming, what they're wearing (so it reads real, not corporate).

Constraints:
- Total under 60s. Most scripts 15–30s.
- 9:16 vertical framing, thinking of TikTok / Reels / Shorts.
- Real-person voice. No "" in quotes that sound like a script. No fake testimonials;
  write it as a script a real creator performs.
- Use UI copy verbatim from the brief ("Log a Purchase", "Log anyway?", Need/Want).
- Output as JSON: { "scripts": [ { "title", "style", "duration_seconds", "hook_line",
  "captions", "shot_list": [...], "creator_notes", "estimated_vo_word_count" } ] }
```

---

## PROMPT D — Style variants (pick one, or ask the user)

Swap the style line to change the angle. Each is a drop-in for the "selected style" in PROMPT C.

**D1 · Confessional / "I was faking it"**
```
Style: confessional, slightly self-deprecating, first-person. A young adult who used to
swipe cards and lie to themselves about "needing" things admits it, then shows the app
making them face it. Tone: honest, wry, a little vulnerable. Hook energy:
"I used to do mental gymnastics to convince myself a new gadget was a 'need'."
```
**D2 · Gen Z relatable / skit**
```
Style: energetic, meme-adjacent, single camera, fast cuts. Two states: "before" (spend
without thinking) vs "after" (seal that WANT in 2 seconds). Uses the green/red color
language visually. Hook energy: "POV: you realized your daily budget is basically
a challenge mode."
```
**D3 · Mom / family budget**
```
Style: warm, trustworthy, calm. A parent who used to lose track of little daily spends
(groceries, coffee, kids' stuff) and now catches the small leaks with a daily budget.
Hook energy: "It's not the big bills that break you — it's 12 small 'wants' a day."
```
**D4 · Finance-bro / educational**
```
Style: quick, confident, bite-size finance lesson. Teaches the Need/Want split and why
forcing a binary choice changes behavior. Fast, numbers-forward but plain language.
Hook energy: "Every budgeting app is a ledger. This one is a trainer — here's the diff."
```
**D5 · Transformation / results**
```
Style: before → after, emotional arc, slower camera, hopeful close. Opens with visible
stress (money shame), closes calm and in control. Hook energy:
"I stopped ignoring my bank app at the end of the month."
```
**D6 · Raw testimonial / review**
```
Style: straight-to-camera, single continuous take, no overproduction. A real-feeling
person genuinely describing what changed. Hook energy:
"I downloaded this to track expenses. I didn't expect it to change how I think."
```
**D7 · Humor / light roast**
```
Style: deadpan comedy, a slightly exaggerated "us vs the impulse". Plays the WANT
classification as the fun part. Hook energy:
"Officially, I 'needed' that 4th milk tea. This app disagrees."
```

---

## PROMPT E — Platform delivery / formatting

Takes the chosen script and formats it for one platform.

```
Format the chosen script for {PLATFORM} (TikTok / Instagram Reels / YouTube Shorts / static
caption). Adjust the shot list to the recommended duration for that platform:
- TikTok: 15–30s, hook by 1.5s, hard CTA at end, captions top-safe.
- Reels: 15–30s, hook by 2s, title-safe top third, CTA to profile/link.
- Shorts: 30–60s, allow a slightly slower setup, CTA mid + end.

Output, as JSON:
{
  "platform": "...",
  "duration_seconds": ...,
  "script": "full line-by-line VO + OST",
  "captions": "the exact on-screen text per line, with emoji where the creator would use them",
  "hook_ost": "first-frame on-screen text",
  "caption_for_post": "50-120 char post caption",
  "hashtags": ["at least 8, mix of broad + niche finance tags"],
  "cta": "the exact end line + URL",
  "specs": "resolution, aspect ratio, safe zones, subtitle requirement"
}
```

---

## PROMPT F — QA / brand-lock check

Final pass before output. Catch lies and off-brand copy.

```
You are the QA stage. Review the generated UGC script against these checks and return
a verdict. Do NOT change the script unless a check fails; if one fails, fix it and
explain the fix in one line.

1. TRUTH: Every claim is supported by the product facts (PROMPT A) or the screenshots.
   No invented features, no "guaranteed savings", no finance/medical promises.
2. COPY: UI text matches the screenshots verbatim ("Log a Purchase", "Log anyway?",
   "Need", "Want"). No renamed buttons.
3. BRAND: Tone is warm/real, never lecture-y or shaming. Need/Want shown as green/red
   where color is mentioned.
4. STRUCTURE: Hook < 3s, CTA present, total < 60s, 9:16.
5. PLATFORM: length + safe-frame fit the target platform.

Return:
{
  "verdict": "PASS" | "FIXED",
  "checks": [ { "check": "truth", "status": "pass|fail", "note": "..." } ],
  "alerts": ["anything the human should double-check"],
  "final_script": "the QA-passed script"
}
```

---

## Quick-start checklist

1. Create a flow in Google Flows.
2. Put **PROMPT A** in the flow description/system field.
3. Add an **Ask** node: "Upload screenshots (multiple images) and pick a style (D1..D7)."
4. Add the **LLM/generate** nodes in the order in **Flow wiring**, pasting B, C+D, E, F.
5. Run it, upload your screenshots, pick a style, and copy the final script out.

> Note: Google Flows node names can change between releases. If a node type differs
> (e.g. "Ask a question" vs "Collect input", or "Generate" vs "AI text"), pick the closest
> current node — the prompt text above is what does the work.