package com.bradysm.conway.ui.screens.game.models

import androidx.compose.runtime.Stable

/**
 * Model defining the data representing the UI of the menu within the game
 */
@Stable
data class GameMenuUIModel(
    val isExpanded: Boolean = true,
    val generation: Int = 0,
    // TODO: We can likely eventually make this a list of menu items
    val primaryButtonModel: MenuButtonUIModel = MenuButtonUIModel(MenuButtonType.START),
    val resetButtonModel: MenuButtonUIModel = MenuButtonUIModel(MenuButtonType.RESET)
)
