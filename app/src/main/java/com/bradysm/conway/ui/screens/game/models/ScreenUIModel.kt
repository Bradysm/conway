package com.bradysm.conway.ui.screens.game.models

import androidx.compose.runtime.Stable


@Stable
data class ScreenUIModel(
    val gameMap: List<List<Boolean>> = listOf(),
    val menuModel: GameMenuUIModel = GameMenuUIModel()
)
