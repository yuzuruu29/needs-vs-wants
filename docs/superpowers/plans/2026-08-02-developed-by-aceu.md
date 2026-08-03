# Developed by ACEU Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add a hybrid “Developed by ACEU AI Solutions” studio card above the footer and a compact footer credit, both linking to the ACEU homepage.

**Architecture:** Pure HTML/CSS in the existing single-file soft-launch pages. No new libraries. Section `#studio` sits inside `<main>` after `#get`; footer credit extends `.foot-bottom`. Keep `website/public/index.html` and `website/index.html` byte-identical.

**Tech Stack:** Static HTML/CSS (existing CSS variables + inline SVG mark); Vercel deploy from `website/` only.

**Spec:** [`docs/superpowers/specs/2026-08-02-developed-by-aceu-design.md`](../specs/2026-08-02-developed-by-aceu-design.md)

---

## File map

| File | Role |
|------|------|
| `website/public/index.html` | Canonical page — CSS + markup |
| `website/index.html` | Mirror copy — must match public byte-for-byte |
| Vault `Projects/Needs vs Wants/Tasks.md` | Progress log |
| Vault `Projects/Needs vs Wants/Decisions.md` | New decision (next D#) |

---

### Task 1: Pre-code gate

- [ ] Confirm Obsidian Summary/Tasks/Decisions already loaded for this session
- [ ] Context7: N/A (no new library/API)
- [ ] Graphify: scoped check on `website/` if `.graphify` exists; otherwise note N/A and proceed

### Task 2: CSS for studio section + footer credit

**Files:**
- Modify: `website/public/index.html` (footer CSS block ~840–855)

- [ ] Add styles before `/* ============ FOOTER ============ */`:

```css
/* ============ DEVELOPED BY / STUDIO ============ */
#studio{padding:28px 0 8px;position:relative;z-index:1}
.studio-card{
  display:block;text-decoration:none;color:inherit;
  background:var(--card);border:1px solid var(--divider);border-radius:var(--r-m);
  padding:28px 26px 26px;position:relative;overflow:hidden;
  box-shadow:0 18px 40px -28px rgba(26,26,26,.28);
  transition:transform .28s var(--ease-out), box-shadow .28s var(--ease-out), border-color .2s;
}
.studio-card::before{
  content:'';position:absolute;left:26px;right:26px;top:0;height:2px;
  background:linear-gradient(90deg,var(--gold),transparent 85%);
}
.studio-card:hover{
  transform:translateY(-2px);
  box-shadow:0 22px 44px -24px rgba(26,26,26,.38);
  border-color:var(--divider-strong);
}
.studio-card:focus-visible{outline:2px solid #1f6bff;outline-offset:3px}
.studio-eyebrow{
  font:600 10px/1 var(--body);letter-spacing:2.2px;text-transform:uppercase;
  color:var(--muted);margin-bottom:16px;
}
.studio-row{display:flex;align-items:center;justify-content:space-between;gap:18px;flex-wrap:wrap}
.studio-brand{display:flex;align-items:center;gap:14px;min-width:0}
.studio-mark{
  width:44px;height:44px;border-radius:12px;flex:none;
  background:#05060a;display:grid;place-items:center;
  box-shadow:inset 0 0 0 1px rgba(31,107,255,.35);
}
.studio-mark svg{width:20px;height:20px;display:block}
.studio-name{font:700 18px/1.15 var(--body);color:var(--ink);letter-spacing:.01em}
.studio-sub{font:400 13px/1.45 var(--body);color:var(--sub);margin-top:4px}
.studio-body{margin-top:16px;padding-top:14px;border-top:1px solid var(--divider);
  font:400 13.5px/1.65 var(--body);color:var(--sub);max-width:62ch}
.studio-cta{
  font:600 12px/1 var(--body);letter-spacing:1.2px;text-transform:uppercase;
  color:#1f6bff;white-space:nowrap;
}
.studio-card:hover .studio-cta{text-decoration:underline;text-underline-offset:3px}
.foot-credit a{
  color:var(--sub);text-decoration:none;border-bottom:1px solid transparent;
  transition:color .2s, border-color .2s;
}
.foot-credit a:hover{color:#1f6bff;border-bottom-color:#1f6bff}
@media (max-width:760px){
  .studio-card{padding:22px 18px 20px}
  .studio-card::before{left:18px;right:18px}
  .studio-name{font-size:16px}
}
@media (prefers-reduced-motion: reduce){
  .studio-card,.studio-card:hover{transition:none;transform:none}
}
```

- [ ] Extend `.foot-bottom` layout so copyright + credit + motto wrap cleanly (keep flex + wrap; credit as `.foot-credit` between `small` and `.motto` or inside a left cluster)

### Task 3: Markup — studio section

**Files:**
- Modify: `website/public/index.html` (after `#get` `</section>`, before `</main>`)

- [ ] Insert:

```html
<!-- ================= DEVELOPED BY ================= -->
<section id="studio" aria-label="Developed by ACEU AI Solutions">
  <div class="wrap">
    <a class="studio-card reveal"
       href="https://aceu-ai-solutions-portfolio.netlify.app"
       target="_blank" rel="noopener noreferrer">
      <p class="studio-eyebrow">Developed by</p>
      <div class="studio-row">
        <div class="studio-brand">
          <span class="studio-mark" aria-hidden="true">
            <svg viewBox="0 0 24 24"><path fill="#1f6bff" fill-rule="evenodd" d="M12 2.5 2.5 21h19Zm0 7-3.7 8h7.4Z"/></svg>
          </span>
          <span>
            <span class="studio-name">ACEU AI Solutions</span>
            <span class="studio-sub">Founder-led AI studio · Intelligence, engineered.</span>
          </span>
        </div>
        <span class="studio-cta">Visit studio →</span>
      </div>
      <p class="studio-body">Needs vs Wants ships as a studio product — offline-first, built to change how you spend.</p>
    </a>
  </div>
</section>
```

### Task 4: Markup — footer credit

**Files:**
- Modify: `website/public/index.html` (`.foot-bottom`)

- [ ] Add compact credit (same URL/attrs as card):

```html
<small class="foot-credit">Developed by
  <a href="https://aceu-ai-solutions-portfolio.netlify.app" target="_blank" rel="noopener noreferrer">ACEU AI Solutions</a>
</small>
```

Place it in the bottom bar so it reads with the copyright without crowding the motto.

### Task 5: Mirror sync + verify

- [ ] Copy `website/public/index.html` → `website/index.html` (byte-identical)
- [ ] Spot-check: no horizontal overflow at ~390px and desktop; hover/focus; reduced-motion
- [ ] Confirm CTA QR / notepad flip untouched
- [ ] Optional: local Playwright smoke if already used for site QA

### Task 6: Memory + deploy (when user asks to ship)

- [ ] Append decision to vault `Decisions.md` (Developed by hybrid card + homepage link)
- [ ] Update vault `Tasks.md` progress log
- [ ] Deploy from `website/` only; re-alias `needs-vs-wants.vercel.app` if needed
- [ ] Do **not** commit unless user explicitly asks

---

## Done when

- `#studio` card and footer credit both link to ACEU homepage in a new tab
- Visual matches approved hybrid studio-card design
- Both HTML files identical; no new deps; locked QR/CTA/flip intact
