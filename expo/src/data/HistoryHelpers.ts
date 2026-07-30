/** Groups entries by day for History. Port of HistoryViewModel.swift. */
import { dayKey } from "./StatsEngine";
import { type Entry } from "./schema";

export interface DayTotals {
  needs: number;
  wants: number;
}

export function groupedByDay(entries: Entry[]): { key: string; entries: Entry[] }[] {
  const map = new Map<string, Entry[]>();
  for (const e of entries) {
    const k = dayKey(e.dateUtc);
    const list = map.get(k);
    if (list) list.push(e);
    else map.set(k, [e]);
  }
  return [...map.entries()]
    .sort((a, b) => (a[0] > b[0] ? -1 : 1))
    .map(([key, dayEntries]) => ({
      key,
      entries: dayEntries.sort((a, b) => b.dateUtc - a.dateUtc),
    }));
}

export function dayTotals(dayEntries: Entry[]): DayTotals {
  let needs = 0;
  let wants = 0;
  for (const e of dayEntries) {
    if (e.typeRaw === "NEED") needs += e.costCents;
    else wants += e.costCents;
  }
  return { needs, wants };
}

export function formatDayHeader(key: string): string {
  const [y, m, d] = key.split("-").map(Number);
  if (!y || !m || !d) return key;
  return new Intl.DateTimeFormat("en-US", { dateStyle: "medium" }).format(new Date(y, m - 1, d));
}
