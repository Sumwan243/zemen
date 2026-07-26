package com.surafel.zemen.ui.converter

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.list.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs

/**
 * Single drum scroller column with snap physics and gradient fading.
 * Each item is 40dp tall. Three items visible above and below center.
 */
@Composable
fun DrumScroller(
    items: List<Int>,
    selectedIndex: Int,
    isReadOnly: Boolean,
    accentColor: Color,
    displayText: (Int) -> String,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val itemHeightDp = 40.dp
    val itemHeightPx = with(androidx.compose.ui.platform.LocalDensity.current) { itemHeightDp.toPx() }

    val listState = rememberLazyListState()
    val haptic = LocalHapticFeedback.current

    // Auto-scroll to selected index on first composition or when it changes
    LaunchedEffect(selectedIndex) {
        if (selectedIndex in items.indices) {
            listState.animateScrollToItem(selectedIndex)
        }
    }

    // Snap detection: detect when scrolling stops
    LaunchedEffect(listState.isScrollInProgress) {
        if (!listState.isScrollInProgress && !isReadOnly) {
            val visibleItems = listState.layoutInfo.visibleItemsInfo
            if (visibleItems.isNotEmpty()) {
                val centerOffset = listState.layoutInfo.viewportEndOffset / 2 - itemHeightPx / 2
                val closest = visibleItems.minByOrNull {
                    abs((it.offset + it.size / 2) - centerOffset)
                }
                closest?.let {
                    val index = it.index
                    if (index in items.indices && index != selectedIndex) {
                        onSelected(index)
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    }
                }
            }
        }
    }

    Box(
        modifier = modifier
            .height(itemHeightDp * 7) // Show 7 items
            .clip(RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Selector band (highlighted center)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp)
                .height(itemHeightDp)
                .align(Alignment.Center)
                .clip(RoundedCornerShape(6.dp))
                .then(
                    if (isReadOnly) {
                        Modifier
                    } else {
                        Modifier
                    }
                )
        )

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top padding items to allow centering
            items(3) {
                Box(modifier = Modifier.height(itemHeightDp))
            }

            items(items) { item ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeightDp)
                ) {
                    Text(
                        text = displayText(item),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            textAlign = TextAlign.Center
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Bottom padding items
            items(3) {
                Box(modifier = Modifier.height(itemHeightDp))
            }
        }

        // Gradient overlay top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * 3)
                .align(Alignment.TopCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            Color.Transparent
                        )
                    )
                )
        )

        // Gradient overlay bottom
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * 3)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        )
    }
}
