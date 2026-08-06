// ASCII color map of a rendered frame — lets me "see" composition in text.
// Usage: node scripts/ascii-map.mjs <png> [cols] [rows]
import { readFileSync } from "node:fs";
import { PNG } from "pngjs";

const file = process.argv[2];
if (!file) process.exit(1);
const cols = Number(process.argv[3] || 48);
const rows = Number(process.argv[4] || 56);
const png = PNG.sync.read(readFileSync(file));
const { width, height, data } = png;
const cw = width / cols;
const ch = height / rows;

const classify = (r, g, b) => {
  if (r + g + b < 90) return "#"; // dark (bezel, ink)
  if (r < 90 && g > 90 && g > r * 1.4) return "g"; // green (need)
  if (r > 140 && g < 90 && b < 100) return "r"; // crimson (want)
  if (r > 200 && g > 140 && b < 110) return "o"; // gold
  if (r > 240 && g > 235 && b > 225) return " "; // white/near-white (cards)
  return "."; // light paper
};

let out = "";
for (let ry = 0; ry < rows; ry++) {
  let line = "";
  for (let rx = 0; rx < cols; rx++) {
    const x0 = Math.floor(rx * cw);
    const y0 = Math.floor(ry * ch);
    // sample a few points within the cell
    const counts = { "#": 0, g: 0, r: 0, o: 0, " ": 0, ".": 0 };
    for (let dy = Math.floor(ch * 0.35); dy < ch * 0.65; dy += Math.max(2, Math.floor(ch * 0.04))) {
      for (let dx = Math.floor(cw * 0.35); dx < cw * 0.65; dx += Math.max(2, Math.floor(cw * 0.04))) {
        const i = ((y0 + dy) * width + (x0 + dx)) * 4;
        if (i + 3 >= data.length) continue;
        counts[classify(data[i], data[i + 1], data[i + 2])]++;
      }
    }
    // majority wins, dark has priority
    const best = Object.entries(counts).sort((a, b) => b[1] - a[1])[0][0];
    line += best === "#" ? "#" : best === "g" ? "g" : best === "r" ? "R" : best === "o" ? "o" : best === " " ? "." : "·";
  }
  out += line + "\n";
}
console.log(out);
console.log(`legend: # dark · g green · R crimson · o gold · . white/card · · paper`);