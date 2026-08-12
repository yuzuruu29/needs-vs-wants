#!/usr/bin/env node
// Needs vs Wants — release ritual automation.
//
//   node tools/release.mjs [--dry-run] <newVersionName>   # default: dry-run, prints the plan, MODIFIES NOTHING
//   node tools/release.mjs --yes <newVersionName>         # actually executes every step
//
// Steps (see tools/README.md):
//   1. bump versionCode/versionName in app/build.gradle.kts
//   2. gradlew :app:testFullDebugUnitTest, then :app:assembleFullRelease
//   3. sha256 of the built APK
//   4. copy APK into website/public/downloads/ AND website/downloads/
//   5. version-marker checklist (hand-edited HTML copy) -> pause -> apply.js -> check.js (must print ALL CHECKS PASSED)
//   6. write website/public/version.json
//   7. vercel deploy --prod from website/ + re-alias needs-vs-wants.vercel.app
//   8. git tag v<version> + push tag + gh release create
//
// No npm dependencies: node:child_process / node:fs / node:crypto only.

import { spawnSync } from 'node:child_process';
import fs from 'node:fs';
import path from 'node:path';
import crypto from 'node:crypto';
import readline from 'node:readline/promises';
import { fileURLToPath } from 'node:url';

const ROOT = path.dirname(path.dirname(fileURLToPath(import.meta.url)));
const GRADLE_FILE = path.join(ROOT, 'app', 'build.gradle.kts');
const APK_OUT = path.join(ROOT, 'app', 'build', 'outputs', 'apk', 'full', 'release', 'app-full-release.apk');
const SITE_DIR = path.join(ROOT, 'website');
const PUBLIC_HTML = path.join(SITE_DIR, 'public', 'index.html');
const DOWNLOAD_DIRS = [
  path.join(SITE_DIR, 'public', 'downloads'),
  path.join(SITE_DIR, 'downloads'),
];
const VERSION_JSON = path.join(SITE_DIR, 'public', 'version.json');
const PROD_HOST = 'needs-vs-wants.vercel.app';
const IS_WIN = process.platform === 'win32';

// ---------------------------------------------------------------------------
// CLI parsing
// ---------------------------------------------------------------------------
const args = process.argv.slice(2);
const flags = new Set(args.filter((a) => a.startsWith('--')));
const positional = args.filter((a) => !a.startsWith('--'));
const EXECUTE = flags.has('--yes');
if (flags.has('--yes') && flags.has('--dry-run')) {
  die('Pass either --yes or --dry-run, not both.');
}
for (const f of flags) {
  if (!['--yes', '--dry-run'].includes(f)) die(`Unknown flag: ${f}`);
}
const newVersion = positional[0];
if (!newVersion || !/^\d+\.\d+\.\d+(?:-[0-9A-Za-z.]+)?$/.test(newVersion)) {
  die(
    'Usage: node tools/release.mjs [--dry-run|--yes] <versionName>\n' +
    '  e.g. node tools/release.mjs --dry-run 2.0.15   (default mode: dry-run)\n' +
    '       node tools/release.mjs --yes 2.0.15'
  );
}

function die(msg) {
  console.error(`\nABORT: ${msg}`);
  process.exit(1);
}

const MODE = EXECUTE ? 'EXECUTE' : 'DRY-RUN';
function step(n, title) {
  console.log(`\n=== [step ${n}/${8}] ${title} ${EXECUTE ? '' : '(dry-run: printing plan only)'}`);
}
function plan(cmdText, cwd) {
  console.log(`  ${EXECUTE ? 'run' : 'would run'}: ${cmdText}${cwd ? `   (cwd: ${path.relative(ROOT, cwd) || '.'})` : ''}`);
}

// Runs a command, streaming output. Only ever called in EXECUTE mode.
// shell:true on Windows so .bat/.cmd shims (gradlew.bat, vercel, gh) resolve.
function run(cmd, cmdArgs, opts = {}) {
  const res = spawnSync(cmd, cmdArgs, {
    cwd: opts.cwd ?? ROOT,
    stdio: 'inherit',
    shell: IS_WIN,
  });
  if (res.error) die(`${cmd} failed to start: ${res.error.message}`);
  if (res.status !== 0) die(`${cmd} ${cmdArgs.join(' ')} exited with ${res.status}`);
}

// Same, but captures stdout+stderr (still echoed) for inspection.
function runCapture(cmd, cmdArgs, opts = {}) {
  const res = spawnSync(cmd, cmdArgs, {
    cwd: opts.cwd ?? ROOT,
    encoding: 'utf8',
    shell: IS_WIN,
  });
  if (res.error) die(`${cmd} failed to start: ${res.error.message}`);
  const out = (res.stdout ?? '') + (res.stderr ?? '');
  process.stdout.write(out);
  return { status: res.status, out };
}

async function confirm(promptText) {
  if (!EXECUTE) return true;
  if (!process.stdin.isTTY) die('Interactive confirmation needed but stdin is not a TTY. Re-run from a terminal.');
  const rl = readline.createInterface({ input: process.stdin, output: process.stdout });
  const answer = (await rl.question(promptText)).trim().toLowerCase();
  rl.close();
  return answer === 'y' || answer === 'yes' || answer === 'done';
}

