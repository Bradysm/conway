package com.bradysm.conway.ui.screens.game.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bradysm.conway.ui.screens.game.aliases.GameScreenEventEmitter
import com.bradysm.conway.ui.screens.game.composables.menu.GameMenu
import com.bradysm.conway.ui.screens.game.models.ScreenUIModel

/**
 * This is the main composable screen which is rendered when entering the application
 *
 * This contains the game of life by composing the main game and menu. Think of this as the
 * core entry point into the application. This should be rendered by the MainActivity.
 */
@Composable
fun GameScreenRoot(screenState: ScreenUIModel, emitEvent: GameScreenEventEmitter, modifier: Modifier = Modifier) {
    // fill the entire screen
    Box(modifier.fillMaxSize()) {
        GameGrid(screenState.gameMap, Modifier.align(Alignment.Center), emitEvent = emitEvent)

        // Move this to it's own composable
        GameMenu(
            screenState.menuModel,
            modifier = Modifier.align(Alignment.BottomCenter).padding(32.dp),
            emitEvent,
        )
    }
}
