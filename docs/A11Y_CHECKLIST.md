# Accessibility checklist (TalkBack manual pass)

Status 2026-08-13: the design system centralizes most semantics — `HeaderIconWell`
requires a `contentDescription` and announces as a button, nav pills announce as
tabs with selection state, streak/live values use polite live regions, and icon
wells are 48dp with nav pills at 56dp minimum. What ships below is the manual
verification matrix that has NOT been run on a device yet — run it with TalkBack
on before the next release and tick items here.

## How to run

Settings → Accessibility → TalkBack → on. Navigate by swipe only (no vision).

## Matrix

### Log (Input)
- [ ] Every ledger field announces its column (Date, Time, Item, Cost, Type)
- [ ] Need/Want type choice announces the selected state
- [ ] Seal action announces success (and the over-budget confirm is readable)
- [ ] Daily budget meter value is announced when it changes (live region)

### Summary
- [ ] Period selector (Day/Week/All) announces selection
- [ ] Ring chart values reachable as text (Need total, Want total, split)
- [ ] Streak changes announced politely, not interruptively

### History
- [ ] Search field announces its label; clear/filters reachable
- [ ] All/Need/Want filter chips announce selected state
- [ ] Entry rows read item · time · amount · type in one focus stop
- [ ] Long-press edit/delete affordance is reachable via actions menu

### Paywall
- [ ] Plan cards announce plan, price, and selected state
- [ ] Monthly/Annual selector announces selection
- [ ] "Continue with Google" / email-code fallback reachable and labeled
- [ ] Error banners are announced when they appear

### Settings
- [ ] Every toggle announces its state; every row announces its value
- [ ] Reminder time picker hours are individually focusable
- [ ] Backup rows (folder, back up now, restore) fully labeled

## Known gaps (tracked)
- UI copy is hardcoded English (i18n staging plan — see the repo plan);
  TalkBack reads English regardless of system language.
- Charts expose totals as text but not per-slice values.
