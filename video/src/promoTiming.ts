/** Shared timing for the 45s 16:9 marketing promo (30fps). */

export const DUR_HOOK = 120; // 0–4s
export const DUR_PURPOSE = 150; // 4–9s
export const DUR_SEE = 180; // 9–15s
export const DUR_SEAL = 390; // 15–28s
export const DUR_GUARD = 180; // 28–34s
export const DUR_BENEFITS = 180; // 34–40s
export const DUR_CLOSE = 150; // 40–45s

export const PROMO_TOTAL_FRAMES =
  DUR_HOOK +
  DUR_PURPOSE +
  DUR_SEE +
  DUR_SEAL +
  DUR_GUARD +
  DUR_BENEFITS +
  DUR_CLOSE; // 1350

export const PROMO_T = {
  hook: 0,
  purpose: DUR_HOOK,
  see: DUR_HOOK + DUR_PURPOSE,
  seal: DUR_HOOK + DUR_PURPOSE + DUR_SEE,
  guard: DUR_HOOK + DUR_PURPOSE + DUR_SEE + DUR_SEAL,
  benefits: DUR_HOOK + DUR_PURPOSE + DUR_SEE + DUR_SEAL + DUR_GUARD,
  close:
    DUR_HOOK +
    DUR_PURPOSE +
    DUR_SEE +
    DUR_SEAL +
    DUR_GUARD +
    DUR_BENEFITS,
} as const;
