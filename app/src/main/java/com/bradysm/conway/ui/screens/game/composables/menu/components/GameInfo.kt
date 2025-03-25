package com.bradysm.conway.ui.screens.game.composables.menu.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bradysm.conway.ui.screens.game.models.GameMenuUIModel
import com.bradysm.conway.ui.theme.TerminalGreen

@Composable
internal fun GameInfo(menuModel: GameMenuUIModel, modifier: Modifier = Modifier) {
    // Animation for the blinking cursor
    val formattedGenerationString = menuModel.generation.toString().padStart(6, '0')

    // Row containing generation info followed by a blinking cursor
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(top = 8.dp).padding(horizontal = 12.dp)
    ) {
        Text(
            text = "GEN: $formattedGenerationString",
            color = TerminalGreen,
            fontSize = 16.sp,
            fontFamily = FontFamily.Monospace
        )
        BlinkingCursor()
    }
}

@Composable
private fun BlinkingCursor(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "cursorBlink")
    val cursorAlpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorBlinkAnimation"
    )

    // TODO: Move all of this to res files
    Box(
        modifier = modifier
            .padding(start = 2.dp)
            .width(10.dp)
            .height(18.dp)
            .graphicsLayer {
                alpha = cursorAlpha
            }
            .background(TerminalGreen)
    )
}