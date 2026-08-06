// Count dark (ink) pixels in horizontal bands — verifies text/type presence.
// Usage: node scripts/bands.mjs <png> bandlabel y0 y1 [y0 y1]*
import { readFileSync } from "node:fs";
import { PNG } from "pngjs";

const file = process.argv[2];
const png = PNG.sync.read(readFileSync(file));
const { width, height, data } = png;
const bands = [];
for (let i = 3; i + 1 < process.argv.length; i += 3) {
  bands.push([process.argv[i], Number(process.argv[i + 1]), Number(process.argv[i + 2])]);
}
for (const [label, y0, y1] of bands) {
  let dark = 0;
  let total = 0;
  for (let y = y0; y < Math.min(y1, height); y += 2) {
    for (let x = 80; x < width - 80; x += 2) {
      const i = (y * width + x) * 4;
      const c = data[i] + data[i + 1] + data[i + 2];
      if (c < 330) dark++; // near-ink (text)
      total++;
    }
  }
  console.log(`${label.padEnd(24)} ink=${dark} (${Math.round((dark / total) * 1000) / 10}‰)`);
}