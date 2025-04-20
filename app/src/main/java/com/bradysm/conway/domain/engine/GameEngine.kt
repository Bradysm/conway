package com.bradysm.conway.domain.engine

import com.bradysm.conway.domain.engine.models.EngineState
import com.bradysm.conway.domain.engine.models.GameEngineModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Engine which maintains a job that executes a "tic" function every "x" milliseconds when running.
 * The engine will maintain the tic mechanism and tic execution on a background thread to ensure
 * UI performance is maintained.
 *
 * The engine can be in two states, RUNNING or STOPPED. To start the engine ticking, send an action
 * to start the engine. Once the engine is RUNNING, the tic function will begin being executed
 * every engine cycle interval.
 */
class GameEngine(
    private val scope: CoroutineScope,
    private val onEngineTick: () -> Unit
) {
    // engine job
    private var engineJob: Job? = null

    private val _state = MutableStateFlow(GameEngineModel())
    val state: StateFlow<GameEngineModel> = _state


    fun toggleEngine() {
        when (_state.value.engineState) {
            EngineState.RUNNING -> stop()
            EngineState.STOPPED -> start()
        }
    }

    /**
     * Starts the game engine, this will kick off a clock which will emit a new state every
     * x seconds
     */
    fun start() {
        _state.update { currEngineModel ->
            // start the engine job and then
            startEngineJob()
            currEngineModel.copy(engineState = EngineState.RUNNING)
        }
    }

    /**
     * stops the game engine. Once this is called, engine tics will no longer be executed
     * until the engine is started again
     */
    fun stop() {
        _state.update { currEngineModel ->
            // start the engine job and then
            stopEngineJob()
            currEngineModel.copy(engineState = EngineState.STOPPED)
        }
    }

    // TODO: likely we will need to synchronize these
    private fun startEngineJob() {
        // If there is an active engine job, leave it running and return
        if (engineJob != null) {
            return
        }

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

    companion object {
        private const val ENGINE_UPDATE_BACKOFF_MS = 250L
    }
}
