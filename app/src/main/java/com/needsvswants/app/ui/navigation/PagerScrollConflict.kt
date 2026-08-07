package com.needsvswants.app.ui.navigation

import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Velocity
import kotlin.math.abs

/**
 * Fixes HorizontalPager fighting vertical scroll (up/down accidentally paging left/right).
 *
 * On the first meaningful drag we lock to one axis:
 * - **Vertical** → residual X is swallowed; [allowPagerUserScroll] goes false so the pager
 *   itself cannot page-turn for the rest of the gesture.
 * - **Horizontal** → normal pager nested-scroll (tab change).
 *
 * Use [rememberPagerScrollGate] for both the pager flag and nested connection.
 */
private enum class DragAxis { Horizontal, Vertical }

class PagerScrollGate internal constructor(
    private val delegate: NestedScrollConnection?,
) {
    /** When false, HorizontalPager.userScrollEnabled should be false. */
    val allowPagerUserScroll = mutableStateOf(true)

    val connection: NestedScrollConnection = object : NestedScrollConnection {
        private var locked: DragAxis? = null

        private fun lockFrom(delta: Offset) {
            if (locked != null) return
            val ax = abs(delta.x)
            val ay = abs(delta.y)
            // Need a bit of movement before locking (below touch slop noise).
            if (ax < 2f && ay < 2f) return
            // Prefer vertical unless clearly horizontal (~30° from horizontal axis).
            locked = if (ax > ay * 1.75f) DragAxis.Horizontal else DragAxis.Vertical
            allowPagerUserScroll.value = locked == DragAxis.Horizontal
        }

        private fun reset() {
            locked = null
            allowPagerUserScroll.value = true
        }

        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            if (source == NestedScrollSource.UserInput || source == NestedScrollSource.SideEffect) {
                lockFrom(available)
            }
            return when (locked) {
                DragAxis.Vertical -> Offset.Zero
                DragAxis.Horizontal -> delegate?.onPreScroll(available, source) ?: Offset.Zero
                null -> Offset.Zero
            }
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            lockFrom(consumed + available)
            return when (locked) {
                // Swallow leftover X so the pager never receives a horizontal page delta.
                DragAxis.Vertical -> Offset(x = available.x, y = 0f)
                DragAxis.Horizontal ->
                    delegate?.onPostScroll(consumed, available, source) ?: Offset.Zero
                null -> {
                    if (abs(consumed.y) > abs(consumed.x)) Offset(x = available.x, y = 0f)
                    else Offset.Zero
                }
            }
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            val axis = locked ?: if (abs(available.y) >= abs(available.x)) {
                DragAxis.Vertical
            } else {
                DragAxis.Horizontal
            }
            return when (axis) {
                DragAxis.Vertical -> Velocity(x = available.x, y = 0f)
                DragAxis.Horizontal -> delegate?.onPreFling(available) ?: Velocity.Zero
            }
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            val wasVertical = locked == DragAxis.Vertical ||
                (locked == null && abs(consumed.y) >= abs(consumed.x))
            val leftover = if (wasVertical) {
                Velocity(x = available.x, y = 0f)
            } else {
                delegate?.onPostFling(consumed, available) ?: Velocity.Zero
            }
            reset()
            return leftover
        }
    }
}

@Composable
fun rememberPagerScrollGate(pagerState: PagerState): PagerScrollGate {
    // Pure axis-lock gate (no composable PagerDefaults call inside remember).
    // Foundation's default nested connection is optional; swallowing residual X
    // + disabling pager user-scroll on vertical lock is what stops tab thrash.
    return remember(pagerState) {
        PagerScrollGate(delegate = null)
    }
}

/** Child-side axis lock on verticalScroll / LazyColumn roots (no pager delegate). */
fun Modifier.verticalScrollFirst(): Modifier = composed {
    val connection = remember { PagerScrollGate(delegate = null).connection }
    this.nestedScroll(connection)
}
