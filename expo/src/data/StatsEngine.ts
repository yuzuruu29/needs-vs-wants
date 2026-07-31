/**
 * Pure, deterministic aggregation of entries over a period window.
 *
 * Faithful port of StatsEngine.swift. Separated from the repository so it can
 * be unit-tested with plain Entry arrays and no database.
 */
import { EMPTY_STATS, type Entry, type Period, type SummaryStats } from "./schema";

const MS_PER_DAY = 24 * 60 * 60 * 1000;

export class StatsEngine {
  constructor(
    private readonly now: number = Date.now(),
  ) {}

  /**
   * Inclusive start of the window for a period (epoch ms).
   * - day:  start of today (local midnight)
   * - week: today minus 6 days (7-day inclusive window)
   * - all:  today minus 34 days (35-day inclusive window, matches retention)
   */
  startOf(period: Period): number {
    const today = startOfDay(this.now);
    switch (period) {
      case "day":
        return today;
      case "week":
        return today - 6 * MS_PER_DAY;
      case "all":
        return today - 34 * MS_PER_DAY;
    }
  }

  stats(period: Period, entries: Entry[]): SummaryStats {
    const since = this.startOf(period);
    const s: SummaryStats = { ...EMPTY_STATS };
    for (const e of entries) {
      if (e.dateUtc < since) continue;
      if (e.typeRaw === "NEED") {
        s.needsTotalCents += e.costCents;
        s.needsCount += 1;
      } else {
        s.wantsTotalCents += e.costCents;
        s.wantsCount += 1;
      }
    }
    return s;
  }

  /** "Jul 29" or "Jul 23 – Jul 29" caption for the period rotor. */
  rangeCaption(period: Period): string {
    const fmt = new Intl.DateTimeFormat("en-US", { month: "short", day: "numeric" });
    if (period === "day") return fmt.format(this.now);
    return `${fmt.format(this.startOf(period))} – ${fmt.format(this.now)}`;
  }
}

/** Local-midnight start of the given epoch-ms timestamp. */
export function startOfDay(ts: number): number {
  const d = new Date(ts);
  d.setHours(0, 0, 0, 0);
  return d.getTime();
}

/** "yyyy-MM-dd" day key for grouping (local time). Mirrors Entry.dayKey. */
export function dayKey(ts: number): string {
  const d = new Date(ts);
  const y = d.getFullYear();
  const m = String(d.getMonth() + 1).padStart(2, "0");
  const day = String(d.getDate()).padStart(2, "0");
  return `${y}-${m}-${day}`;
}

/** "HH:mm" time label (local time). Mirrors Entry.timeLabel. */
export function timeLabel(ts: number): string {
  const d = new Date(ts);
  const h = String(d.getHours()).padStart(2, "0");
  const min = String(d.getMinutes()).padStart(2, "0");
  return `${h}:${min}`;
}
