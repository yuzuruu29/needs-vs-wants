const fs = require('fs');
const path = require('path');
const html = fs.readFileSync(path.join(__dirname, '..', 'public', 'index.html'), 'utf8');
const i = html.indexOf('(function(){');
const j = html.lastIndexOf('})();');
const code = html.slice(i + 12, j);
try {
  new Function(code);
  console.log('JS OK', code.length);
} catch (e) {
  console.error('SYNTAX', e.message);
  process.exit(1);
}
console.log('focusDemo', html.split('function focusDemo').length - 1);
console.log('todayLabel', html.split('id="todayLabel"').length - 1);
console.log('bootPad', html.includes('bootPad'));
console.log('useCrossfade bug?', /if \(!flatRoot \|\| flatRoot\.children\.length !== pages\.length\) buildFlat\(!!useCrossfade\)/.test(code));