function listVersionMarkers(html, version) {
  const hits = [];
  const needle = version;
  html.split('\n').forEach((line, i) => {
    let col = line.indexOf(needle);
    while (col !== -1) {
      hits.push({ line: i + 1, snippet: line.trim().slice(0, 110) });
      col = line.indexOf(needle, col + needle.length);
    }
  });
  return hits;
}

// ---------------------------------------------------------------------------
// Preflight (read-only in both modes)
// ---------------------------------------------------------------------------
console.log(`Needs vs Wants release script — mode: ${MODE}`);
console.log(`Repo root: ${ROOT}`);

if (!fs.existsSync(GRADLE_FILE)) die(`Missing ${GRADLE_FILE}`);
const gradleSrc = fs.readFileSync(GRADLE_FILE, 'utf8');
const codeMatch = gradleSrc.match(/versionCode = (\d+)/);
const nameMatch = gradleSrc.match(/versionName = "([^"]+)"/);
if (!codeMatch || !nameMatch) die('Could not parse versionCode/versionName from app/build.gradle.kts');
const oldCode = Number(codeMatch[1]);
const oldVersion = nameMatch[1];
const newCode = oldCode + 1;
const apkName = `needs-vs-wants-${newVersion}.apk`;

for (const d of DOWNLOAD_DIRS) if (!fs.existsSync(d)) die(`Missing download dir: ${d}`);
if (!fs.existsSync(PUBLIC_HTML)) die(`Missing ${PUBLIC_HTML}`);
for (const s of ['apply.js', 'check.js']) {
  if (!fs.existsSync(path.join(SITE_DIR, '_pad-parts', s))) die(`Missing website/_pad-parts/${s}`);
}
if (newVersion === oldVersion) die(`New version ${newVersion} equals current versionName.`);

console.log(`Current: versionName ${oldVersion}, versionCode ${oldCode}`);
console.log(`Target:  versionName ${newVersion}, versionCode ${newCode}`);
console.log(`APK:     ${apkName}`);

const gradlew = IS_WIN ? 'gradlew.bat' : './gradlew';
let sha256 = '(computed after build)';

// ---------------------------------------------------------------------------
// Step 1 — bump versionCode / versionName
// ---------------------------------------------------------------------------
step(1, 'Bump versionCode/versionName in app/build.gradle.kts');
console.log(`  versionCode = ${oldCode}  ->  versionCode = ${newCode}`);
console.log(`  versionName = "${oldVersion}"  ->  versionName = "${newVersion}"`);
if (EXECUTE) {
  const bumped = gradleSrc
    .replace(`versionCode = ${oldCode}`, `versionCode = ${newCode}`)
    .replace(`versionName = "${oldVersion}"`, `versionName = "${newVersion}"`);
  fs.writeFileSync(GRADLE_FILE, bumped);
  console.log('  written.');
}

// ---------------------------------------------------------------------------
// Step 2 — unit tests, then signed release build
// ---------------------------------------------------------------------------
step(2, 'Gradle: unit tests + assembleFullRelease');
plan(`${gradlew} :app:testFullDebugUnitTest`);
plan(`${gradlew} :app:assembleFullRelease   (signing needs RELEASE_* creds in ~/.gradle/gradle.properties or env)`);
if (EXECUTE) {
  run(gradlew, [':app:testFullDebugUnitTest']);
  run(gradlew, [':app:assembleFullRelease']);
}

// ---------------------------------------------------------------------------
// Step 3 — sha256 of the built APK
// ---------------------------------------------------------------------------
step(3, 'SHA-256 of the built APK');
console.log(`  APK path: ${path.relative(ROOT, APK_OUT)} (currently ${fs.existsSync(APK_OUT) ? 'exists — will be overwritten by the build' : 'absent — produced by step 2'})`);
if (EXECUTE) {
  if (!fs.existsSync(APK_OUT)) die(`Build finished but APK not found at ${APK_OUT}`);
  sha256 = crypto.createHash('sha256').update(fs.readFileSync(APK_OUT)).digest('hex');
  console.log(`  sha256: ${sha256}`);
}

// ---------------------------------------------------------------------------
// Step 4 — copy the APK into both download dirs
// ---------------------------------------------------------------------------
step(4, 'Copy APK into both download directories');
for (const d of DOWNLOAD_DIRS) {
  console.log(`  ${EXECUTE ? 'copy' : 'would copy'}: ${path.relative(ROOT, APK_OUT)} -> ${path.relative(ROOT, path.join(d, apkName))}`);
  if (EXECUTE) fs.copyFileSync(APK_OUT, path.join(d, apkName));
}

