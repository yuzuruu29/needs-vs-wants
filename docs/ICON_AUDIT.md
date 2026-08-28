# Icon Audit - Website Inline SVGs

Point-in-time inventory of every inline `svg` in `website/public/index.html`, taken 2026-08-28 as part of D185. This is a report, not policy. Policy lives in `docs/UI_GUIDELINES.md`.

## Method

1. Extract each svg element with its viewBox, fill, stroke, and path data signature.
2. Compare path data against Lucide, Feather, and Font Awesome path data. No signature matches any of them. The stroke icons use the same visual vocabulary (24x24, `fill="none"`, `stroke="currentColor"`) but the geometry is custom.
3. Verify authorship per unique signature with `git log -S "<path data>" -- website/public/index.html`.

## Result

52 inline SVGs in 17 unique groups. Every group is original work authored in this repository (commits `5f770b1c` 2026-08-03 and `f75b182e` 2026-08-05). No third-party icon asset was found. `LICENSE_REVIEW_REQUIRED` flags: none.

| Group | Count | ViewBox | Purpose | Signature start | Introduced |
|---|---|---|---|---|---|
| Check (header) | 5 | 0 0 24 24 | Header checkmark | `M8.6 12.3l2.3 2.3 4.6-4.7` | 5f770b1c |
| Star (Pro/Max notice) | 1 | 0 0 24 24 | Rating star | `M12 3l2.5 5.6 6.1.6-4.6 4.1...` | f75b182e |
| External-link arrow | 4 | 0 0 24 24 | Arrow-out links | `M7 17L17 7M9 7h8v8` | f75b182e |
| Need-panel mark | 1 | 0 0 24 24 | Need panel icon | `M4.5 12.5l7-8 7 8` | 5f770b1c |
| Check (exchange chips) | 28 | 0 0 24 24 | Confirm chips | `M5 12.5l4.5 4.5L19 7` | 5f770b1c |
| Want-panel mark | 1 | 0 0 24 24 | Want panel icon | `M12 3.5l2.1 4.6 4.9.6-3.6 3.4...` | 5f770b1c |
| Cart rule icon | 1 | 0 0 24 24 | How-it-works rule | `M3 3.5h2l2.3 11.7a1.6...` | 5f770b1c |
| Calendar rule icon | 1 | 0 0 24 24 | How-it-works rule | `M3.5 9.5h17M8 3v4M16 3v4` | 5f770b1c |
| Calendar rule icon (2) | 1 | 0 0 24 24 | How-it-works rule | `M4 8.5h16M8 3.5v3M16 3.5v3` | 5f770b1c |
| Retention dial | 1 | 0 0 220 220 | 30-day dial ring (circle + ticks) | structural | 5f770b1c |
| Donut chart | 1 | 0 0 200 200 | Need/Want donut (circles) | structural | 5f770b1c |
| Document mark | 1 | 0 0 24 24 | Ledger sheet icon | `M5 4.5A2.5 2.5 0 0 1 7.5 2H19v17...` | 5f770b1c |
| Trash (wipe) | 2 | 0 0 24 24 | Data-wipe button | `M4.5 6.5h15M9.5 6V4.5h5V6...` | 5f770b1c |
| CTA progress ring | 1 | 0 0 120 120 | Download progress arc | `M60,60 m-45,0 a45,45 0 1,1...` | 5f770b1c |
| Download arrow | 1 | 0 0 24 24 | APK download | `M12 4v10M7.5 10.5L12 15l4.5-4.5...` | 5f770b1c |
| QR grid | 1 | 0 0 24 24 | QR toggle | `M4 4h6v6H4zM14 4h6v6h-6z...` | 5f770b1c |
| Warning triangle | 1 | 0 0 24 24 | Studio mark | `M12 2.5 2.5 21h19Zm0 7-3.7 8h7.4Z` | 5f770b1c |

## Standing rule

New icons follow `docs/UI_GUIDELINES.md`. If an icon is copied from a library instead of authored, its license gate entry goes in this file before it ships.
