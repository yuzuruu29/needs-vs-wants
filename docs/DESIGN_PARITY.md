# Design parity notes

## Dark mode (v1)

- **iOS native** ships adaptive light/dark tokens (`AppColors`).
- **Android** remains light-only per D7.

This is an intentional platform divergence for v1, not an unfinished port.
Android dark mode can land as a follow-up if parity becomes a release requirement.

## Premium surfaces

iOS native mirrors Android `Components.kt` brush strokes:

| Helper | Role |
|---|---|
| `inkWashBackground()` | Soft vertical `surface` → `surfaceRaised` wash on every screen |
| `giltGlow()` | Radial gold behind the donut / empty diary |
| `GiltRule` | Short gold rule under section titles |
| `PeriodRotor` | Crimson → crimsonDeep selected segment |
| `LegendChip` | Need/Want legend under the donut |
| `AccentShareBar` | Single-accent bar under stat-card values |

Card corner radius is locked at **16** (`AppMetrics.cardRadius`) to match Android `GiltCard`.
