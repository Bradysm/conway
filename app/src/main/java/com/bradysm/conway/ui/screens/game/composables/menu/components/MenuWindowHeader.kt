package com.bradysm.conway.ui.screens.game.composables.menu.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bradysm.conway.ui.screens.game.GameScreenEvent
import com.bradysm.conway.ui.screens.game.aliases.GameScreenEventEmitter
import com.bradysm.conway.ui.screens.game.models.MenuButtonType
import com.bradysm.conway.ui.screens.game.models.MenuButtonUIModel
import com.bradysm.conway.ui.theme.MaximizeButtonGreen
import com.bradysm.conway.ui.theme.MinimizeButtonYellow
import com.bradysm.conway.ui.theme.TerminalHeader
import com.bradysm.conway.ui.theme.TerminalWhite

@Composable
fun MenuWindowHeader(
    isMenuExpanded: Boolean,
    primaryActionButtonModel: MenuButtonUIModel,
    emitEvent: GameScreenEventEmitter,
    modifier: Modifier = Modifier,
    ) {
    Box(
        modifier = modifier
            .height(32.dp)
            .background(TerminalHeader)
            .fillMaxWidth()
            .padding(start = 8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            Modifier
                .size(16.dp)
                .clip(CircleShape)
                .background(if (isMenuExpanded) MinimizeButtonYellow else MaximizeButtonGreen)
                .clickable {
                    emitEvent(GameScreenEvent.ToggleMenuExpansion)
                }
        )

        Text(
            text = "Conway Terminal", // TODO: Move this to a res file
            color = TerminalWhite.copy(alpha = 0.7f),
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.align(Alignment.Center)
        )


        AnimatedVisibility(!isMenuExpanded, modifier = Modifier.align(Alignment.CenterEnd),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            IconButton(onClick = {
                emitEvent(GameScreenEvent.ToggleEngineState)
            }, modifier = Modifier.wrapContentSize()) {
                val iconVector = when(primaryActionButtonModel.type) {
                    is MenuButtonType.START -> Icons.Filled.PlayArrow
                    else -> Icons.Filled.Pause
                }
                Icon(imageVector = iconVector, contentDescription = null, tint = TerminalWhite)
            }
        }
    }
}