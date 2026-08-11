/**
 * One-shot patch for Workstreams D+E on public/index.html.
 * Run: node website/_parts/workstream-de.js
 */
const fs = require('fs');
const path = require('path');

const root = path.join(__dirname, '..');
const file = path.join(root, 'public', 'index.html');
let html = fs.readFileSync(file, 'utf8');

const fontFaces = `
/* ============ SELF-HOSTED FONTS ============ */
@font-face{
  font-family:'Playfair Display SC';
  font-style:normal;font-weight:700;font-display:swap;
  src:url('./fonts/playfair-display-sc-700.woff2') format('woff2');
}
@font-face{
  font-family:'Inter';
  font-style:normal;font-weight:400;font-display:swap;
  src:url('./fonts/inter-400.woff2') format('woff2');
}
@font-face{
  font-family:'Inter';
  font-style:normal;font-weight:500;font-display:swap;
  src:url('./fonts/inter-500.woff2') format('woff2');
}
@font-face{
  font-family:'Inter';
  font-style:normal;font-weight:600;font-display:swap;
  src:url('./fonts/inter-600.woff2') format('woff2');
}
@font-face{
  font-family:'Inter Tight';
  font-style:normal;font-weight:600;font-display:swap;
  src:url('./fonts/inter-tight-600.woff2') format('woff2');
}
`;

html = html.replace(
  /<link rel="preconnect" href="https:\/\/fonts\.googleapis\.com">\s*<link rel="preconnect" href="https:\/\/fonts\.gstatic\.com" crossorigin>\s*<link href="https:\/\/fonts\.googleapis\.com\/css2[^"]+" rel="stylesheet">\s*/,
  ''
);

if (!html.includes('SELF-HOSTED FONTS')) {
  html = html.replace('<style>', '<style>' + fontFaces);
}

html = html.replace(
  /--ink:#1A1A1A; --sub:#5A5A5A; --muted:#8A8A8A;/,
  '--ink:#1A1A1A; --sub:#5A5A5A; --muted:#8A8A8A;\n  --paypal:#0070ba; --aceu-blue:#1f6bff; --paper-line:#f7f5ef; --advisor-muted:#4a4a4a; --advisor-soft:#777;'
);

html = html.replace(
  /--display:'Playfair Display SC', Georgia, 'Times New Roman', serif;\s*--body:'Inter', -apple-system, 'Segoe UI', Helvetica, sans-serif;\s*--money:'Inter Tight','Inter', ui-monospace, 'SF Mono', Menlo, monospace;/,
  "--display:'Playfair Display SC', Georgia, 'Times New Roman', serif;\n  --body:'Inter', -apple-system, 'Segoe UI', Helvetica, sans-serif;\n  --money:'Inter Tight', 'Inter', ui-monospace, 'SF Mono', Menlo, monospace;"
);

// Header shrink + backdrop fallback
if (!html.includes('header.is-shrunk')) {
  html = html.replace(
    'header.scrolled{\n  border-color:var(--divider);',
    'header.is-shrunk .nav{padding:10px 24px}\nheader.scrolled{\n  border-color:var(--divider);'
  );
  html = html.replace(
    '.nav{display:flex;align-items:center;gap:20px;padding:14px 24px;max-width:1180px;margin:0 auto}',
    '.nav{display:flex;align-items:center;gap:20px;padding:14px 24px;max-width:1180px;margin:0 auto;transition:padding .35s var(--ease-soft)}'
  );
  html = html.replace(
    'header{\n  position:sticky;top:0;z-index:50;',
    'header{\n  position:sticky;top:0;z-index:50;max-height:72px;\n  @supports not ((backdrop-filter: blur(1px)) or (-webkit-backdrop-filter: blur(1px))){\n    background:rgba(250,250,247,.96);\n  }'
  );
}

html = html.replace(
  '.studio-card:focus-visible{outline:2px solid #1f6bff;outline-offset:3px}',
  '.studio-card:focus-visible{outline:2px solid var(--aceu-blue);outline-offset:3px}'
);
html = html.replace(
  'color:#1f6bff;white-space:nowrap;',
  'color:var(--aceu-blue);white-space:nowrap;'
);
html = html.replace(
  '.foot-credit a:hover{color:#1f6bff;border-bottom-color:#1f6bff}',
  '.foot-credit a:hover{color:var(--aceu-blue);border-bottom-color:var(--aceu-blue)}'
);
html = html.replace(
  'repeating-linear-gradient(180deg,#f7f5ef 0 28px,rgba(26,26,26,.04) 28px 29px);',
  'repeating-linear-gradient(180deg,var(--paper-line) 0 28px,rgba(26,26,26,.04) 28px 29px);'
);

