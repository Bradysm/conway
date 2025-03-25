package com.bradysm.conway.ui.screens.game.composables.menu.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.bradysm.conway.ui.theme.TerminalGreen
import com.bradysm.conway.ui.theme.TerminalWhite

@Composable
internal fun TerminalButton(
    text: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Terminal content area
    Box(
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(color = TerminalGreen.copy(alpha = 0.2f)),
                enabled = true,
                onClick = onClick
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            // Command prompt symbol

            Text(
                text = "$",
                color = TerminalWhite,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Command text
            Text(
                text = text,
                color = TerminalWhite,
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        // Command underline
        Box(
            modifier = Modifier
                .padding(top = 38.dp, start = 24.dp)
                .width(text.length * 9.5f.dp) // Approximate width based on character count
                .height(2.dp)
                .background(TerminalGreen.copy(alpha = 0.7f))
        )
    }
}