package com.needsvswants.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * The house corner scale (D188). Every RoundedCornerShape in the app resolves to
 * one of these seven steps: 6 / 8 / 12 / 14 / 16 / 20 / 28. Before consolidation
 * the codebase shipped 18 distinct literal radii: 8 absorbed the 7/9/10 chip
 * radii, 20 absorbed the 18/22 hero-card radii, 6 absorbed the 2-5 micro radii,
 * and 12/14 stay as de-facto steps.
 */
object AppShapes {
    val r6 = RoundedCornerShape(6.dp)     // swatches, ticks, micro chips
    val r8 = RoundedCornerShape(8.dp)     // chips, badges, inner wells
    val r12 = RoundedCornerShape(12.dp)   // buttons, fields, small cards
    val r14 = RoundedCornerShape(14.dp)   // bezel cores, mid rows
    val r16 = RoundedCornerShape(16.dp)   // standard cards, dialogs, inputs
    val r20 = RoundedCornerShape(20.dp)   // hero cards, showpiece surfaces, sheets
    val r28 = RoundedCornerShape(28.dp)   // floating islands, stadium overlays

    /**
     * Material3 slot mapping so framework components (dialogs, menus, snackbars)
     * inherit the same scale. 12 and 14 are house-only steps with no M3 slot.
     */
    fun asMaterialShapes(): Shapes = Shapes(
        extraSmall = r6,
        small = r8,
        medium = r16,
        large = r20,
        extraLarge = r28
    )
}