// ---------------------------------------------------------------------------
// Step 5 — hand-edited version markers, then apply.js + check.js
// ---------------------------------------------------------------------------
step(5, 'Version markers in the landing page + lock checks');
const htmlNow = fs.readFileSync(PUBLIC_HTML, 'utf8');
const markers = listVersionMarkers(htmlNow, oldVersion);
console.log(`  The HTML version markers are HAND-EDITED (do not auto-edit copy beyond apply.js).`);
console.log(`  Occurrences of "${oldVersion}" in website/public/index.html to update to "${newVersion}" (${markers.length} found):`);
for (const m of markers) console.log(`    line ${String(m.line).padStart(5)}: ${m.snippet}`);
console.log(`  Note: website/index.html is a byte-identical mirror — edit ONLY website/public/index.html; apply.js rewrites the mirror.`);
if (EXECUTE) {
  const ok = await confirm(`\n  Hand-edit the ${markers.length} marker(s) above now, save, then type "done" to continue (anything else aborts): `);
  if (!ok) die('Version markers not confirmed.');
  plan('node website/_pad-parts/apply.js');
  run(process.execPath, [path.join(SITE_DIR, '_pad-parts', 'apply.js')]);
  plan('node website/_pad-parts/check.js');
  const { status, out } = runCapture(process.execPath, [path.join(SITE_DIR, '_pad-parts', 'check.js')]);
  if (status !== 0 || !out.includes('ALL CHECKS PASSED')) die('check.js did not report ALL CHECKS PASSED.');
  const remaining = listVersionMarkers(fs.readFileSync(PUBLIC_HTML, 'utf8'), oldVersion);
  if (remaining.length > 0) {
    console.log(`  WARNING: ${remaining.length} occurrence(s) of old version "${oldVersion}" still present:`);
    for (const m of remaining) console.log(`    line ${String(m.line).padStart(5)}: ${m.snippet}`);
    const cont = await confirm('  Continue anyway? (y/N): ');
    if (!cont) die('Stale version markers remain.');
  }
} else {
  console.log('  (dry-run) would then: pause for confirmation -> run apply.js -> run check.js -> abort unless "ALL CHECKS PASSED" -> re-scan for stale markers.');
}

// ---------------------------------------------------------------------------
// Step 6 — website/public/version.json
// ---------------------------------------------------------------------------
step(6, 'Update website/public/version.json');
// Merge-update: preserve any extra fields (e.g. "notes") already in the file.
let existingVersionJson = {};
if (fs.existsSync(VERSION_JSON)) {
  try {
    existingVersionJson = JSON.parse(fs.readFileSync(VERSION_JSON, 'utf8'));
  } catch {
    console.log('  (existing version.json is not valid JSON — rebuilding it)');
  }
}
const versionJson = {
  ...existingVersionJson,
  versionName: newVersion,
  versionCode: newCode,
  apkUrl: `https://${PROD_HOST}/downloads/${apkName}`,
  sha256,
  updatedAt: new Date().toISOString(),
};
console.log(`  ${EXECUTE ? 'writing' : 'would write'}: ${path.relative(ROOT, VERSION_JSON)}`);
console.log('  ' + JSON.stringify(versionJson, null, 2).replace(/\n/g, '\n  '));
if (EXECUTE) fs.writeFileSync(VERSION_JSON, JSON.stringify(versionJson, null, 2) + '\n');

// ---------------------------------------------------------------------------
// Step 7 — deploy from website/ only, then re-alias production
// ---------------------------------------------------------------------------
step(7, 'Vercel: deploy --prod from website/ + re-alias');
plan('vercel deploy --prod', SITE_DIR);
plan(`vercel alias set <deployment-url> ${PROD_HOST}`, SITE_DIR);
if (EXECUTE) {
  const dep = runCapture('vercel', ['deploy', '--prod'], { cwd: SITE_DIR });
  if (dep.status !== 0) die('vercel deploy failed.');
  const urls = dep.out.match(/https:\/\/\S+\.vercel\.app/g) ?? [];
  const deployUrl = urls[urls.length - 1];
  if (!deployUrl) die(`Could not find a deployment URL in vercel output — alias manually: vercel alias set <url> ${PROD_HOST}`);
  console.log(`  deployment: ${deployUrl}`);
  run('vercel', ['alias', 'set', deployUrl, PROD_HOST], { cwd: SITE_DIR });
}

// ---------------------------------------------------------------------------
// Step 8 — git tag + GitHub release
// ---------------------------------------------------------------------------
step(8, 'git tag + gh release');
const tag = `v${newVersion}`;
const releaseApk = path.join(DOWNLOAD_DIRS[0], apkName);
const notes = `Needs vs Wants ${newVersion} (versionCode ${newCode}). APK SHA-256: ${sha256}`;
plan(`git tag ${tag}`);
plan(`git push origin ${tag}`);
plan(`gh release create ${tag} "${path.relative(ROOT, releaseApk)}" --title "${tag}" --notes "${notes}"`);
if (EXECUTE) {
  run('git', ['tag', tag]);
  run('git', ['push', 'origin', tag]);
  run('gh', ['release', 'create', tag, releaseApk, '--title', tag, '--notes', notes]);
}

// ---------------------------------------------------------------------------
console.log(`\n${MODE} complete.`);
if (!EXECUTE) {
  console.log('Nothing was modified, no commands were run. Re-run with --yes to execute.');
} else {
  console.log('Remember: commit the version bump + site changes, and update the Second Brain notes (Tasks.md / Decisions.md).');
}
