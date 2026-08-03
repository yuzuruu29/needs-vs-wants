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
page.on('console', m => console.log('C', m.text()));

await page.goto(`http://127.0.0.1:${port}/`, { waitUntil: 'load' });
await page.waitForTimeout(700);

// expose internals by monkeypatching after boot
await page.evaluate(() => {
  window.__padDebug = true;
});

// next without seal first
await page.locator('#pageNextTab').click();
await page.waitForTimeout(1200);
console.log('A next', await page.evaluate(() => document.querySelector('#pagePos').textContent));

// prev without seal
await page.locator('#pagePrevTab').click();
await page.waitForTimeout(1200);
console.log('B prev', await page.evaluate(() => document.querySelector('#pagePos').textContent));

// next again
await page.locator('#pageNextTab').click();
await page.waitForTimeout(1200);
console.log('C next', await page.evaluate(() => document.querySelector('#pagePos').textContent));

// seal on page 2
await page.locator('.sheet.is-live #fItem').fill('X');
await page.locator('.sheet.is-live #fCost').fill('1');
await page.locator('.sheet.is-live .type-chip.chip-need').click();
await page.waitForTimeout(300);
console.log('D seal', await page.evaluate(() => ({
  page: document.querySelector('#pagePos').textContent,
  row: document.querySelector('.sheet.is-live .l-item')?.textContent
})));

// instrument flip
const probe = await page.evaluate(() => {
  // Find pageFlip via stage - not exposed. Hook buttons.
  return {
    prevDisabled: document.getElementById('pagePrevTab').disabled,
    nextDisabled: document.getElementById('pageNextTab').disabled,
  };
});
console.log('probe', probe);

await page.locator('#pagePrevTab').click();
await page.waitForTimeout(1500);
console.log('E prev after seal', await page.evaluate(() => ({
  page: document.querySelector('#pagePos').textContent,
  live: document.querySelector('.sheet.is-live')?.dataset.sheet,
  rows: [...document.querySelectorAll('.sheet.is-live .l-item')].map(e => e.textContent)
})));

// Try turnToPage via re-clicking prev multiple times
await page.locator('#pagePrevTab').click();
await page.waitForTimeout(1500);
console.log('F prev again', await page.evaluate(() => document.querySelector('#pagePos').textContent));

await browser.close();
server.close();
