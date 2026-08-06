// Find bounding boxes of green/crimson/gold clusters in a frame.
// Usage: node scripts/clusters.mjs <png>
import { readFileSync } from "node:fs";
import { PNG } from "pngjs";

const file = process.argv[2];
if (!file) process.exit(1);
const png = PNG.sync.read(readFileSync(file));
const { width, height, data } = png;

const greenish = (c) => c[1] > 90 && c[1] > c[0] * 1.5 && c[1] > c[2] * 1.4 && c[0] < 70;
const redish = (c) => c[0] > 140 && c[1] < 80 && c[2] < 100;
const goldish = (c) => c[0] > 200 && c[1] > 140 && c[1] < 210 && c[2] < 120;

const find = (pred, label) => {
  const pts = [];
  for (let y = 78; y < height; y += 2) {
    for (let x = 0; x < width; x += 2) {
      const i = (y * width + x) * 4;
      const c = [data[i], data[i + 1], data[i + 2]];
      if (pred(c)) pts.push([x, y]);
    }
  }
  if (!pts.length) {
    console.log(`${label}: none`);
    return;
  }
  // simple 1D clustering on y then x, report groups
  pts.sort((a, b) => a[1] - b[1]);
  const groups = [];
  let cur = [pts[0]];
  for (let i = 1; i < pts.length; i++) {
    if (pts[i][1] - cur[cur.length - 1][1] > 60) {
      groups.push(cur);
      cur = [pts[i]];
    } else {
      cur.push(pts[i]);
    }
  }
  groups.push(cur);
  groups.forEach((g) => {
    const xs = g.map((p) => p[0]);
    const ys = g.map((p) => p[1]);
    const cx = Math.round(xs.reduce((a, b) => a + b, 0) / g.length);
    const cy = Math.round(ys.reduce((a, b) => a + b, 0) / g.length);
    const n = g.length;
    console.log(`${label} group n=${n} x[${Math.min(...xs)}..${Math.max(...xs)}] y[${Math.min(...ys)}..${Math.max(...ys)}] centroid(${cx},${cy})`);
  });
};

find(greenish, "GREEN");
find(redish, "CRIMSON");
find(goldish, "GOLD");