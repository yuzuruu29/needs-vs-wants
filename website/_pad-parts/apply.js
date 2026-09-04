const fs = require('fs');
const path = require('path');
const root = path.join(__dirname, '..');
const file = path.join(root, 'public', 'index.html');
const parts = __dirname;

let html = fs.readFileSync(file, 'utf8');
const css = fs.readFileSync(path.join(parts, 'pad.css'), 'utf8');
const padHtml = fs.readFileSync(path.join(parts, 'pad.html'), 'utf8');
const padJs = fs.readFileSync(path.join(parts, 'pad.js'), 'utf8');

// CSS: replace old notepad styles through before .sheet-head
{
  const start = html.indexOf('/* ---- Ledger pad');
  const end = html.indexOf('.sheet-head{');
  if (start < 0 || end < 0) throw new Error('CSS markers missing: ' + start + ' ' + end);
  html = html.slice(0, start) + css + html.slice(end);
}

// HTML: replace sheet-wrap block
{
  const start = html.indexOf('<div class="sheet-wrap hero-in hero-in-sheet">');
  const end = html.indexOf('</div><!-- .wrap.hero-grid -->');
  if (start < 0 || end < 0) throw new Error('HTML markers missing: ' + start + ' ' + end);
  // Idempotent insert: cut at the marker line's START so the source line's
  // leading whitespace is dropped — pad.html carries its own base indent, and
  // preserving both would grow the block by 4 spaces on every apply run.
  const lineStart = html.lastIndexOf('\n', start - 1) + 1;
  html = html.slice(0, lineStart) + padHtml.trimEnd() + '\n  ' + html.slice(end);
}

// Scripts: remove any previously-injected page-flip library (flip is now native CSS/WAAPI)
{
  const tag = '<script src="https://cdn.jsdelivr.net/npm/qrcode@1.5.1/build/qrcode.min.js" crossorigin="anonymous"></script>';
  if (!html.includes(tag)) throw new Error('qrcode script missing');
  html = html.replace(/<script[^>]*page-flip@2\.0\.7[^>]*><\/script>\s*/g, '');
}

// JS: replace demo sheet through focus-demo (inclusive) up to Summary donut
{
  const start = html.indexOf('/* ---------- Demo sheet: PadController');
  const end = html.indexOf('/* ---------- Summary donut + period rotor ---------- */');
  if (start < 0 || end < 0) throw new Error('JS markers missing: ' + start + ' ' + end);
  html = html.slice(0, start) + padJs + html.slice(end);
}

// reduced-motion cleanup for old notepad selectors
html = html.replace(
  `.ticker-track,.sheet,.phone,.dial-hand,.stamp-rot .stamp-spin,.stamp-rot .stamp-pulse{animation:none !important}
  .notepad-leaf,.sheet,.notepad-stack .notepad-sheet-under{transition:none !important}
  .notepad.is-flipping .sheet{opacity:1}`,
  `.ticker-track,.sheet,.phone,.dial-hand,.stamp-rot .stamp-spin,.stamp-rot .stamp-pulse{animation:none !important}
  .pad-leaf,.sheet,.pad-stack i{transition:none !important}`
);

// Init: boot pad after load (flip is now native; page-flip not needed)
html = html.replace(
  `/* ---------- Init ---------- */
renderRows(false);
renderTicker();`,
  `/* ---------- Init ---------- */
function bootPad(){
  if ($('#todayLabel') && !$('#todayLabel').textContent){
    $('#todayLabel').textContent = now.toLocaleDateString('en-US',{month:'short',day:'numeric',year:'numeric'});
  }
  initPad();
}
if (document.readyState === 'complete') bootPad();
else window.addEventListener('load', bootPad);
renderTicker();`
);

// Dates: todayLabel may already be set — keep assignment
// Original: $('#todayLabel').textContent = ... — still works with hidden span

const mirror = path.join(root, 'index.html');
fs.writeFileSync(file, html);
fs.writeFileSync(mirror, html);
console.log('OK', file, 'bytes', html.length);
console.log('OK', mirror, 'bytes', html.length);
console.log('checks', {
  padBoard: html.includes('pad-board'),
  pflip: html.includes('pflip-stage') || html.includes('buildFlipLeaves'),
  initPad: html.includes('function initPad'),
  qrcode151: html.includes('qrcode@1.5.1'),
  apk: html.includes('needs-vs-wants-2.0.30.apk'),
  ctaPanel: html.includes('rollRedPanel'),
  noOldBind: !html.includes('notepad-bind'),
  noPageFlipLib: !html.includes('page-flip@'),
});
