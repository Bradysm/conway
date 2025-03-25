package com.bradysm.conway.ui.screens.game.models

import androidx.compose.runtime.Stable


/**
 * Model defining a button within the Game Menu
 */
@Stable
data class MenuButtonUIModel(
    val type: MenuButtonType
)

/**
 * Sealed class representing a type of button within a menu
 *
 * Potentially we could get rid of MenuButtonModel and just use this
 * but I'm not sure at this point so we'll leave it
 */
sealed class MenuButtonType {
    data object STOP: MenuButtonType()
    data object START: MenuButtonType()
    data object RESET: MenuButtonType()
}
