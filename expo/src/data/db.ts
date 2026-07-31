/**
 * SQLite database setup via expo-sqlite.
 *
 * Single table `entries` mirrors the SwiftData @Model Entry. costCents is
 * stored as INTEGER (currency-immune minor units), dateUtc as INTEGER (epoch
 * ms). The schema is intentionally minimal — one entity, no relationships.
 */
import { openDatabaseAsync, type SQLiteDatabase } from "expo-sqlite";

const DB_NAME = "needs_vs_wants.db";

let dbInstance: SQLiteDatabase | null = null;

export async function getDb(): Promise<SQLiteDatabase> {
  if (dbInstance) return dbInstance;
  dbInstance = await openDatabaseAsync(DB_NAME);
  await migrate(dbInstance);
  return dbInstance;
}

async function migrate(db: SQLiteDatabase): Promise<void> {
  await db.execAsync(`
    PRAGMA journal_mode = WAL;
    CREATE TABLE IF NOT EXISTS entries (
      id         TEXT    PRIMARY KEY NOT NULL,
      dateUtc    INTEGER NOT NULL,
      item        TEXT    NOT NULL,
      costCents  INTEGER NOT NULL,
      typeRaw    TEXT    NOT NULL CHECK(typeRaw IN ('NEED','WANT'))
    );
    CREATE INDEX IF NOT EXISTS idx_entries_date ON entries(dateUtc DESC);
  `);

  // Persisted user preferences (currency, onboarding flag).
  await db.execAsync(`
    CREATE TABLE IF NOT EXISTS prefs (
      key   TEXT PRIMARY KEY NOT NULL,
      value TEXT NOT NULL
    );
  `);
}

// ── Preferences helpers ─────────────────────────────────────────────────────

export async function getPref(db: SQLiteDatabase, key: string): Promise<string | null> {
  const row = await db.getFirstAsync<{ value: string }>(
    "SELECT value FROM prefs WHERE key = ?",
    [key],
  );
  return row?.value ?? null;
}

export async function setPref(db: SQLiteDatabase, key: string, value: string): Promise<void> {
  await db.runAsync("INSERT OR REPLACE INTO prefs (key, value) VALUES (?, ?)", [key, value]);
}
