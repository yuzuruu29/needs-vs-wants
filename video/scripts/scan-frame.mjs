import { readFileSync } from "node:fs";
import { PNG } from "pngjs";

const file = process.argv[2] || "out/smoke-100.png";
const png = PNG.sync.read(readFileSync(file));
const { width, height, data } = png;
const at = (x, y) => {
  const i = (y * width + x) * 4;
  if (i + 3 >= data.length) return null;
  return [data[i], data[i + 1], data[i + 2], data[i + 3]];
};
const hex = (c) => (c ? "#" + c.slice(0, 3).map((n) => n.toString(16).padStart(2, "0")).join("") : "null");
const isDark = (c) => !!c && c[0] + c[1] + c[2] < 90;

// Horizontal scan at several rows: report runs of dark pixels.
for (const y of [500, 600, 900, 1500, 1900]) {
  const row = [];
  let runStart = null;
  for (let x = 0; x < width; x += 6) {
    const c = at(x, y);
    const d = isDark(c);
    if (d && runStart === null) runStart = x;
    if (!d && runStart !== null) {
      row.push(`dark ${runStart}..${x - 6}`);
      runStart = null;
    }
  }
  if (runStart !== null) row.push(`dark ${runStart}..end`);
  console.log(`y=${y}: ${row.length ? row.join(" | ") : "(no dark px)"}`);
}

// Sample a vertical strip at x=720 to find the phone top/bottom bounds.
let phoneTop = null;
let phoneBottom = null;
for (let y = 300; y < 2400; y += 4) {
  const c = at(720, y);
  // dark bezel or non-paper color
  if (c && c[0] < 40 && c[1] < 40 && c[2] < 40 && phoneTop === null) phoneTop = y;
  if (phoneTop !== null && phoneBottom === null && c && c[0] > 200 && c[1] > 200 && c[2] > 190) phoneBottom = y;
}
console.log(`phone dark top~${phoneTop}, first light below~${phoneBottom}`);