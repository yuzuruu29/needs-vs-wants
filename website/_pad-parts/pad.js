/* ---------- Demo sheet: PadController + FlatRenderer + FlipRenderer (CSS 3D two-face turn) ---------- */
const MAX_ROWS = 20;
const PAGE_CAP = 12;
/* 700ms reads as a weighted paper turn — the edge lifts, sweeps past vertical, settles. */
const FLIP_MS = 700;
const FLIP_SAFETY_PAD = 250;
const FLIP_FREEZE_DELAY = 180; /* ms into flip before dimming overlay (let lift phase show first) */
const reducePaper = matchMedia('(prefers-reduced-motion: reduce)').matches;
const notepad = $('#notepad');
let padStage = $('#padStage');
const pageDots = $('#pageDots');
const pagePrev = $('#pagePrev');
const pageNext = $('#pageNext');
const pagePrevTab = $('#pagePrevTab');
const pageNextTab = $('#pageNextTab');
const trashSVG = '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M4.5 6.5h15M9.5 6V4.5h5V6M7 6.5l.8 13h8.4l.8-13"/></svg>';
const esc = s => String(s).replace(/&/g,'&amp;').replace(/</g,'&lt;').replace(/"/g,'&quot;');

/* PageIdxAdapter: portrait single-leaf ⇒ library pageIndex === sheet index (locked Step 4) */
const adapter = {
  toPageIdx(sheet){ return sheet; },
  sheetFromPageIdx(pageIdx){ return pageIdx; }
};

let pages = [
  [
    {t:'12:05', n:'Bus fare',    c:4500,  y:'NEED'},
    {t:'09:47', n:'Coffee',      c:18000, y:'WANT'},
    {t:'08:12', n:'Rice & eggs', c:12500, y:'NEED'}
  ],
  []
];
let pageIndex = 0;
let type = null;
let flipping = false;
let mode = 'flat'; /* 'flat' | 'flip' */
let flipLeaves = [];
let flipAnim = null;
let flatRoot = null;
let focusAfterFlip = false;
let formDraft = { item:'', cost:'', type:null };
let flipGen = 0;
let flipSafetyTimer = null;
let pendingFlipTarget = null;

function rows(){ return pages[pageIndex] || []; }
function setRows(next){ pages[pageIndex] = next; }

function dateLabel(){
  const el = $('#todayLabel');
  return el ? el.textContent : '';
}

function announce(msg){
  const l = $('#live'); if (!l) return;
  l.textContent = '';
  setTimeout(() => { l.textContent = msg; }, 40);
}

function renderSheetMarkup(sheetIdx, live){
  const list = pages[sheetIdx] || [];
  const full = list.length >= MAX_ROWS;
  const empty = list.length === 0;
  const pageNo = sheetIdx + 1;
  const rowsHtml = list.map((r,i) =>
    `<div class="l-row${live && i===0 && r._fresh ? ' row-new' : ''}" data-i="${i}">
       <span class="l-time">${r.t}</span>
       <span class="l-item">${esc(r.n)}</span>
       <b class="money l-cost" data-cents="${r.c}">${fmt(r.c)}</b>
       <i class="l-type ${r.y==='NEED'?'t-n':'t-w'}">${r.y}</i>
       ${live ? `<button class="del" type="button" data-i="${i}" aria-label="Delete entry: ${esc(r.n)}">${trashSVG}</button>` : '<span></span>'}
     </div>`).join('');
  let body;
  if (full){
    body = live
      ? `<button type="button" class="page-full-cta" data-action="next-page">Page full — turn to a fresh sheet →</button>`
      : `<p class="page-full-cta" aria-hidden="true">Page full — turn to a fresh sheet →</p>`;
  } else if (live){
    body = `<div class="demo-form" data-live-form>
      ${empty ? '<p class="sheet-ghost">Seal your first purchase on this page — Need or Want?</p>' : ''}
      <div class="field">
        <label for="fItem">Item</label>
        <input id="fItem" type="text" autocomplete="off" placeholder="What did you just buy?" maxlength="40" value="${esc(formDraft.item)}">
      </div>
      <div class="field">
        <label for="fCost">Cost</label>
        <div class="cost-wrap">
          <span class="cur-sym" aria-hidden="true">${CUR[cur].s}</span>
          <input id="fCost" type="text" inputmode="decimal" autocomplete="off" placeholder="0.00" aria-label="Cost" value="${esc(formDraft.cost)}">
        </div>
      </div>
      <div class="type-row" role="group" aria-label="Classify this expense">
        <button class="type-chip chip-need" type="button" data-type="NEED" aria-pressed="${formDraft.type==='NEED'?'true':'false'}">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M5 12.5l4.5 4.5L19 7"/></svg>NEED</button>
        <button class="type-chip chip-want" type="button" data-type="WANT" aria-pressed="${formDraft.type==='WANT'?'true':'false'}">
          <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M5 12.5l4.5 4.5L19 7"/></svg>WANT</button>
      </div>
      <p class="seal-hint">Fill all three — the row seals itself. No save button.</p>
    </div>`;
  } else {
    const snapItem = formDraft.item && sheetIdx === pageIndex ? esc(formDraft.item) : '';
    const snapCost = formDraft.cost && sheetIdx === pageIndex ? esc(formDraft.cost) : '';
    body = `${empty ? '<p class="sheet-ghost">Seal your first purchase on this page — Need or Want?</p>' : ''}
      <div class="demo-form-snap" inert>
        <div class="field"><label>Item</label><input type="text" readonly tabindex="-1" value="${snapItem}" placeholder="What did you just buy?"></div>
        <div class="field"><label>Cost</label><div class="cost-wrap"><span class="cur-sym">${CUR[cur].s}</span><input type="text" readonly tabindex="-1" value="${snapCost}" placeholder="0.00"></div></div>
        <div class="type-row"><span class="type-chip chip-need" aria-hidden="true">NEED</span><span class="type-chip chip-want" aria-hidden="true">WANT</span></div>
        <p class="seal-hint">Fill all three — the row seals itself. No save button.</p>
      </div>`;
  }
  const hideDog = (pages.length >= PAGE_CAP && sheetIdx >= pages.length - 1 && full);
  const dogEar = live && !hideDog
    ? `<button class="dog-ear" type="button" data-action="next-page" aria-label="Flip to next page" title="Next page"></button>`
    : '';
  return `<div class="sheet ${live ? 'is-live' : 'is-static'}" data-sheet="${sheetIdx}" ${live ? '' : 'inert'}>
    <div class="sheet-head">
      <div>
        <p class="eyebrow">Today · <span class="today-label">${esc(dateLabel())}</span> · Page <span class="sheet-page-no">${pageNo}</span></p>
        <h3>Log</h3>
      </div>
      <span class="counter" aria-live="polite">
        <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><circle cx="12" cy="12" r="8.2"/><path d="M8.6 12.3l2.3 2.3 4.6-4.7"/></svg>
        SHEET&nbsp;<b class="sheet-count">${list.length}</b>&nbsp;/&nbsp;20
      </span>
    </div>
    ${body}
    <div class="ledger-head" aria-hidden="true">
      <span>Time</span><span>Item</span><span class="r">Cost</span><span style="text-align:center">Type</span><span></span>
    </div>
    <div class="ledger-rows">${rowsHtml}</div>
    ${dogEar}
  </div>`;
}

function captureDraft(){
  const itemEl = $('#fItem');
  const costEl = $('#fCost');
  formDraft = {
    item: itemEl ? itemEl.value : formDraft.item,
    cost: costEl ? costEl.value : formDraft.cost,
    type
  };
}

function wireLiveForm(root){
  const itemEl = root.querySelector('#fItem');
  const costEl = root.querySelector('#fCost');
  if (itemEl){
    itemEl.addEventListener('input', () => {
      let v = itemEl.value.replace(/[^A-Za-z0-9\s\-'\.,]/g,'');
      if (v !== itemEl.value) itemEl.value = v;
      formDraft.item = itemEl.value;
      trySeal();
    });
  }
  if (costEl){
    costEl.addEventListener('input', () => {
      let v = costEl.value.replace(/[^\d.]/g,'');
      const p = v.split('.');
      if (p.length > 2) v = p[0] + '.' + p.slice(1).join('');
      if (p[1] && p[1].length > 2) v = p[0] + '.' + p[1].slice(0,2);
      if (v !== costEl.value) costEl.value = v;
      formDraft.cost = costEl.value;
      trySeal();
    });
  }
  root.querySelectorAll('.type-chip').forEach(ch => ch.addEventListener('click', () => {
    type = ch.dataset.type;
    formDraft.type = type;
    root.querySelectorAll('.type-chip').forEach(c => c.setAttribute('aria-pressed', c === ch ? 'true' : 'false'));
    trySeal();
  }));
  const ledger = root.querySelector('.ledger-rows');
  if (ledger){
    ledger.addEventListener('click', e => {
      const b = e.target.closest('.del'); if (!b) return;
      const list = rows().slice();
      const r = list[+b.dataset.i];
      list.splice(+b.dataset.i, 1);
      setRows(list);
      hydrateLiveSheet(pageIndex, false);
      announce('Deleted: ' + r.n);
    });
  }
  root.querySelectorAll('[data-action="next-page"]').forEach(btn => {
    btn.addEventListener('click', () => go(1));
  });
}

function updateChrome(){
  const total = pages.length;
  const pos = pageIndex + 1;
  const pp = $('#pagePos'); const pt = $('#pageTotal');
  if (pp) pp.textContent = String(pos);
  if (pt) pt.textContent = String(total);
  const nextDisabled = flipping || (pages.length >= PAGE_CAP && pageIndex >= pages.length - 1 && rows().length >= MAX_ROWS);
  const prevDisabled = flipping || pageIndex <= 0;
  [pagePrev, pagePrevTab].forEach(el => {
    if (!el) return;
    el.disabled = prevDisabled;
    el.setAttribute('aria-disabled', prevDisabled ? 'true' : 'false');
  });
  [pageNext, pageNextTab].forEach(el => {
    if (!el) return;
    el.disabled = nextDisabled;
    el.setAttribute('aria-disabled', nextDisabled ? 'true' : 'false');
    el.title = nextDisabled ? 'Ledger full — 12 pages.' : 'Next page';
  });
  if (pageDots){
    if (total > 8){
      const pct = total <= 1 ? 0 : (pageIndex / (total - 1)) * 100;
      pageDots.innerHTML = `<div class="page-progress" role="progressbar" aria-valuenow="${pos}" aria-valuemin="1" aria-valuemax="${total}" aria-label="Page progress"><span class="page-progress-mark" style="left:${pct}%"></span></div>`;
    } else {
      pageDots.innerHTML = pages.map((_, i) =>
        `<button type="button" class="page-dot${i===pageIndex?' is-active':''}" data-page="${i}"
          aria-label="Go to page ${i+1}" aria-current="${i===pageIndex?'page':'false'}"
          ${flipping?'disabled':''}></button>`
      ).join('');
    }
  }
  const stack = $('#padStack');
  if (stack){
    const remaining = Math.max(0, pages.length - pageIndex - 1);
    stack.querySelectorAll('i').forEach((el, i) => {
      el.style.opacity = i < Math.min(3, remaining) ? '' : '0';
    });
  }
}

function pruneTrailingEmpty(){
  while (pages.length > 1){
    const last = pages.length - 1;
    if (last === pageIndex) break;
    if ((pages[last] || []).length === 0) pages.pop();
    else break;
  }
}

function liveOverlay(){ return $('#liveOverlay'); }

function clearFlipSafety(){
  if (flipSafetyTimer){ clearTimeout(flipSafetyTimer); flipSafetyTimer = null; }
}

/** Dim the live overlay so the form ghosts through the flip — bridges the turn
 *  instead of vanishing and reappearing as a hard cut. */
function freezeLiveSheet(){
  captureDraft();
  if (notepad) notepad.classList.add('is-flipping');
  /* schedule the dim so the first ~180ms shows the full page lifting off */
  setTimeout(() => {
    const ov = liveOverlay();
    if (ov && flipping){
      ov.classList.add('is-dimmed');
      ov.setAttribute('aria-hidden', 'true');
    }
  }, FLIP_FREEZE_DELAY);
}

/**
 * @param {number} idx
 * @param {{ land?: boolean }} [opts] land=true: soft paper-drop after flip (event settle)
 */
function hydrateLiveSheet(idx, opts){
  const land = !!(opts && opts.land) && mode === 'flip' && !reducePaper;
  pageIndex = idx;
  if (mode === 'flat' && flatRoot){
    if (flatRoot.children.length !== pages.length){
      buildFlat(false);
      return;
    }
    flatRoot.querySelectorAll('.pad-leaf').forEach((leaf, i) => {
      leaf.classList.toggle('is-current', i === pageIndex);
      leaf.classList.remove('is-leaving');
      leaf.innerHTML = renderSheetMarkup(i, i === pageIndex);
    });
    const liveLeaf = flatRoot.querySelector('.pad-leaf.is-current');
    if (liveLeaf) wireLiveForm(liveLeaf);
  } else if (mode === 'flip'){
    /* Library pages stay static; interactive form rides in overlay on settled page */
    const ov = liveOverlay();
    if (ov){
      /* Clear freeze dim + initial hide, then land with paper-drop animation */
      ov.classList.remove('is-hidden', 'is-dimmed');
      ov.innerHTML = renderSheetMarkup(pageIndex, true);
      wireLiveForm(ov);
      if (land){
        ov.classList.add('is-landing');
        ov.setAttribute('aria-hidden', 'false');
        if (notepad) notepad.classList.remove('is-flipping');
        /* two rAFs: first paint at scale(1.012), then settle to scale(1) */
        requestAnimationFrame(() => {
          requestAnimationFrame(() => {
            if (pageIndex !== idx) return;
            ov.classList.remove('is-landing');
          });
        });
      } else {
        ov.classList.remove('is-landing');
        ov.setAttribute('aria-hidden', 'false');
        if (notepad) notepad.classList.remove('is-flipping');
      }
    } else if (notepad){
      notepad.classList.remove('is-flipping');
    }
  }
  pages.forEach(list => list.forEach(r => { delete r._fresh; }));
  updateChrome();
  const scope = mode === 'flip' ? liveOverlay() : padStage;
  if (scope){
    scope.querySelectorAll('[data-cents]').forEach(el => el.textContent = fmt(+el.dataset.cents));
    scope.querySelectorAll('.cur-sym').forEach(el => el.textContent = CUR[cur].s);
  }
}

/** Finish a programmatic flip: sync index, clear freeze dim, land overlay with paper-drop */
function settleFlip(libTarget, gen){
  if (gen !== flipGen || !flipping) return;
  clearFlipSafety();
  if (flipAnim){
    try { flipAnim.cancel(); } catch(_){}
    flipAnim = null;
  }
  flipping = false;
  pendingFlipTarget = null;
  pageIndex = adapter.sheetFromPageIdx(libTarget);
  pruneTrailingEmpty();
  /* quiet-refresh static snapshots under the new page so reverse flip shows correct ink */
  try { syncStaticFlipPagesQuiet(); } catch(_){}
  poseAllLeaves();
  hydrateLiveSheet(pageIndex, { land: true });
  announce('Page ' + (pageIndex + 1) + ' of ' + pages.length);
  if (focusAfterFlip){
    focusAfterFlip = false;
    const fi = $('#fItem');
    if (fi){
      /* wait for land frame so focus outline doesn't flash mid-scale */
      setTimeout(() => { if (fi.isConnected) fi.focus({preventScroll:true}); }, 60);
    }
  }
}

function trySeal(){
  if (flipping) return;
  const itemEl = $('#fItem');
  const costEl = $('#fCost');
  if (!itemEl || !costEl) return;
  const list = rows();
  if (list.length >= MAX_ROWS) return;
  const name = itemEl.value.trim();
  const cents = Math.round(parseFloat(costEl.value) * 100);
  if (!name || !costEl.value || !(cents > 0) || !type) return;
  const sealedType = type;
  const now = new Date();
  const t = String(now.getHours()).padStart(2,'0') + ':' + String(now.getMinutes()).padStart(2,'0');
  const next = [{t, n:name, c:cents, y:sealedType, _fresh:true}, ...list];
  setRows(next);
  formDraft = { item:'', cost:'', type:null };
  type = null;
  hydrateLiveSheet(pageIndex);
  announce('Sealed: ' + name + ', ' + fmt(cents) + ', ' + sealedType.toLowerCase());
  const fi = $('#fItem');
  if (fi) fi.focus();
  if (next.length >= MAX_ROWS) announce('Page full — turn to a fresh sheet');
}

function ensureStage(){
  let stage = $('#padStage');
  const wrap = $('#padStageWrap');
  if (!stage && wrap){
    stage = document.createElement('div');
    stage.id = 'padStage';
    stage.className = 'pad-stage';
    wrap.insertBefore(stage, wrap.firstChild);
  }
  if (!stage){
    const board = notepad && notepad.querySelector('.pad-board');
    const w = document.createElement('div');
    w.id = 'padStageWrap';
    w.className = 'pad-stage-wrap';
    stage = document.createElement('div');
    stage.id = 'padStage';
    stage.className = 'pad-stage';
    const ov = document.createElement('div');
    ov.id = 'liveOverlay';
    ov.className = 'live-overlay is-hidden';
    ov.setAttribute('aria-hidden', 'true');
    w.appendChild(stage);
    w.appendChild(ov);
    if (board) board.insertBefore(w, board.querySelector('.pad-tabs'));
  } else if (!$('#liveOverlay')){
    /* markup ships a bare #padStage (not wrapped) — lift it into a stage-wrap
       so the live overlay + clipped 3D turn both work */
    const board = notepad && notepad.querySelector('.pad-board');
    const w = document.createElement('div');
    w.id = 'padStageWrap';
    w.className = 'pad-stage-wrap';
    const ov = document.createElement('div');
    ov.id = 'liveOverlay';
    ov.className = 'live-overlay is-hidden';
    ov.setAttribute('aria-hidden', 'true');
    if (stage.parentNode && stage.parentNode !== w){
      stage.parentNode.insertBefore(w, stage);
    }
    w.appendChild(stage);
    w.appendChild(ov);
    if (board && w.parentNode !== board) board.insertBefore(w, board.querySelector('.pad-tabs'));
  }
  padStage = stage;
  return stage;
}

const PFLIP_FACE = `<span class="pflip__crease" aria-hidden="true"></span><span class="pflip__sheen" aria-hidden="true"></span>`;

function buildFlipLeaves(){
  const stage = ensureStage();
  stage.innerHTML = '';
  stage.className = 'pad-stage pflip-stage';
  stage.style.setProperty('--flip', FLIP_MS + 'ms');
  const shadow = document.createElement('div');
  shadow.className = 'pflip-shadow';
  shadow.setAttribute('aria-hidden', 'true');
  stage.appendChild(shadow);
  flipLeaves = pages.map((_, i) => {
    const leaf = document.createElement('div');
    leaf.className = 'pflip';
    leaf.dataset.sheet = i;
    const front = document.createElement('div');
    front.className = 'pflip__face pflip__face--front';
    front.innerHTML = PFLIP_FACE + renderSheetMarkup(i, false);
    const back = document.createElement('div');
    back.className = 'pflip__face pflip__face--back';
    back.innerHTML = PFLIP_FACE;
    leaf.appendChild(front);
    leaf.appendChild(back);
    stage.appendChild(leaf);
    return leaf;
  });
  poseAllLeaves();
  hydrateLiveSheet(pageIndex);
  window.__pflipMode = 'flip';
}

function poseAllLeaves(){
  if (!flipLeaves.length) return;
  flipLeaves.forEach((leaf, i) => {
    leaf.classList.remove('is-turn', 'is-turn-next', 'is-turn-prev');
    const angle = i < pageIndex ? -180 : 0;
    leaf.style.zIndex = 100 - i;
    leaf.style.transform = `rotateY(${angle}deg)`;
  });
}

function syncStaticFlipPagesQuiet(){
  if (!flipLeaves.length) return;
  flipLeaves.forEach((leaf, i) => {
    if (i >= pages.length) return;
    const front = leaf.querySelector('.pflip__face--front');
    if (!front) return;
    const crease = front.querySelector('.pflip__crease');
    const sheen = front.querySelector('.pflip__sheen');
    front.innerHTML = renderSheetMarkup(i, false);
    if (sheen) front.insertBefore(sheen, front.firstChild);
    if (crease) front.insertBefore(crease, front.firstChild);
  });
}

function buildFlat(crossfade){
  if (flipAnim){
    try { flipAnim.cancel(); } catch(_){}
    flipAnim = null;
  }
  let stage = $('#padStage');
  if (!stage){
    const board = notepad && notepad.querySelector('.pad-board');
    stage = document.createElement('div');
    stage.id = 'padStage';
    stage.className = 'pad-stage';
    if (board) board.appendChild(stage);
  }
  stage.innerHTML = '';
  stage.className = 'pad-stage';
  padStage = stage;
  flipLeaves = [];
  flatRoot = document.createElement('div');
  flatRoot.className = 'pad-flat-root' + (crossfade ? ' is-crossfade' : '');
  pages.forEach((_, i) => {
    const leaf = document.createElement('div');
    leaf.className = 'pad-leaf' + (i === pageIndex ? ' is-current' : '');
    leaf.innerHTML = renderSheetMarkup(i, i === pageIndex);
    flatRoot.appendChild(leaf);
  });
  stage.appendChild(flatRoot);
  const liveLeaf = flatRoot.querySelector('.pad-leaf.is-current');
  if (liveLeaf) wireLiveForm(liveLeaf);
  updateChrome();
}

function go(delta){
  if (flipping) return;
  const active = document.activeElement;
  const wantFocus = !!(active && notepad && notepad.contains(active) && (active.tagName === 'INPUT' || active.classList.contains('type-chip')));
  let target = pageIndex + delta;

  if (delta > 0){
    if (pageIndex >= pages.length - 1){
      if (pages.length >= PAGE_CAP){
        announce('Ledger full — 12 pages.');
        updateChrome();
        return;
      }
      pages.push([]);
      target = pages.length - 1;
    }
  } else if (target < 0){
    return;
  }

  if (mode === 'flat'){
    const useCrossfade = false;
    if (useCrossfade && flatRoot){
      const curLeaf = flatRoot.querySelector('.pad-leaf.is-current');
      if (curLeaf) curLeaf.classList.add('is-leaving');
    }
    pageIndex = target;
    pruneTrailingEmpty();
    if (!flatRoot || flatRoot.children.length !== pages.length) buildFlat(!!useCrossfade);
    else hydrateLiveSheet(pageIndex);
    announce('Page ' + (pageIndex + 1) + ' of ' + pages.length);
    updateChrome();
    if (wantFocus){
      const fi = $('#fItem');
      if (fi) fi.focus({preventScroll:true});
    }
    return;
  }

  /* Flip mode: dim live overlay, turn a CSS 3D leaf, land overlay on settle */
  captureDraft();
  focusAfterFlip = wantFocus;
  const targetSheet = target;
  if (!flipLeaves.length || flipLeaves.length !== pages.length){
    buildFlipLeaves();
  }
  freezeLiveSheet();
  flipping = true;
  clearFlipSafety();
  const gen = ++flipGen;
  updateChrome();
  const libTarget = adapter.toPageIdx(targetSheet);
  pendingFlipTarget = libTarget;
  /* Dog-ear is top-right: peel next from top; reverse from bottom for readable curl */
  const corner = delta > 0 ? 'top' : 'bottom';
  flipTo(libTarget, gen, corner);
}

function jumpTo(sheet){
  if (flipping || sheet === pageIndex || sheet < 0 || sheet >= pages.length) return;
  if (mode === 'flat'){
    pageIndex = sheet;
    pruneTrailingEmpty();
    hydrateLiveSheet(pageIndex);
    announce('Page ' + (pageIndex + 1) + ' of ' + pages.length);
    return;
  }
  if (!flipLeaves.length) return;
  focusAfterFlip = false;
  freezeLiveSheet();
  flipping = true;
  clearFlipSafety();
  const gen = ++flipGen;
  updateChrome();
  const libTarget = adapter.toPageIdx(sheet);
  pendingFlipTarget = libTarget;
  const corner = sheet > pageIndex ? 'top' : 'bottom';
  flipTo(libTarget, gen, corner);
}

/**
 * Animate the turning leaf with WAAPI. Forward turns the current leaf 0 -> -180
 * around the left spine; reverse turns the target leaf -180 -> 0 back over the top.
 * Sheen/crease/cast run off the .is-turn class (CSS `--flip` duration).
 */
function flipTo(libTarget, gen, corner){
  const dir = libTarget > pageIndex ? 1 : -1;
  const moving = flipLeaves[dir > 0 ? pageIndex : libTarget];
  if (!moving){ settleFlip(libTarget, gen); return; }
  /* settle every non-moving leaf to its final pose instantly so the turning
     leaf is the only thing in motion (multi-page jumps snap beneath it) */
  flipLeaves.forEach((leaf, i) => {
    if (leaf === moving) return;
    const angle = i < libTarget ? -180 : 0;
    leaf.style.zIndex = 100 - i;
    leaf.style.transform = `rotateY(${angle}deg)`;
  });
  moving.style.zIndex = 1000;
  moving.classList.add('is-turn', dir > 0 ? 'is-turn-next' : 'is-turn-prev');
  const from = dir > 0 ? 0 : -180;
  const to = dir > 0 ? -180 : 0;
  if (flipAnim){ try { flipAnim.cancel(); } catch(_){} flipAnim = null; }
  /* apex translateZ lifts the free edge off the stack mid-turn (paper flex) */
  flipAnim = moving.animate([
    { transform: `rotateY(${from}deg) translateZ(0px)` },
    { transform: `rotateY(${(from + to) / 2}deg) translateZ(30px)`, offset: .5 },
    { transform: `rotateY(${to}deg) translateZ(0px)` }
  ], {
    duration: FLIP_MS,
    easing: 'cubic-bezier(.32,.72,.25,1)',
    fill: 'both'
  });
  flipAnim.onfinish = () => settleFlip(libTarget, gen);
  flipSafetyTimer = setTimeout(() => settleFlip(libTarget, gen), FLIP_MS + FLIP_SAFETY_PAD);
}

function bindNav(){
  const onPrev = () => go(-1);
  const onNext = () => go(1);
  if (pagePrev) pagePrev.addEventListener('click', onPrev);
  if (pageNext) pageNext.addEventListener('click', onNext);
  if (pagePrevTab) pagePrevTab.addEventListener('click', onPrev);
  if (pageNextTab) pageNextTab.addEventListener('click', onNext);
  if (pageDots) pageDots.addEventListener('click', e => {
    const dot = e.target.closest('.page-dot');
    if (!dot || flipping) return;
    jumpTo(+dot.dataset.page);
  });
  if (notepad){
    notepad.addEventListener('keydown', e => {
      if (!notepad.contains(e.target) && e.target !== notepad) return;
      if (e.target.matches('input, textarea')) return; /* don't steal typing */
      if (e.key === 'ArrowLeft'){ e.preventDefault(); go(-1); }
      if (e.key === 'ArrowRight'){ e.preventDefault(); go(1); }
    });
  }
}

function initPad(){
  const canFlip = !reducePaper && typeof Element.prototype.animate === 'function';
  mode = canFlip ? 'flip' : 'flat';
  bindNav();
  if (mode === 'flip'){
    try {
      buildFlipLeaves();
      console.info('[pad] FlipRenderer active (CSS 3D two-face turn, WAAPI). Adapter: sheetIndex === pageIndex (portrait).');
    } catch (err){
      console.warn('[pad] Flip init failed, FlatRenderer fallback', err);
      mode = 'flat';
      buildFlat(false);
    }
  } else {
    buildFlat(false);
    console.info('[pad] FlatRenderer active', reducePaper ? '(reduced-motion)' : '(no WAAPI)');
  }
  updateChrome();
}

function renderRows(){ hydrateLiveSheet(pageIndex); }

/* ---------- Focus-the-demo CTAs ---------- */
function focusDemo(){
  const card = notepad || $('.sheet');
  if (card) card.scrollIntoView({behavior:'smooth', block:'center'});
  setTimeout(() => { const fi = $('#fItem'); if (fi) fi.focus({preventScroll:true}); }, 450);
}
$('#heroCta').addEventListener('click', focusDemo);
$$('[data-focus-demo]').forEach(b => b.addEventListener('click', focusDemo));
