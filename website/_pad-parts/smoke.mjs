import { chromium } from 'playwright';
import { createServer } from 'http';
import { readFileSync, statSync } from 'fs';
import { join, extname } from 'path';
import { fileURLToPath } from 'url';

const root = join(fileURLToPath(new URL('.', import.meta.url)), '..', 'public');
const mime = { '.html': 'text/html', '.js': 'application/javascript', '.css': 'text/css', '.apk': 'application/vnd.android.package-archive' };

const server = createServer((req, res) => {
  let p = req.url.split('?')[0];
  if (p === '/') p = '/index.html';
  const file = join(root, p.replace(/^\//, ''));
  try {
    const data = readFileSync(file);
    res.writeHead(200, { 'Content-Type': mime[extname(file)] || 'application/octet-stream' });
    res.end(data);
  } catch {
    res.writeHead(404); res.end('no');
  }
});

await new Promise(r => server.listen(0, r));
const port = server.address().port;
const url = `http://127.0.0.1:${port}/`;
console.log('serving', url);

const browser = await chromium.launch();
const page = await browser.newPage({ viewport: { width: 1280, height: 900 } });
const logs = [];
page.on('console', m => logs.push(m.type() + ': ' + m.text()));
page.on('pageerror', e => logs.push('PAGEERROR: ' + e.message));

await page.goto(url, { waitUntil: 'load', timeout: 30000 });
await page.waitForTimeout(700);

const mode = await page.evaluate(() => {
  return {
    pflipMode: window.__pflipMode,
    leafCount: document.querySelectorAll('.pflip').length,
    faceFront: document.querySelectorAll('.pflip__face--front').length,
    faceBack: document.querySelectorAll('.pflip__face--back').length,
    pageText: document.querySelector('#pagePos')?.textContent,
    total: document.querySelector('#pageTotal')?.textContent,
    hasLive: !!document.querySelector('.sheet.is-live #fItem'),
    hasOverlay: !!document.getElementById('liveOverlay'),
    hasWrap: !!document.getElementById('padStageWrap'),
    libLoaded: [...document.scripts].some(s => (s.src || '').includes('page-flip@')),
  };
});
console.log('boot', mode);
await page.screenshot({ path: '_pad-parts/shot-1-boot.png' });

// seal on page 1
await page.fill('#fItem', 'Smoke test snack');
await page.fill('#fCost', '12.50');
await page.click('.type-chip.chip-want');
await page.waitForTimeout(200);
const afterSeal = await page.evaluate(() => ({
  count: document.querySelector('.sheet-count')?.textContent,
  row: document.querySelector('.l-item')?.textContent,
}));
console.log('seal', afterSeal);

// flip next — probe mid-flip (is-turn + sheen active), then settled state
await page.click('#pageNextTab');
await page.waitForTimeout(260);
const midFlip = await page.evaluate(() => {
  const notepad = document.getElementById('notepad');
  const moving = document.querySelector('.pflip.is-turn');
  const sheen = moving ? moving.querySelector('.pflip__sheen') : null;
  const cast = document.querySelector('.pflip-shadow');
  return {
    flipping: notepad?.classList.contains('is-flipping'),
    turnClass: !!moving,
    turnNext: !!document.querySelector('.pflip.is-turn-next'),
    animsRunning: moving ? moving.getAnimations().filter(a => a.playState === 'running').length : 0,
    sheenAnimating: sheen ? getComputedStyle(sheen).animationName !== 'none' : false,
    castAnimating: cast ? getComputedStyle(cast).animationName !== 'none' : false,
  };
});
await page.screenshot({ path: '_pad-parts/shot-2-midflip.png' });
console.log('midflip', midFlip);

await page.waitForTimeout(900);
const afterNext = await page.evaluate(() => ({
  page: document.querySelector('#pagePos')?.textContent,
  total: document.querySelector('#pageTotal')?.textContent,
  live: !!document.querySelector('#fItem'),
  flipping: document.getElementById('notepad')?.classList.contains('is-flipping'),
}));
console.log('next', afterNext);

// seal on page 2
if (afterNext.live) {
  await page.fill('#fItem', 'Page two item');
  await page.fill('#fCost', '3.00');
  await page.click('.type-chip.chip-need');
  await page.waitForTimeout(200);
}
const mid = await page.evaluate(() => ({
  page: document.querySelector('#pagePos')?.textContent,
  row: document.querySelector('.l-item')?.textContent,
}));
console.log('page2 seal', mid);

// flip prev — probe reverse direction
await page.click('#pagePrevTab');
await page.waitForTimeout(260);
const midPrev = await page.evaluate(() => ({
  turnPrev: !!document.querySelector('.pflip.is-turn-prev'),
  turnClass: !!document.querySelector('.pflip.is-turn'),
}));
console.log('midprev', midPrev);
await page.waitForTimeout(900);
const afterPrev = await page.evaluate(() => ({
  page: document.querySelector('#pagePos')?.textContent,
  firstRow: document.querySelector('.l-item')?.textContent,
  live: !!document.querySelector('#fItem'),
}));
console.log('prev', afterPrev);

// flip forward again (go to page 2)
await page.click('#pageNext');
await page.waitForTimeout(1100);
const again = await page.evaluate(() => ({
  page: document.querySelector('#pagePos')?.textContent,
  row: document.querySelector('.l-item')?.textContent,
}));
console.log('forward', again);

// dot-jump: back to page 1 via dots
await page.click('.page-dot[data-page="0"]');
await page.waitForTimeout(1100);
const dot = await page.evaluate(() => ({
  page: document.querySelector('#pagePos')?.textContent,
  row: document.querySelector('.l-item')?.textContent,
}));
console.log('dot', dot);
await page.screenshot({ path: '_pad-parts/shot-3-settled.png' });

// QR / CTA regression (library must be GONE, qrcode kept)
const cta = await page.evaluate(() => ({
  apk: document.querySelector('#apkDownload')?.getAttribute('href'),
  qrScript: [...document.scripts].some(s => (s.src || '').includes('qrcode@1.5.1')),
  flipScript: [...document.scripts].some(s => (s.src || '').includes('page-flip@2.0.7')),
}));
console.log('locked', cta);

const errs = logs.filter(l => l.startsWith('error') || l.startsWith('PAGEERROR'));
console.log('console errors', errs.length ? errs : 'none');
console.log('pad logs', logs.filter(l => l.includes('[pad]')).slice(0, 5));

await browser.close();
server.close();

const ok =
  mode.pflipMode === 'flip' &&
  mode.leafCount >= 2 &&
  mode.faceBack >= 2 &&
  mode.hasLive &&
  mode.hasOverlay &&
  mode.hasWrap &&
  !mode.libLoaded &&
  midFlip.turnClass &&
  midFlip.turnNext &&
  midPrev.turnPrev &&
  afterSeal.row === 'Smoke test snack' &&
  afterNext.page === '2' &&
  afterPrev.page === '1' &&
  afterPrev.firstRow === 'Smoke test snack' &&
  again.row === 'Page two item' &&
  dot.page === '1' &&
  cta.apk.includes('needs-vs-wants-1.0.0.apk') &&
  cta.qrScript &&
  !cta.flipScript &&
  errs.length === 0;

console.log(ok ? 'SMOKE PASS' : 'SMOKE FAIL');
process.exit(ok ? 0 : 1);
