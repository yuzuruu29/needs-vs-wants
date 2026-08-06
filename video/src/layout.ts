/**
 * Canvas layout constants shared by PhoneFrame, screens and scenes.
 * Canvas: 1440 x 2560.
 *
 * Phone sits slightly higher than the original 600 top to reduce dead paper
 * above the hero while keeping caption room below.
 */
export const LAYOUT = {
  W: 1440,
  H: 2560,
  PHONE_W: 660,
  PHONE_H: 1360,
  PHONE_TOP: 540,
  PHONE_LEFT: 390, // (1440 - 660) / 2
  PHONE_RADIUS: 108,
  BEZEL_X: 24, // bezel thickness left/right
  BEZEL_TOP: 34,
  BEZEL_BOTTOM: 26,
  STATUS_H: 74,
} as const;

export const SCREEN = {
  X: LAYOUT.PHONE_LEFT + LAYOUT.BEZEL_X,
  Y: LAYOUT.PHONE_TOP + LAYOUT.BEZEL_TOP,
  W: LAYOUT.PHONE_W - LAYOUT.BEZEL_X * 2,
  H: LAYOUT.PHONE_H - LAYOUT.BEZEL_TOP - LAYOUT.BEZEL_BOTTOM - LAYOUT.STATUS_H,
};

/** Canvas origin of the screen-content area (below the status bar). */
export const CONTENT = {
  X: SCREEN.X,
  Y: SCREEN.Y + LAYOUT.STATUS_H,
  W: SCREEN.W,
  H: SCREEN.H,
};

/**
 * Canvas coords of interactive targets inside the Log screen (budget-off).
 * Measured from rendered stills; Y shifted −60 with PHONE_TOP 600→540.
 */
export const TARGETS = {
  itemField: { x: 686, y: 929 },
  costField: { x: 536, y: 1032 },
  needChip: { x: 748, y: 1032 },
  wantChip: { x: 908, y: 1032 },
  chipsMid: { x: 828, y: 1032 }, // between the two chips — hook macro origin
  donutCenter: { x: 720, y: 1180 },
};

/** Default lower-third caption Y (balanced composition). */
export const CAPTION_Y = 2060;
