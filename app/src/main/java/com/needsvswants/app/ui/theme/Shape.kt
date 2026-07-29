package com.needsvswants.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Premium shape scale. Larger, calmer corners — 16-20dp on cards give an
// editorial, app-like feel; 12dp on chips keeps tactility.
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(8.dp),       // chips, badges
    medium = RoundedCornerShape(16.dp),     // cards, dialogs, inputs
    large = RoundedCornerShape(20.dp),      // hero cards
    extraLarge = RoundedCornerShape(28.dp)  // sheets, big overlays
)
