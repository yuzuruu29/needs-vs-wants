/**
 * Entry repository — data access layer.
 *
 * Faithful port of EntryRepository.swift. All writes return Result-style
 * booleans (no silent swallowing). Reads return plain Entry objects.
 *
 * Key behaviors preserved:
 *  - insert() stamps dateUtc = Date.now() (local TZ moment)
 *  - purgeOlderThan(35) silently deletes entries older than 35 days
 *  - sheetCount counts TOTAL entries (not per-day); isSheetFull at >= 20
 *  - stats() delegates to StatsEngine
 */
import { type SQLiteDatabase } from "expo-sqlite";
import { EMPTY_STATS, type Entry, type Period, type SummaryStats } from "./schema";
import { StatsEngine } from "./StatsEngine";

const SHEET_CAP = 20;
const RETENTION_DAYS = 35;
const MS_PER_DAY = 24 * 60 * 60 * 1000;

interface EntryRow {
  id: string;
  dateUtc: number;
  item: string;
  costCents: number;
  typeRaw: "NEED" | "WANT";
}

function rowToEntry(row: EntryRow): Entry {
  return { ...row };
}

export class EntryRepository {
  constructor(private readonly db: SQLiteDatabase) {}

  async insert(item: string, costCents: number, type: "NEED" | "WANT"): Promise<boolean> {
    try {
      const entry: Entry = {
        id: crypto.randomUUID(),
        dateUtc: Date.now(),
        item,
        costCents,
        typeRaw: type,
      };
      await this.db.runAsync(
        "INSERT INTO entries (id, dateUtc, item, costCents, typeRaw) VALUES (?, ?, ?, ?, ?)",
        [entry.id, entry.dateUtc, entry.item, entry.costCents, entry.typeRaw],
      );
      return true;
    } catch {
      return false;
    }
  }

  /** Explicit-date insert (for back-dating / tests). */
  async insertAt(dateUtc: number, item: string, costCents: number, type: "NEED" | "WANT"): Promise<boolean> {
    try {
      const entry: Entry = {
        id: crypto.randomUUID(),
        dateUtc,
        item,
        costCents,
        typeRaw: type,
      };
      await this.db.runAsync(
        "INSERT INTO entries (id, dateUtc, item, costCents, typeRaw) VALUES (?, ?, ?, ?, ?)",
        [entry.id, entry.dateUtc, entry.item, entry.costCents, entry.typeRaw],
      );
      return true;
    } catch {
      return false;
    }
  }

  async delete(id: string): Promise<boolean> {
    try {
      await this.db.runAsync("DELETE FROM entries WHERE id = ?", [id]);
      return true;
    } catch {
      return false;
    }
  }

  async deleteAll(): Promise<boolean> {
    try {
      await this.db.runAsync("DELETE FROM entries");
      return true;
    } catch {
      return false;
    }
  }

  /** Silently deletes entries older than `days`. Called once per launch. */
  async purgeOlderThan(days: number = RETENTION_DAYS): Promise<void> {
    const cutoff = Date.now() - days * MS_PER_DAY;
    await this.db.runAsync("DELETE FROM entries WHERE dateUtc < ?", [cutoff]);
  }

  /** Total entry count across the diary (not per-day). */
  async sheetCount(): Promise<number> {
    const row = await this.db.getFirstAsync<{ count: number }>("SELECT COUNT(*) as count FROM entries");
    return row?.count ?? 0;
  }

  async isSheetFull(): Promise<boolean> {
    return (await this.sheetCount()) >= SHEET_CAP;
  }

  /** All entries, newest first. */
  async all(): Promise<Entry[]> {
    const rows = await this.db.getAllAsync<EntryRow>(
      "SELECT * FROM entries ORDER BY dateUtc DESC",
    );
    return rows.map(rowToEntry);
  }

  async stats(period: Period, engine: StatsEngine): Promise<SummaryStats> {
    const entries = await this.all();
    return engine.stats(period, entries);
  }

  get sheetCap(): number {
    return SHEET_CAP;
  }
}

export { SHEET_CAP, RETENTION_DAYS, EMPTY_STATS };
