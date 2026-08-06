// Poor-man's vision QA: sample pixels in a rendered still and report the
// colors found at named probe points. Run: node scripts/still-check.mjs <png>
import { readFileSync } from "node:fs";
import { PNG } from "pngjs";

const file = process.argv[2];
if (!file) {
  console.error("usage: node scripts/still-check.mjs <png>");
  process.exit(1);
}
const png = PNG.sync.read(readFileSync(file));
const { width, height, data } = png;
const at = (x, y) => {
  x = Math.round(x);
  y = Math.round(y);
  if (x < 0 || y < 0 || x >= width || y >= height) return null;
  const i = (y * width + x) * 4;
  return [data[i], data[i + 1], data[i + 2], data[i + 3]];
};
const hex = ([r, g, b]) => {
  if ([r, g, b].some((n) => n === undefined)) return "null";
  const to = (n) => n.toString(16).padStart(2, "0");
  return "#" + to(r) + to(g) + to(b);
};
const isDark = (c) => !!c && c[0] + c[1] + c[2] < 90;

console.log(`size: ${width}x${height}`);

// Expected geometry (LAYOUT)
const PHONE = { L: 390, T: 600, W: 660, H: 1360 };
const CONTENT = { L: 414, T: 708, W: 612, H: 1226 };

const probes = [
  ["bg top-left (paper)", 60, 120],
  ["bg top-right (paper)", 1380, 120],
  ["phone bezel left", PHONE.L + 8, 900],
  ["phone bezel right", PHONE.L + PHONE.W - 8, 900],
  ["screen bg (mid-right)", CONTENT.L + CONTENT.W - 40, 900],
  ["screen center (donut area)", 720, 1240],
  ["status bar pill", 720, PHONE.T + 34 + 40],
  ["cta button area", 720, CONTENT.T + CONTENT.H - 70],
  ["outside phone bottom (paper)", 720, 2100],
  ["below phone (paper)", 720, 2620 - 300],
];
for (const [name, x, y] of probes) {
  const c = at(x, y);
  console.log(`${name.padEnd(28)} (${x},${y}) ${hex(c)} ${isDark(c) ? "DARK" : ""}`);
}

// Count dark pixels in phone bezel frame band to confirm the bezel is present.
let bezelDark = 0;
for (let px = PHONE.L; px < PHONE.L + PHONE.W; px += 4) {
  const c = at(px, PHONE.T + 20);
  if (isDark(c)) bezelDark++;
}
console.log(`bezel top-band dark px: ${bezelDark} (expect ~165 of 165)`);

// Count green-ish and red-ish pixels in screen to confirm donut arcs.
const greenish = (c) => c[1] > 90 && c[1] > c[0] && c[1] > c[2] && c[0] < 60;
const redish = (c) => c[0] > 150 && c[1] < 80 && c[2] < 90;
let greens = 0;
let reds = 0;
for (let y = CONTENT.T; y < CONTENT.T + CONTENT.H; y += 5) {
  for (let x = CONTENT.L; x < CONTENT.L + CONTENT.W; x += 5) {
    const c = at(x, y);
    if (greenish(c)) greens++;
    if (redish(c)) reds++;
  }
}
console.log(`screen greenish px: ${greens}, redish px: ${reds}`);