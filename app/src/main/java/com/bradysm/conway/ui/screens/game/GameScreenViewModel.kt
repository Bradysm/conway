package com.bradysm.conway.ui.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bradysm.conway.domain.engine.GameEngine
import com.bradysm.conway.domain.state.ConwayGameStore
import com.bradysm.conway.domain.state.modles.GameCell
import com.bradysm.conway.ui.screens.game.extensions.toScreenState
import com.bradysm.conway.ui.screens.game.models.ComposedGameState
import com.bradysm.conway.ui.screens.game.models.ScreenUIModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * Core game ViewModel which provides the abstraction layer between domain models and logic and the
 * UI. This ViewModel is built on the principles of UDF, where events emitted from the UI are
 * handled and new state is emitted back down to the UI through the [screenState] flow.
 *
 * All UI changes for this screen should continue to follow the UDF principles.
 */
class GameScreenViewModel: ViewModel() {
    // TODO: Add Hilt/Koin to inject these into the ViewModel, this is terrible
    // there is no reason that the VM knows how to construct these
    private val gameRepository = ConwayGameStore()
    private val gameEngine = GameEngine(viewModelScope) { gameRepository.executeCycle() }

    // Convert the game model state to a screen state that we can consume
    val screenState: StateFlow<ScreenUIModel> = combine(
        gameEngine.state,
        gameRepository.state
    ) { engineModel, gameModel -> ComposedGameState(engineModel, gameModel) }
        .toScreenState()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ScreenUIModel())

    /**
     * Emits event intents to the ViewModel to process. These intent events are events that stem
     * from UI interactions events from the UI. This is the core entrypoint where UI events
     * are handled within the application. The ViewModel can choose to handle the event on its
     * own or delegate the intent handling to another class.
     *
     * If the intent changed the state of the game, a new [ScreenUIModel] will be emitted from
     * the [screenState] flow which the UI should collect to stay aware.
     */
    fun emit(event: GameScreenEvent) {
        when (event) {
            is GameScreenEvent.TogglePosition -> {
                gameRepository.toggleCellLiveliness(event.cell)
            }
            is GameScreenEvent.ToggleEngineState -> {
                gameEngine.toggleEngine()
            }
            is GameScreenEvent.ResetGame -> {
                gameRepository.reset()
                gameEngine.stop()
            }
            is GameScreenEvent.ToggleMenuExpansion -> {
                gameRepository.toggleMenuVisibility()
            }
        }
    }
}

/**
 * Sealed class containing the intent events that can be emitted from the Game Screen
 * to the ViewModel. All intent actions that need to manipulate game data from a UI
 * interaction should be added to this sealed class.
 */
sealed class GameScreenEvent {
    /**
     * Event triggered when a game cell position is toggled by the user.
     *
     * @property cell The [GameCell] that was toggled, containing position and state information.
     */
    data class TogglePosition(val cell: GameCell): GameScreenEvent()

    /**
     * Event triggered when the user requests to toggle the game engine's running state.
     * This will either start or stop the game simulation depending on its current state.
     */
    data object ToggleEngineState: GameScreenEvent()

    /**
     * Event triggered when the user requests to reset the game to its initial state.
     * This will clear all active cells and reset any game progress.
     */
    data object ResetGame: GameScreenEvent()

    /**
     * Event triggered when the user toggles the game menu's expansion state.
     * This will either expand or collapse the menu UI component.
     */
    data object ToggleMenuExpansion: GameScreenEvent()
}
