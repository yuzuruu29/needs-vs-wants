/**
 * Domain types — faithful port of the native iOS schema.
 *
 * Source of truth: ios-native/NeedsVsWants/Data/{Entry,EntryType,Period,
 * CurrencyOption,SummaryStats}.swift
 *
 * Key invariants preserved:
 *  - costCents is Int64 (currency-immune minor units)
 *  - dateUtc is a UTC epoch milliseconds timestamp
 *  - typeRaw is "NEED" | "WANT"
 */

/** Need vs Want classification. Mirrors EntryType.swift. */
export type EntryType = "NEED" | "WANT";

/** Aggregation window. Mirrors Period.swift. */
export type Period = "day" | "week" | "all";

/** Supported currencies. Mirrors CurrencyOption.swift. */
export type CurrencyCode = "PHP" | "USD" | "EUR" | "JPY" | "SGD";

/** A single sealed expense entry. Mirrors @Model Entry.swift. */
export interface Entry {
  id: string;
  /** UTC epoch milliseconds — single source of truth for the moment. */
  dateUtc: number;
  item: string;
  /** Currency-immune minor units (e.g. ₱12.50 → 1250). */
  costCents: number;
  typeRaw: "NEED" | "WANT";
}

/** Aggregated totals for a period window. Mirrors SummaryStats.swift. */
export interface SummaryStats {
  needsTotalCents: number;
  wantsTotalCents: number;
  needsCount: number;
  wantsCount: number;
}

export const EMPTY_STATS: SummaryStats = {
  needsTotalCents: 0,
  wantsTotalCents: 0,
  needsCount: 0,
  wantsCount: 0,
};

// ── Derived helpers (mirror the Swift computed properties) ──────────────────

export function entryType(e: Entry): EntryType {
  return e.typeRaw as EntryType;
}

export function totalCents(s: SummaryStats): number {
  return s.needsTotalCents + s.wantsTotalCents;
}

export function totalCount(s: SummaryStats): number {
  return s.needsCount + s.wantsCount;
}

/** Need fraction 0–1. Empty window → 0 (no division by zero). */
export function needPct(s: SummaryStats): number {
  const total = totalCents(s);
  return total > 0 ? s.needsTotalCents / total : 0;
}

/** Want fraction 0–1. */
export function wantPct(s: SummaryStats): number {
  const total = totalCents(s);
  return total > 0 ? s.wantsTotalCents / total : 0;
}

// ── Currency metadata — mirrors CurrencyOption.swift ────────────────────────

export interface CurrencyMeta {
  code: CurrencyCode;
  symbol: string;
  displayName: string;
  /** BCP-47 locale that drives correct decimal/grouping/symbol placement. */
  locale: string;
  /** True for zero-minor-unit currencies (JPY). */
  zeroMinor: boolean;
}

export const CURRENCIES: Record<CurrencyCode, CurrencyMeta> = {
  PHP: { code: "PHP", symbol: "₱", displayName: "₱ PHP", locale: "en-PH", zeroMinor: false },
  USD: { code: "USD", symbol: "$", displayName: "$ USD", locale: "en-US", zeroMinor: false },
  EUR: { code: "EUR", symbol: "€", displayName: "€ EUR", locale: "en-IE", zeroMinor: false },
  JPY: { code: "JPY", symbol: "¥", displayName: "¥ JPY", locale: "ja-JP", zeroMinor: true },
  SGD: { code: "SGD", symbol: "S$", displayName: "S$ SGD", locale: "en-SG", zeroMinor: false },
};

export const CURRENCY_LIST: CurrencyMeta[] = [
  CURRENCIES.PHP,
  CURRENCIES.USD,
  CURRENCIES.EUR,
  CURRENCIES.JPY,
  CURRENCIES.SGD,
];

export const DEFAULT_CURRENCY: CurrencyCode = "PHP";

// ── Period labels — mirrors Period.swift ────────────────────────────────────

export const PERIOD_LABELS: Record<Period, string> = {
  day: "Day",
  week: "Week",
  all: "All (35d)",
};

export const PERIOD_LIST: Period[] = ["day", "week", "all"];
