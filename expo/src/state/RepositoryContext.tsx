/**
 * Repository context — provides the EntryRepository and reactive entry list,
 * plus persisted user preferences (currency, onboarding flag).
 *
 * This fixes the DI gap in the native app (where .environment injection of
 * EntryRepository was missing). All descendants access data through this hook.
 */
import { createContext, useCallback, useContext, useEffect, useMemo, useRef, useState, type ReactNode } from "react";
import { EntryRepository } from "../data/EntryRepository";
import { getDb, getPref, setPref } from "../data/db";
import { DEFAULT_CURRENCY, type CurrencyCode, type Entry } from "../data/schema";

interface RepositoryContextValue {
  repo: EntryRepository | null;
  entries: Entry[];
  currency: CurrencyCode;
  setCurrency: (code: CurrencyCode) => Promise<void>;
  hasOnboarded: boolean;
  setHasOnboarded: (value: boolean) => Promise<void>;
  /** Re-fetch entries from the DB after a mutation. */
  refresh: () => Promise<void>;
  loading: boolean;
}

const Ctx = createContext<RepositoryContextValue | null>(null);

const PREF_CURRENCY = "currency";
const PREF_ONBOARDED = "hasOnboarded";

export function RepositoryProvider({ children }: { children: ReactNode }) {
  const [repo, setRepo] = useState<EntryRepository | null>(null);
  const [entries, setEntries] = useState<Entry[]>([]);
  const [currency, setCurrencyState] = useState<CurrencyCode>(DEFAULT_CURRENCY);
  const [hasOnboarded, setHasOnboardedState] = useState(false);
  const [loading, setLoading] = useState(true);
  const dbRef = useRef<Awaited<ReturnType<typeof getDb>> | null>(null);

  const refresh = useCallback(async () => {
    if (!repo) return;
    const all = await repo.all();
    setEntries(all);
  }, [repo]);

  // Initialize DB + repository + load prefs.
  useEffect(() => {
    let cancelled = false;
    (async () => {
      const db = await getDb();
      if (cancelled) return;
      dbRef.current = db;
      const repository = new EntryRepository(db);
      setRepo(repository);

      // Load persisted prefs.
      const savedCurrency = await getPref(db, PREF_CURRENCY);
      const savedOnboarded = await getPref(db, PREF_ONBOARDED);
      if (cancelled) return;
      if (savedCurrency) setCurrencyState(savedCurrency as CurrencyCode);
      setHasOnboardedState(savedOnboarded === "true");

      // 35-day silent auto-purge (once per launch).
      await repository.purgeOlderThan();

      const all = await repository.all();
      if (cancelled) return;
      setEntries(all);
      setLoading(false);
    })();
    return () => {
      cancelled = true;
    };
  }, []);

  const setCurrency = useCallback(async (code: CurrencyCode) => {
    setCurrencyState(code);
    if (dbRef.current) await setPref(dbRef.current, PREF_CURRENCY, code);
  }, []);

  const setHasOnboarded = useCallback(async (value: boolean) => {
    setHasOnboardedState(value);
    if (dbRef.current) await setPref(dbRef.current, PREF_ONBOARDED, String(value));
  }, []);

  const value = useMemo<RepositoryContextValue>(
    () => ({
      repo,
      entries,
      currency,
      setCurrency,
      hasOnboarded,
      setHasOnboarded,
      refresh,
      loading,
    }),
    [repo, entries, currency, hasOnboarded, setCurrency, setHasOnboarded, refresh, loading],
  );

  return <Ctx.Provider value={value}>{children}</Ctx.Provider>;
}

export function useRepository(): RepositoryContextValue {
  const ctx = useContext(Ctx);
  if (!ctx) throw new Error("useRepository must be used within RepositoryProvider");
  return ctx;
}
