package com.bradysm.conway.domain.engine.models


/**
 * Model defining the state of the [GameEngine]
 *
 * @property engineState - Current state of the engine
 */
data class GameEngineModel(
    val engineState: EngineState = EngineState.STOPPED,
)