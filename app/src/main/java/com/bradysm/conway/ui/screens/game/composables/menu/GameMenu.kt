package com.bradysm.conway.ui.screens.game.composables.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.bradysm.conway.ui.screens.game.GameScreenViewModel
import com.bradysm.conway.ui.screens.game.ScreenEvent
import com.bradysm.conway.ui.screens.game.aliases.ScreenEventEmitter
import com.bradysm.conway.ui.screens.game.composables.menu.components.GameInfo
import com.bradysm.conway.ui.screens.game.composables.menu.components.MenuWindowHeader
import com.bradysm.conway.ui.screens.game.composables.menu.components.TerminalButton
import com.bradysm.conway.ui.screens.game.models.GameMenuUIModel
import com.bradysm.conway.ui.screens.game.models.MenuButtonType
import com.bradysm.conway.ui.theme.TerminalBlack

/**
 * Composable which draws the Game Menu in the form of a terminal styling
 *
 * This has two different states, expanded and collapsed. Based on the two states, different
 * information is provided.
 */
@Composable
fun GameMenu(
    menuModel: GameMenuUIModel,
    modifier: Modifier = Modifier,
    emitEvent: ScreenEventEmitter
) {
    // Create an interaction source to detect when the button is pressed
    val onMenuButtonClick: (MenuButtonType) -> Unit = { menuButtonType ->
        when (menuButtonType) {
            MenuButtonType.START, MenuButtonType.STOP -> emitEvent(ScreenEvent.ToggleEngineState)
            MenuButtonType.RESET -> emitEvent(ScreenEvent.ResetGame)
        }
    }

    // Use a surface to prevent pointer inputs from passing through
    // to the game map
    Surface(
        modifier = modifier.clip(RoundedCornerShape(4.dp)),
        color = TerminalBlack
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            // Terminal window header
            MenuWindowHeader(
                menuModel.isExpanded,
                menuModel.primaryButtonModel,
                emitEvent= emitEvent
            )
            AnimatedVisibility(menuModel.isExpanded) {
                ExpandedMenuContent(menuModel, onMenuButtonClick)
            }
        }
    }
}

/**
 * Expanded menu content which contains buttons to reset the game, start/stop, and information
 * on the generation count of the current game that is running.
 */
@Composable
private fun ExpandedMenuContent(
    menuModel: GameMenuUIModel,
    onMenuButtonClick: (MenuButtonType) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier.padding(vertical = 8.dp)) {
        GameInfo(menuModel)

        with(menuModel.primaryButtonModel) {
            TerminalButton(
                type.buttonText()
            ) { onMenuButtonClick(type) }
        }

        with(menuModel.resetButtonModel) {
            TerminalButton(
                type.buttonText(),
            ) { onMenuButtonClick(type) }
        }
    }
}


private fun MenuButtonType.buttonText(): String {
    // TODO: Move this to a res file
    return when (this) {
        is MenuButtonType.STOP -> "./stop-engine.sh"
        is MenuButtonType.START -> "./start-engine.sh"
        is MenuButtonType.RESET -> "./reset.sh"
    }
}