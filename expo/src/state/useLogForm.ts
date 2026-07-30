/**
 * Auto-seal form state for Log — port of LogViewModel.swift.
 */
import { useCallback, useEffect, useRef, useState } from "react";
import * as Haptics from "../design/Haptics";
import { type EntryType } from "../data/schema";
import { useRepository } from "./RepositoryContext";

export function parseCostCents(costText: string): number | null {
  const cleaned = costText.trim().replace(",", ".");
  const value = Number(cleaned);
  if (!Number.isFinite(value) || value <= 0) return null;
  return Math.round(value * 100);
}

export function useLogForm() {
  const { repo, refresh, entries } = useRepository();
  const [item, setItem] = useState("");
  const [costText, setCostText] = useState("");
  const [type, setType] = useState<EntryType | null>(null);
  const sealingRef = useRef(false);

  const sheetCount = entries.length;
  const isSheetFull = sheetCount >= 20;

  const costCents = parseCostCents(costText);
  const canSeal = Boolean(item.trim() && costCents != null && type != null);

  const reset = useCallback(() => {
    setItem("");
    setCostText("");
    setType(null);
  }, []);

  const sealIfPossible = useCallback(async () => {
    if (!repo || !canSeal || isSheetFull || costCents == null || type == null || sealingRef.current) {
      return false;
    }
    sealingRef.current = true;
    const ok = await repo.insert(item.trim(), costCents, type);
    if (ok) {
      await Haptics.seal();
      reset();
      await refresh();
    }
    sealingRef.current = false;
    return ok;
  }, [repo, canSeal, isSheetFull, costCents, type, item, reset, refresh]);

  const prevCanSeal = useRef(false);
  useEffect(() => {
    if (canSeal && !prevCanSeal.current && !isSheetFull) {
      void sealIfPossible();
    }
    prevCanSeal.current = canSeal;
  }, [canSeal, isSheetFull, sealIfPossible]);

  const startNewSheet = useCallback(async () => {
    if (!repo) return;
    await repo.deleteAll();
    await Haptics.success();
    reset();
    await refresh();
  }, [repo, reset, refresh]);

  const pickType = useCallback(
    (t: EntryType) => {
      setType(t);
    },
    [],
  );

  return {
    item,
    setItem,
    costText,
    setCostText,
    type,
    pickType,
    canSeal,
    isSheetFull,
    sheetCount,
    startNewSheet,
    sealIfPossible,
  };
}

/** Today label for Log header — e.g. "Thu, Jul 30". */
export function todayCaption(): string {
  return new Intl.DateTimeFormat("en-US", {
    weekday: "short",
    month: "short",
    day: "numeric",
  }).format(new Date());
}
