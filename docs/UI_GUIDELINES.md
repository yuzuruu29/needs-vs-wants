# UI Guidelines

Policy for building and changing the app and website interfaces. Adopted from the vault guides "Frontend Reference & Anti-AI-Slop Guide" and "Icon Resource & Implementation Guide" (`Knowledge Base/Design/`, 2026-08-28). Decision D185 records the adoption. This is a reference document: facts and rules, no tutorials.

## Surfaces

| Surface | Stack | Icon library |
|---|---|---|
| Android app | Kotlin, Jetpack Compose, Material 3 | Material Icons (`material-icons-extended`) |
| Soft-launch website | Static HTML, CSS, JS (`website/`) | Inline SVG, hand-authored |

The reference stack in the frontend guide (shadcn/ui, ReUI, beUI, Transitions.dev and the rest) targets React. Neither surface uses React, so those libraries do not apply directly. Apply the guide's principles instead: borrow patterns, never identities, and normalize every borrowed pattern into project tokens.

## Component selection order

1. An existing component or pattern on that surface.
2. A composition of existing primitives. On Android that means Material 3 components plus the project's branded components (`Components.kt`, `MotionPrimitives.kt`). On the web that means the panel, chip, and bezel classes.
3. A pattern studied from the approved reference list, rebuilt with project tokens.
4. A custom implementation, justified in the decision note.

Introducing a new UI toolkit or component library is a decision-numbered change, not a drive-by.

## Icons

One primary library per surface. Never mix icon families on one screen.

- The app's icon weight standard is Outlined. Write `Icons.Outlined.X` and `Icons.AutoMirrored.Outlined.X`. `Icons.Default` and `Icons.Filled` are the Filled weight; do not use them for standalone icons.
- Weight as state is the one exception. The bottom navigation pairs each item's Filled variant (selected) with its Outlined variant (unselected) in `AppNavigation.kt` `bottomNavItems`. New selected/unselected pairs follow that pattern. Everything else stays Outlined.
- Select by meaning first. A delete action gets a trash glyph, not whatever looks decorative.
- Sizes. App icons in dp, web icons in px. Dense rows 16, buttons and inputs 18 to 20, section headers 20 to 24, feature moments 28 to 32. Icon size and touch target are separate concerns. A 20dp icon sits inside a 44 to 48dp target.
- Color. Functional icons inherit color (`currentColor` on the web, default tint on Compose). Semantic tints come from the theme palette (crimson, marketGreen, gilt), never a new hex.
- Accessibility. Classify every icon. Decorative icons get `aria-hidden="true"` on the web and `contentDescription = null` in Compose when adjacent text carries the meaning. Icon-only controls need an accessible name (`aria-label`, `contentDescription`).
- Motion. Animate an icon only when the motion communicates state (menu to close, chevron flip, play to pause). Reduced motion collapses state changes to instant (D88, D184).
- Licensing. Record library, license, and commercial terms before a new icon asset ships. The current website inventory and its provenance live in `docs/ICON_AUDIT.md`.

## Motion

Motion lives in `app/src/main/java/com/needsvswants/app/ui/theme/Motion.kt` and `MotionPrimitives.kt`. Use the existing tokens. Do not inline raw durations or easings.

| Guide vocabulary | Project token |
|---|---|
| motion.instant (level 0) | reduced-motion collapse, 1ms |
| motion.fast (levels 1 to 2) | `RecoilMs`, press springs |
| motion.default (level 3) | `TabGlideMs`, entrance and exit tweens |
| motion.slow (levels 3 to 4) | `OdometerRollMs`, `ReceiptUnrollMs` |

Most product UI stays at levels 0 to 3. Levels 4 and 5 are reserved for the existing signature moments (orb, odometer, activation seal). Before adding motion, ask whether it explains a state change or only delays the action. If it only delays, remove it.

## Normalization pass

Any pattern borrowed from a reference, a screenshot, or an AI suggestion becomes native before merge.

- Typography matches the Inter Tight and Playfair Display SC roles and the D95 text-scale system.
- Geometry uses the DoubleBezelCard and panel vocabulary. No foreign radius, shadow, or border.
- Color comes from the D7 supermarket-premium palette. No imported hex values.
- Spacing, elevation, and density follow the receiving screen, not the source design.
- Icons swap to the surface's library and weight standard.
- Motion swaps to the Motion tokens.

## Anti-slop

Do not ship: automatic cardification of every content block, pill overload, decorative gradients, oversized hero type inside app screens, centered everything, whitespace as a proxy for premium, glassmorphism beyond the D184 nav island, motion without a state change to explain. The project constraint holds: the product must not look AI-generated.

## QA checklist

Small UI edits run the short list.

- Tokens used (color, spacing, motion, type scale)
- States handled: loading, empty, error, disabled
- Touch targets 44 to 48dp
- Icon classification and accessible names
- Reduced motion respected
- Extra large text pass (D95 to D98, D173)

Feature-sized work and releases add the full checklist from the vault frontend guide, plus the dark and high-contrast appearances (D42) in the app and a responsive and keyboard pass on the website.

## References

- Vault guide, frontend: `Knowledge Base/Design/Frontend Reference & Anti-AI-Slop Guide.md`
- Vault guide, icons: `Knowledge Base/Design/Icon Resource & Implementation Guide.md`
- Website icon inventory: `docs/ICON_AUDIT.md`
- Related decisions: D7 (theme), D22 (pinned CDN precedent), D42 (appearance), D88 and D184 (motion), D95 to D98 and D173 (text scaling), D185 (this policy)
