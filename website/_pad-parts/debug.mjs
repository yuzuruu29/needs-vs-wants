import { chromium } from 'playwright';
import { createServer } from 'http';
import { readFileSync } from 'fs';
import { join, extname } from 'path';
import { fileURLToPath } from 'url';

const root = join(fileURLToPath(new URL('.', import.meta.url)), '..', 'public');
const mime = { '.html': 'text/html', '.js': 'application/javascript', '.css': 'text/css', '.apk': 'application/octet-stream' };
const server = createServer((req, res) => {
  let p = req.url.split('?')[0];
  if (p === '/') p = '/index.html';
  try {
    const data = readFileSync(join(root, p.replace(/^\//, '')));
    res.writeHead(200, { 'Content-Type': mime[extname(p)] || 'text/plain' });
    res.end(data);
  } catch { res.writeHead(404); res.end('no'); }
});
await new Promise(r => server.listen(0, r));
const port = server.address().port;

const browser = await chromium.launch();
const page = await browser.newPage();
page.on('console', m => console.log('CONSOLE', m.type(), m.text()));
page.on('pageerror', e => console.log('PAGEERROR', e.message));

await page.goto(`http://127.0.0.1:${port}/`, { waitUntil: 'load' });
await page.waitForTimeout(800);

const info = await page.evaluate(() => {
  const stage = document.getElementById('padStage');
  return {
    stageClass: stage?.className,
    children: stage?.children?.length,
    pflipMode: window.__pflipMode,
    leaves: stage?.querySelectorAll('.pflip').length,
    fronts: stage?.querySelectorAll('.pflip__face--front').length,
    backs: stage?.querySelectorAll('.pflip__face--back').length,
    hasOverlay: !!document.getElementById('liveOverlay'),
  };
});
console.log('stage', info);

// Click next and watch for 2s
await page.click('#pageNextTab');
await page.waitForTimeout(260);
const mid = await page.evaluate(() => ({
  page: document.getElementById('pagePos')?.textContent,
  flippingClass: document.getElementById('notepad')?.className,
  moving: !!document.querySelector('.pflip.is-turn'),
  anims: document.querySelector('.pflip.is-turn')?.getAnimations().map(a => a.playState),
}));
console.log('midflip', JSON.stringify(mid, null, 2));
await page.waitForTimeout(1200);
const after = await page.evaluate(() => ({
  page: document.getElementById('pagePos')?.textContent,
  flippingClass: document.getElementById('notepad')?.className,
  liveSheet: document.querySelector('.sheet.is-live')?.dataset.sheet,
  disabled: document.getElementById('pageNextTab')?.disabled,
}));
console.log('after click', JSON.stringify(after, null, 2));

await browser.close();
server.close();
