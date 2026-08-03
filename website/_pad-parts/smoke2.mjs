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
const logs = [];
page.on('console', m => logs.push(m.text()));
page.on('pageerror', e => logs.push('ERR ' + e.message));

await page.goto(`http://127.0.0.1:${port}/`, { waitUntil: 'load' });
await page.waitForTimeout(700);

await page.locator('.sheet.is-live #fItem').fill('Smoke test snack');
await page.locator('.sheet.is-live #fCost').fill('12.50');
await page.locator('.sheet.is-live .type-chip.chip-want').click();
await page.waitForTimeout(300);
console.log('seal', await page.evaluate(() => ({
  count: document.querySelector('.sheet.is-live .sheet-count')?.textContent,
  row: document.querySelector('.sheet.is-live .l-item')?.textContent,
  page: document.querySelector('#pagePos')?.textContent,
})));

const hBefore = await page.evaluate(() => document.querySelector('.sheet-wrap')?.getBoundingClientRect().height || 0);
await page.locator('#pageNextTab').click();
await page.waitForTimeout(1200);
console.log('next', await page.evaluate(() => ({
  page: document.querySelector('#pagePos')?.textContent,
  liveSheet: document.querySelector('.sheet.is-live')?.dataset.sheet,
  ghost: !!document.querySelector('.sheet.is-live .sheet-ghost'),
  fItem: !!document.querySelector('.sheet.is-live #fItem'),
})));

await page.locator('.sheet.is-live #fItem').fill('Page two item');
await page.locator('.sheet.is-live #fCost').fill('3.00');
await page.locator('.sheet.is-live .type-chip.chip-need').click();
await page.waitForTimeout(300);
console.log('p2 seal', await page.evaluate(() => ({
  page: document.querySelector('#pagePos')?.textContent,
  row: document.querySelector('.sheet.is-live .l-item')?.textContent,
})));

await page.locator('#pagePrevTab').click();
await page.waitForTimeout(1200);
console.log('prev', await page.evaluate(() => ({
  page: document.querySelector('#pagePos')?.textContent,
  first: document.querySelector('.sheet.is-live .l-item')?.textContent,
})));

await page.locator('#pageNextTab').click();
await page.waitForTimeout(1200);
console.log('fwd', await page.evaluate(() => ({
  page: document.querySelector('#pagePos')?.textContent,
  row: document.querySelector('.sheet.is-live .l-item')?.textContent,
})));

const hAfter = await page.evaluate(() => document.querySelector('.sheet-wrap')?.getBoundingClientRect().height || 0);
console.log('shift', Math.abs(hAfter - hBefore), { hBefore, hAfter });
console.log('errors', logs.filter(l => /error|ERR|Error/i.test(l)));
console.log('pad logs', logs.filter(l => l.includes('[pad]')));

await browser.close();
server.close();
