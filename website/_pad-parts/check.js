const fs = require('fs');
const path = require('path');
const crypto = require('crypto');
const root = path.join(__dirname, '..');
const html = fs.readFileSync(path.join(root, 'public', 'index.html'), 'utf8');
const mirror = path.join(root, 'index.html');
let failed = false;

function fail(msg) {
  console.error('FAIL', msg);
  failed = true;
}

const i = html.indexOf('(function(){');
const j = html.lastIndexOf('})();');
const code = html.slice(i + 12, j);
try {
  new Function(code);
  console.log('JS OK', code.length);
} catch (e) {
  fail('SYNTAX ' + e.message);
}

const locks = [
  ['qrcode@1.5.1', /qrcode@1\.5\.1/],
  ['.pflip', /\.pflip|pflip-stage|buildFlipLeaves/],
  ['.cta-panel', /class="cta-panel/],
  ['payPalSeamBtn', /id="payPalSeamBtn"/],
  ['tiktok', /tiktok\.com\/@expenseneedswants/],
  ['₱49', /₱49</],
  ['₱99', /₱99</],
  ['₱490', /₱490/],
  ['₱990', /₱990/],
  ['id=get', /id="get"/],
  // audit-gap locks (2026-08-13): SEO head pack
  ['canonical', /<link rel="canonical" href="https:\/\/needs-vs-wants\.vercel\.app\/">/],
  ['og:title', /<meta property="og:title"/],
  ['og:image', /<meta property="og:image" content="https:\/\/needs-vs-wants\.vercel\.app\/og-image\.png">/],
  ['twitter:card', /<meta name="twitter:card" content="summary_large_image">/],
  ['json-ld SoftwareApplication', /"@type": "SoftwareApplication"/],
  // audit-gap locks: legal footer links + email capture wiring
  ['footer privacy link', /href="\.\/privacy\.html"/],
  ['footer terms link', /href="\.\/terms\.html"/],
  ['notify endpoint', /xpwcrloarciomikfudln\.supabase\.co\/functions\/v1\/notify_signup/],
  ['honeypot field', /name="website" class="hp-field"/],
  // audit-gap locks: download trust block
  ['apk sha256 on page', /79093fd54b8422007f70a88d4ecbae2e060df6e866f735a232e439a304e7563d/],
  ['cert sha256 on page', /5fc43fb6e3a4d8e72123895d4b05d5f83082d8c6b242f1a8f8fd3039d551c961/],
  // audit-gap locks: subset woff2 fonts (TTFs were never in public/ and 404ed in prod)
  ['woff2 body font', /\.\/fonts\/source-sans3-regular\.woff2/],
  ['woff2 display font', /\.\/fonts\/caveat-bold\.woff2/],
  // audit-gap locks: honesty pass (absolutist claims must stay dead)
  ['optional rewarded ad honesty', /optional rewarded ad/],
  // 2.0.15 campaign teaser
  ['essence teaser source', /\.\/video\/essence-teaser\.mp4/],
  // 2.0.15 instruction pass: every user-facing how-to must stay on the page
  ['install unknown-source step', /Allow from this source/],
  ['install play protect step', /Install anyway/],
  ['upgrade from 2.0.x', /Already on 2\.0\.0 or newer/],
  ['update guide', /Keeping the app updated/],
  ['backup guide', /Backing up your diary/],
  ['backup auto step', /Auto-backup daily/],
  ['subscribe guide', /How to subscribe, and how to get it back/],
  ['restore purchases step', /Restore purchases/],
];
['no network calls', '100% offline', 'NotebookLM', 'No ads, no tracking', 'No ads, no analytics'].forEach(claim => {
  if (html.includes(claim)) fail('banned claim present: ' + claim);
  else console.log('claim OK absent:', claim);
});
locks.forEach(([name, re]) => {
  if (!re.test(html)) fail('missing lock: ' + name);
  else console.log('lock OK', name);
});

// audit-gap locks: static files (legal pages, SEO files, OG image)
const fileLocks = [
  ['privacy.html', ['stays on your device', 'Data Privacy Act', 'Sentry', 'PayMongo', 'Google Sign-In', 'one-time code', 'folder you select', 'National Privacy Commission', 'aceuaisolutions@gmail.com']],
  ['terms.html', ['Refund', '7 days', 'PayPal', 'PayMongo', 'accidental-purchase', 'one-time code', 'aceuaisolutions@gmail.com']],
  ['robots.txt', ['Sitemap: https://needs-vs-wants.vercel.app/sitemap.xml']],
  ['sitemap.xml', ['https://needs-vs-wants.vercel.app/privacy.html', 'https://needs-vs-wants.vercel.app/terms.html']],
  ['404.html', ['404', 'Needs <b>vs</b> Wants']],
];
fileLocks.forEach(([name, needles]) => {
  const p = path.join(root, 'public', name);
  if (!fs.existsSync(p)) return fail('missing file: public/' + name);
  const body = fs.readFileSync(p, 'utf8');
  const missing = needles.filter(n => !body.includes(n));
  if (missing.length) fail('public/' + name + ' missing phrases: ' + missing.join(' | '));
  else console.log('file OK', name);
});
for (const name of ['privacy.html', 'terms.html']) {
  const body = fs.readFileSync(path.join(root, 'public', name), 'utf8');
  if (body.includes('[CONTACT EMAIL')) fail('public/' + name + ' still contains the contact placeholder');
}
const ogPath = path.join(root, 'public', 'og-image.png');
if (!fs.existsSync(ogPath) || fs.statSync(ogPath).size < 10000) fail('og-image.png missing or too small');
else console.log('file OK og-image.png', fs.statSync(ogPath).size, 'bytes');

const teaserPath = path.join(root, 'public', 'video', 'essence-teaser.mp4');
if (!fs.existsSync(teaserPath) || fs.statSync(teaserPath).size < 100000) fail('essence-teaser.mp4 missing or too small');
else console.log('file OK essence-teaser.mp4', fs.statSync(teaserPath).size, 'bytes');

// audit-gap lock: version.json consistent with site markers and APK on disk
const vPath = path.join(root, 'public', 'version.json');
if (!fs.existsSync(vPath)) {
  fail('version.json missing');
} else {
  let v = null;
  try { v = JSON.parse(fs.readFileSync(vPath, 'utf8')); }
  catch (e) { fail('version.json invalid JSON: ' + e.message); }
  if (v) {
    if (!html.includes('Version ' + v.versionName)) fail('site missing marker: Version ' + v.versionName);
    else console.log('version OK site marker Version', v.versionName);
    const apkName = 'needs-vs-wants-' + v.versionName + '.apk';
    if (!html.includes(apkName)) fail('site missing download ref: ' + apkName);
    else console.log('version OK site downloads', apkName);
    if (!v.apkUrl || !v.apkUrl.endsWith('/downloads/' + apkName)) fail('version.json apkUrl does not match versionName');
    else console.log('version OK apkUrl matches versionName');
    const apkPath = path.join(root, 'public', 'downloads', apkName);
    if (!fs.existsSync(apkPath)) {
      fail('APK on disk missing: ' + apkPath);
    } else {
      const sha = crypto.createHash('sha256').update(fs.readFileSync(apkPath)).digest('hex');
      if (sha !== v.sha256) fail('version.json sha256 mismatch: disk ' + sha + ' != json ' + v.sha256);
      else console.log('version OK sha256 matches APK on disk');
    }
  }
}

if (fs.existsSync(mirror)) {
  const a = fs.readFileSync(path.join(root, 'public', 'index.html'));
  const b = fs.readFileSync(mirror);
  if (!a.equals(b)) fail('public/index.html and index.html are not byte-identical');
  else console.log('mirror OK byte-identical');
} else {
  fail('website/index.html missing');
}

const bodyMatch = html.match(/<body[^>]*>([\s\S]*)<\/body>/i);
if (bodyMatch) {
  const body = bodyMatch[1];
  const styleBlocks = body.replace(/<style[\s\S]*?<\/style>/gi, '');
  const noScripts = styleBlocks.replace(/<script[\s\S]*?<\/script>/gi, '');
  const textish = noScripts.replace(/<[^>]+>/g, '');
  if (/—/.test(textish) || /–/.test(textish)) {
    fail('em/en dash found in body text content');
  } else console.log('dash OK no em/en dash in body text');
}

console.log('focusDemo', html.split('function focusDemo').length - 1);
console.log('todayLabel', html.split('id="todayLabel"').length - 1);
console.log('bootPad', html.includes('bootPad'));

if (failed) process.exit(1);
console.log('ALL CHECKS PASSED');
