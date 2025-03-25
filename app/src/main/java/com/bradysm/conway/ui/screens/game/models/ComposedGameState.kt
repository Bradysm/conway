package com.bradysm.conway.ui.screens.game.models

import com.bradysm.conway.domain.engine.GameEngineModel
import com.bradysm.conway.domain.state.modles.ConwayGameModel

/**
 * Data class representing a composition of state models which represent the game.
 * This model intentionally collects all state models and composes them into a single
 * model which is used within the ViewModel to Flow a single event. This composed
 * state is then used to build a UI model which is then collected by the Composable.
 */
data class ComposedGameState(
    val engineModel: GameEngineModel,
    val gameModel: ConwayGameModel
)
