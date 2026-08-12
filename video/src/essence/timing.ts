/**
 * "The Choice" campaign — frame maps (30 fps).
 *
 * Three cuts, one design system:
 *  - EssenceAd      1920x1080  1440f (48s) — flagship essence + how-to
 *  - EssenceReel    1080x1920   720f (24s) — vertical social cut
 *  - EssenceSquare  1080x1080   450f (15s) — square feed teaser
 */

export const AD = {
  hook: 0, // Every peso asks one question -> NEED / WANT stamps
  essence: 150, // ledger vs trainer + receipt stamp
  log: 330, // 01 log it as you spend (phone demo)
  see: 600, // 02 see the honest split (donut)
  guard: 840, // 03 guard your day (budget + Log anyway?)
  proof: 1050, // four proof cards
  close: 1230, // lockup + CTA
  total: 1440,
} as const;

export const REEL = {
  hook: 0,
  log: 96,
  see: 306,
  guard: 456,
  close: 576,
  total: 720,
} as const;

export const SQ = {
  hook: 0,
  split: 96,
  seal: 246,
  close: 330,
  total: 450,
} as const;

/**
 * Real-life scenario cuts (both 1080x1920, 900f / 30s).
 * PAYDAY — "Sweldo Day": three purchases witnessed hour by hour.
 * PETSA — "Petsa de Peligro": P500 across the five days before payday.
 */
export const PAYDAY = {
  hook: 0, // SWELDO stamp, "the most expensive day of the month"
  m1: 96, // 10:12 AM  iced latte P180  WANT
  m2: 222, // 12:40 PM  rice + ulam P320  NEED
  m3: 348, // 8:47 PM  flash-sale earbuds P1,299  WANT (hesitation beat)
  summary: 492, // day donut resolves to Need 18 / Want 82
  insight: 648, // "wants today: P1,479"
  close: 756,
  total: 900,
} as const;

export const PETSA = {
  hook: 0, // "P500. Five days."
  set: 96, // set the P100 daily line
  day1: 216, // lugaw + jeep P78, NEED, under the line
  day3: 336, // milk tea temptation -> Log anyway? -> Cancel
  day5: 492, // summary: still standing
  insight: 648, // "you didn't earn more, you leaked less"
  close: 756,
  total: 900,
} as const;

/**
 * Scenario pack 2 — bigger set pieces (all 1080x1920, 900f / 30s):
 * MIDNIGHT flash-sale hold, MILKTEA compounding math, STREAK 30-day rhythm,
 * BARKADA bill split, SUBS subscription audit.
 */
export const MIDNIGHT = {
  hook: 0, // 11:59 flip clock, sale tags swinging
  urge: 108, // gaming mouse typed, WANT selected, Max hold card
  hold: 276, // "hold it for one night" — clock lapse, sunrise
  morning: 444, // 8:00 AM, ledger untouched, money kept
  insight: 612,
  close: 744,
  total: 900,
} as const;

export const MILKTEA = {
  hook: 0, // one cup, swinging P120 tag
  multiply: 96, // 30-cup grid + odometer to P3,600 then P43,200
  log: 300, // the honest WANT seal
  see: 462, // week summary, wants circled in gold
  insight: 612,
  close: 744,
  total: 900,
} as const;

export const STREAK = {
  hook: 0, // DAY 1 odometer + HONEST stamp
  montage: 96, // rhythm: rows rain in, day counter rolls, milestones pulse
  app: 372, // All (30d) summary donut
  insight: 564,
  close: 720,
  total: 900,
} as const;

export const BARKADA = {
  hook: 0, // receipt roll + clink
  split: 108, // 1,920 / 4 = 480 flying chips
  log: 264, // your share sealed WANT
  allowed: 432, // "Wants are allowed."
  insight: 588,
  close: 732,
  total: 900,
} as const;

export const SUBS = {
  hook: 0, // cards cascade in
  stack: 108, // monthly total odometer, x12 year roll
  log: 288, // log the recurring ones honestly
  audit: 456, // one forgotten card flips and falls
  insight: 624,
  close: 756,
  total: 900,
} as const;
