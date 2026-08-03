# Developed by ACEU — Design Spec

**Date:** 2026-08-02  
**Status:** Approved (brainstorm)  
**Surface:** Soft-launch website (`website/`) only  
**Agent:** Cursor  

## Problem

Needs vs Wants is an ACEU AI Solutions studio product, but the soft-launch site has no visible credit or outbound link to the studio portfolio.

## Goals

- Add a polished **Developed by** section above the footer.
- Add a compact credit link in the footer bottom bar.
- Link both to the ACEU homepage: `https://aceu-ai-solutions-portfolio.netlify.app`
- Keep the supermarket-premium page feel; use ACEU blue only as a studio accent.

## Non-goals

- Editing the ACEU portfolio repo
- New JS libraries / CDN deps
- Founders bios, metrics, or secondary CTAs
- Dark full-bleed ACEU contrast band
- Deep-link to `#work` (homepage only)

## Approach

**Hybrid studio card + footer credit** (approved).

- Cream page surface; white raised card; gold top hairline.
- Dark ACEU mark chip (triangular A in `#1f6bff` on `#05060a`).
- Subtle hover lift; respect `prefers-reduced-motion`.
- Entire card is one outbound link (`target="_blank"` + `rel="noopener noreferrer"`).

## Placement

1. New `<section id="studio">` inside `<main>`, after `#get` / CTA, before `</main>`.
2. Compact credit in `.foot-bottom` beside the copyright line.

## Copy (locked)

| Element | Text |
|---------|------|
| Eyebrow | Developed by |
| Title | ACEU AI Solutions |
| Subline | Founder-led AI studio · Intelligence, engineered. |
| Body | Needs vs Wants ships as a studio product — offline-first, built to change how you spend. |
| CTA | Visit studio → |
| Footer credit | Developed by ACEU AI Solutions |

## Visual tokens

- Host page: existing `--surface`, `--card`, `--divider`, `--gold`, `--ink`, `--muted`, `--display`, `--body`
- ACEU accent only: mark fill `#1f6bff`, mark plate `#05060a`, link/hover uses ACEU blue (not crimson)
- Motion: `translateY(-2px)` + soft shadow on hover; disabled under reduced motion

## Files

- Modify both [`website/public/index.html`](../../../website/public/index.html) and [`website/index.html`](../../../website/index.html) (keep byte-identical)
- No new asset files if the A mark is inline SVG

## Acceptance

- Section renders on desktop and mobile without horizontal scroll
- Card + footer credit open ACEU homepage in a new tab
- Both HTML copies identical
- Existing CTA/QR/flip behavior unchanged
- Reduced-motion: no hover transform animation
- Update vault `Tasks.md` + `Decisions.md` after ship

## Related

- ACEU live: https://aceu-ai-solutions-portfolio.netlify.app
- Soft-launch: https://needs-vs-wants.vercel.app
