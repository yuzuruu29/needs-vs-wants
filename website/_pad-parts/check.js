const fs = require('fs');
const path = require('path');
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
  ['₱199', /₱199/],
  ['₱399', /₱399/],
  ['id=get', /id="get"/],
];
locks.forEach(([name, re]) => {
  if (!re.test(html)) fail('missing lock: ' + name);
  else console.log('lock OK', name);
});

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
