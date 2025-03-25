package com.bradysm.conway.domain.engine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameEngine(
    private val scope: CoroutineScope,
    private val onEngineTick: () -> Unit
) {
    private val _state = MutableStateFlow(GameEngineModel())
    val state: StateFlow<GameEngineModel> = _state

    // engine job
    private var engineJob: Job? = null

    fun process(action: GameEngineAction) {
        when(action) {
            is GameEngineAction.StartEngine -> start()
            is GameEngineAction.StopEngine -> stop()
            is GameEngineAction.ToggleEngineState -> toggleEngine()
        }
    }

    private fun toggleEngine() {
        when (_state.value.engineState) {
            GameEngineState.EXECUTING -> stop()
            GameEngineState.STOPPED -> start()
        }
    }

    /**
     * Starts the game engine, this will kick off a clock which will emit a new state every
     * x seconds
     */
    private fun start() {
        _state.update { currEngineModel ->
            // start the engine job and then
            startEngineJob()
            currEngineModel.copy(engineState = GameEngineState.EXECUTING)
        }
    }

    /**
     * stops the game engine
     */
    private fun stop() {
        _state.update { currEngineModel ->
            // start the engine job and then
            stopEngineJob()
            currEngineModel.copy(engineState = GameEngineState.STOPPED)
        }
    }

    // TODO: LIkely we will need to synchronize these
    private fun startEngineJob() {
        engineJob = scope.launch(Dispatchers.Default) {
            while (isActive) {
                onEngineTick()
                delay(ENGINE_UPDATE_BACKOFF_MS)
            }
        }
    }

    private fun stopEngineJob() {
        engineJob?.cancel()
        engineJob = null
    }


    // region ConwayGameUpdater

    // endregion

    sealed class GameEngineAction {
        data object StartEngine: GameEngineAction()
        data object StopEngine: GameEngineAction()
        data object ToggleEngineState: GameEngineAction()
    }

    companion object {
        private const val ENGINE_UPDATE_BACKOFF_MS = 250L
    }
}

data class GameEngineModel(
    val engineState: GameEngineState = GameEngineState.STOPPED,
)

enum class GameEngineState {
    EXECUTING,
    STOPPED
}