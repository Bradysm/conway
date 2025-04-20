package com.bradysm.conway.ui.screens.game.extensions

import com.bradysm.conway.domain.engine.EngineState
import com.bradysm.conway.ui.screens.game.models.ComposedGameState
import com.bradysm.conway.ui.screens.game.models.GameMenuUIModel
import com.bradysm.conway.ui.screens.game.models.MenuButtonUIModel
import com.bradysm.conway.ui.screens.game.models.MenuButtonType
import com.bradysm.conway.ui.screens.game.models.ScreenUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Flow extension function which converts a [ComposedGameState] flow to a [ScreenUIModel]
 * flow. This should be used to convert the domain layer to a UI representation which
 * can be consumed. In a way, this is a "uncle bob usecase"
 */
fun Flow<ComposedGameState>.toScreenState(): Flow<ScreenUIModel> {
    return map { model ->
        ScreenUIModel(
            gameMap = model.gameModel.toScreenGrid(),
            menuModel = model.toGameMenuModel(),
        )
    }
}

/**
 * Converts [ComposedGameState] to a [GameMenuUIModel] which can be drawn on the screen
 * This model contains all information needed to render the game menu.
 */
fun ComposedGameState.toGameMenuModel(): GameMenuUIModel {
    val primaryButtonType = when(engineModel.engineState) {
        EngineState.EXECUTING -> MenuButtonType.STOP
        EngineState.STOPPED -> MenuButtonType.START
    }
    val primaryButtonModel = MenuButtonUIModel(primaryButtonType)
    return GameMenuUIModel(
        isExpanded = gameModel.isMenuExpanded,
        generation = gameModel.generation,
        primaryButtonModel = primaryButtonModel,
        resetButtonModel = MenuButtonUIModel(MenuButtonType.RESET),
    )
}