html = html.replace(
  '<div class="ticker" aria-hidden="true">',
  '<div class="ticker reveal" aria-hidden="true">'
);
html = html.replace(
  '<footer>',
  '<footer class="reveal">'
);

// Eyebrow rationing
html = html.replace(
  '<p class="eyebrow eyebrow--gold">The Rule</p>\n        <h2 class="title">',
  '<h2 class="title">'
);
html = html.replace(
  '<p class="eyebrow eyebrow--gold">The Window</p>\n      <h2 class="title">',
  '<h2 class="title">'
);
html = html.replace(
  '<p class="eyebrow eyebrow--gold">Inside the Diary</p>\n        <h2 class="title">',
  '<h2 class="title">'
);
html = html.replace(
  '<p class="eyebrow">Official Launch · <span id="launchDate">Version 1.4.0</span></p>\n          <h2>',
  '<h2>'
);

// CTAs
html = html.replace('id="heroCta" type="button">Log your first expense', 'id="heroCta" type="button">Log an expense');
html = html.replace(
  'Download Android APK</a>',
  'Get the app</a>'
);
html = html.replace(
  '<a class="btn btn--primary btn-arr" href="#get">\n                Upgrade to Max',
  '<a class="btn btn--primary btn-arr" href="#get">\n                Get the app'
);
html = html.replace(
  'id="payPalSeamBtn" style="background:#0070ba;border-color:#0070ba"',
  'id="payPalSeamBtn" style="background:var(--paypal);border-color:var(--paypal)"'
);

// Advisor preview copy
html = html.replace(
  '<b style="display:block;font-size:12px;margin:4px 0">Budgetary Equilibrium Active</b>',
  '<b style="display:block;font-size:12px;margin:4px 0">Needs lead the ledger today</b>'
);
html = html.replace(
  '<p style="font-size:11px;color:#4a4a4a;line-height:1.4">Based on your Economic Study Notebook #1, essential Needs form the anchor of your spending velocity.</p>',
  '<p style="font-size:11px;color:var(--advisor-muted);line-height:1.4">From your Economic Study notebook: keep Needs ahead of Wants before you seal another line.</p>'
);
html = html.replace(
  '<span style="font-size:9px;color:#777">NotebookLM Section 1.2</span>',
  '<span style="font-size:9px;color:var(--advisor-soft)">NotebookLM Section 1.2</span>'
);
html = html.replace(
  '<path fill="#1f6bff" fill-rule="evenodd"',
  '<path fill="var(--aceu-blue)" fill-rule="evenodd"'
);

// #get desc
html = html.replace(
  '<p class="desc">Take control of your finances. Every expense counts — seal it as a Need or a Want, keep a 30-day window of honest habits, and let an optional daily budget keep today in check.</p>',
  '<p class="desc">Start the diary. Seal the choice. Every line is Need or Want, kept for 30 days, with an optional daily budget on today.</p>'
);

// Visible em-dash / en-dash purge in body (HTML + inline script strings)
const bodyStart = html.indexOf('<body>');
const bodyEnd = html.lastIndexOf('</body>');
if (bodyStart >= 0 && bodyEnd > bodyStart) {
  const before = html.slice(0, bodyStart);
  let body = html.slice(bodyStart, bodyEnd);
  body = body.split('—').join(', ');
  body = body.split('–').join('-');
  html = before + body + html.slice(bodyEnd);
}

// Specific copy fixes after generic dash replace
html = html.replace(
  'Every purchase is either a <strong>need</strong> or a <strong>want</strong>,  and this diary',
  'Every purchase is either a <strong>need</strong> or a <strong>want</strong>, and this diary'
);

// Title/meta (head)
html = html.replace(
  '<title>Needs vs Wants — Expense Tracker · Official Launch</title>',
  '<title>Needs vs Wants: Expense Tracker, Official Launch</title>'
);
html = html.replace(
  'content="An offline-first expense trainer. Every purchase is a Need or a Want — decided in the moment, sealed instantly, kept for 30 days. Optional daily budget. No accounts. No analytics."',
  'content="An offline-first expense trainer. Every purchase is a Need or a Want, decided in the moment, sealed instantly, kept for 30 days. Optional daily budget. No accounts. No analytics."'
);

