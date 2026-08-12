# i18n staging plan (string extraction)

Audit gap: all UI copy is hardcoded English string literals in composables
(`res/values/strings.xml` held only `app_name` + flavor label), which blocks a
future Filipino/Taglish locale for the PH market.

Approach — staged extraction, one surface per PR so each diff stays reviewable
and each stage ships behind green tests:

| Stage | Surface | Files | Status |
|-------|---------|-------|--------|
| 1 | Settings: Notifications / Backup / Privacy / Data sections, their dialogs, currency caption | `ui/screens/settings/SettingsScreen.kt` + `res/values/strings.xml` | done 2026-08-13 |
| 1b | Rest of Settings (Membership, Currency list, Text size, Appearance, Plan, Quota, Feedback, About) + SettingsViewModel feedback strings (needs resource access in VM) | same files | pending |
| 2 | Onboarding + Instructions overlay | `InstructionsOverlay`, onboarding steps | pending |
| 3 | Log (Input) + dialogs | `ui/screens/input/**` | pending |
| 4 | Summary + History | `ui/screens/summary/**`, `ui/screens/history/**` | pending |
| 5 | Paywall + Advisor (money copy last, after copy stabilizes) | `ui/screens/paywall/**`, `ui/screens/advisor/**` | pending |
| 6 | `values-fil/strings.xml` Filipino translation pass | new resource dir | pending |

Conventions:
- Key naming: `<screen>_<element>` (`settings_backup_title`), reuse via
  `common_*` only for true duplicates (Cancel, Save).
- Interpolations use positional args (`%1$s`), never string concatenation.
- Formatted money/time stays in code (CurrencyFormatter / formatHour12);
  only the surrounding template is a resource.
- Domain-layer strings that tests pin verbatim (advisor citations, CSV
  headers) are NOT resources — they are data contracts, not UI copy.
