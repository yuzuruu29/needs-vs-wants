/**
 * Formats currency-immune cents into a display string per the chosen currency.
 *
 * Faithful port of CurrencyFormatter.swift. Uses Intl.NumberFormat with the
 * currency's locale, so symbol placement, grouping, and — crucially —
 * minor-unit rules are correct. JPY (and other zero-minor currencies) show
 * whole units with no decimals.
 */
import { CURRENCIES, type CurrencyCode } from "./schema";

const formatterCache = new Map<string, Intl.NumberFormat>();

function getFormatter(code: CurrencyCode): Intl.NumberFormat {
  const cached = formatterCache.get(code);
  if (cached) return cached;

  const meta = CURRENCIES[code];
  const formatter = new Intl.NumberFormat(meta.locale, {
    style: "currency",
    currency: code,
    minimumFractionDigits: meta.zeroMinor ? 0 : 2,
    maximumFractionDigits: meta.zeroMinor ? 0 : 2,
  });
  formatterCache.set(code, formatter);
  return formatter;
}

export function formatCents(cents: number, currency: CurrencyCode): string {
  const meta = CURRENCIES[currency];
  // Zero-minor currencies: whole units, no decimals.
  const units = meta.zeroMinor ? Math.trunc(cents / 100) : cents / 100;
  return getFormatter(currency).format(units);
}