// JS: count-up + window dial + magnetic + header shrink class
if (!html.includes('function animateMoneyCents')) {
  const injectAfter = 'function setPeriod(p){';
  const countFn = `
function animateMoneyCents(el, targetCents, duration){
  if (!el || matchMedia('(prefers-reduced-motion: reduce)').matches){
    el.dataset.cents = targetCents;
    el.textContent = fmt(targetCents);
    return;
  }
  const start = 0;
  const startTime = performance.now();
  function tick(now){
    const t = Math.min(1, (now - startTime) / duration);
    const eased = 1 - Math.pow(1 - t, 3);
    const val = Math.round(start + (targetCents - start) * eased);
    el.dataset.cents = val;
    el.textContent = fmt(val);
    if (t < 1) requestAnimationFrame(tick);
  }
  requestAnimationFrame(tick);
}
function animateDialDays(el, target, duration){
  if (!el || matchMedia('(prefers-reduced-motion: reduce)').matches){ el.textContent = target; return; }
  const start = 0;
  const startTime = performance.now();
  function tick(now){
    const t = Math.min(1, (now - startTime) / duration);
    const eased = 1 - Math.pow(1 - t, 3);
    el.textContent = Math.round(start + (target - start) * eased);
    if (t < 1) requestAnimationFrame(tick);
  }
  requestAnimationFrame(tick);
}
let donutCountPlayed = false;
`;
  html = html.replace(injectAfter, countFn + injectAfter);

  html = html.replace(
    'function setPeriod(p){',
    'function setPeriod(p, animateCount){'
  );
  html = html.replace(
    'const dt = $(\'#donutTotal\'); dt.dataset.cents = total; dt.textContent = fmt(total);',
    "const dt = $('#donutTotal');\n  if (animateCount){ animateMoneyCents(dt, total, 1100); }\n  else { dt.dataset.cents = total; dt.textContent = fmt(total); }"
  );

  html = html.replace(
    'ents.forEach(en => { if (en.isIntersecting && !donutSeen){ donutSeen = true; setPeriod(\'all\'); io.disconnect(); } });',
    "ents.forEach(en => { if (en.isIntersecting && !donutSeen){ donutSeen = true; donutCountPlayed = true; setPeriod('all', true); io.disconnect(); } });"
  );

  html = html.replace(
    '},{threshold:.35}).observe($(\'.donut\'));',
    "},{threshold:.35}).observe($('.donut'));\n\nlet windowSeen = false;\nnew IntersectionObserver((ents, io) => {\n  ents.forEach(en => {\n    if (en.isIntersecting && !windowSeen){\n      windowSeen = true;\n      const n = document.querySelector('#window .dial-center .n');\n      if (n) animateDialDays(n, 30, 900);\n      io.disconnect();\n    }\n  });\n},{threshold:.35}).observe($('#window'));"
  );

  html = html.replace(
    'addEventListener(\'scroll\', () => hdr.classList.toggle(\'scrolled\', scrollY > 8), {passive:true});',
    "addEventListener('scroll', () => {\n  const y = scrollY > 8;\n  hdr.classList.toggle('scrolled', y);\n  hdr.classList.toggle('is-shrunk', y);\n}, {passive:true});\n\n(function initMagneticCtas(){\n  if (!matchMedia('(pointer:fine)').matches || matchMedia('(prefers-reduced-motion: reduce)').matches) return;\n  const magnets = [$('#payPalSeamBtn'), $('#apkDownload'), $('header a.btn--primary[href=\"#get\"]')].filter(Boolean);\n  magnets.forEach(btn => {\n    btn.addEventListener('mousemove', e => {\n      const r = btn.getBoundingClientRect();\n      const dx = ((e.clientX - r.left) / r.width - 0.5) * 6;\n      const dy = ((e.clientY - r.top) / r.height - 0.5) * 6;\n      btn.animate({ transform: `translate(${dx}px, ${dy}px)` }, { duration: 120, fill: 'forwards' });\n    });\n    btn.addEventListener('mouseleave', () => {\n      btn.animate({ transform: 'translate(0, 0)' }, { duration: 200, fill: 'forwards' });\n    });\n  });\n})();"
  );
}

// rangeLabel placeholder (was em dash)
html = html.replace('id="rangeLabel">—</span>', 'id="rangeLabel">·</span>');

fs.writeFileSync(file, html);
console.log('patched', file, html.length);
