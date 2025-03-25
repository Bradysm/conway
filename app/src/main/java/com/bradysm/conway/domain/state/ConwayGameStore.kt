package com.bradysm.conway.domain.state

import com.bradysm.conway.domain.state.modles.ConwayGameModel
import com.bradysm.conway.domain.state.modles.GameCell
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Store containing the state of the current game. This maintains all state and provides
 * APIs to mutate the state of the game.
 *
 * The current state of the game can be accessed by the [state] flow which emits a new value whenever
 * the game state changes.
 */
class ConwayGameStore {
    private val _state = MutableStateFlow(ConwayGameModel())
    val state: StateFlow<ConwayGameModel> = _state

    // region mutation APIs
    fun executeCycle() {
        _state.update { executeGenerationCycle(it) }
    }

    fun reset() {
        _state.update { ConwayGameModel() }
    }

    fun toggleCellLiveliness(cell: GameCell) {
        _state.update { currGameState ->

            // if we already have the position, then remove it otherwise add it
            val updatedAlivePosSet = currGameState.alivePosSet.toMutableSet().apply {
                if (contains(cell)) remove(cell) else add(cell)
            }
            currGameState.copy(alivePosSet = updatedAlivePosSet)
        }
    }

    fun toggleMenuVisibility() {
        _state.update { currGameState ->
            val updatedMenuVisibility = !currGameState.isMenuExpanded
            currGameState.copy(isMenuExpanded = updatedMenuVisibility)
        }
    }
    // endregion

    // region helper functions
    private fun executeGenerationCycle(currModel: ConwayGameModel): ConwayGameModel {
        val updatedAliveCellPosSet = buildSet {
            addAll(generateSurvivingGameCells(currModel.alivePosSet))
            addAll(generateRepopulatedGameCells(currModel.alivePosSet))
        }
        val updatedGeneration = currModel.generation + 1
        return currModel.copy(alivePosSet = updatedAliveCellPosSet, generation = updatedGeneration)
    }

    private fun generateSurvivingGameCells(alivePosSet: Set<GameCell>): List<GameCell> {
        return alivePosSet.filter { isAliveForNextGeneration(it, alivePosSet) }
    }

    private fun generateRepopulatedGameCells(alivePosSet: Set<GameCell>): List<GameCell> {
        return alivePosSet
            .generateSignificantDeadCells()
            .filter { willBeRepopulated(it, alivePosSet) }
    }

    private fun isAliveForNextGeneration(currPosition: GameCell, alivePosSet: Set<GameCell>) : Boolean {
        // stay alive if the number of alive neighbors is in this range
        return alivePosSet.aliveNeighbors(currPosition) in (2..3)
    }

    private fun willBeRepopulated(currPosition: GameCell, alivePosSet: Set<GameCell>): Boolean {
        return alivePosSet.aliveNeighbors(currPosition) == 3
    }

    /**
     * Generates significant dead cell locations that surround Live cells. This should be used
     * to look at only significant dead cells for re-population
     */
    private fun Set<GameCell>.generateSignificantDeadCells(): List<GameCell> {
        return flatMap { aliveCell ->
            NEIGHBOR_CELL_DELTAS.map { (dr, dc) ->
                GameCell(aliveCell.row + dr, aliveCell.col + dc)
            }
        }.filter { !contains(it) }
    }

    /**
     * @return the number of alive neighbors relative to the provided cell
     * @receiver Set containing alive GameCells
     */
    private fun Set<GameCell>.aliveNeighbors(cell: GameCell): Int {
        return NEIGHBOR_CELL_DELTAS
            // map to the full position
            .map { (dr, dc) -> GameCell(cell.row + dr, cell.col + dc) }
            // check if the alive set contains it
            .filter { contains(it) }
            .size
    }
    // endregion

    companion object {
        private val NEIGHBOR_CELL_DELTAS = buildList {
            add(Pair(-1, 0))
            add(Pair(-1, -1))
            add(Pair(-1, 1))
            add(Pair(1, 1))
            add(Pair(1, 0))
            add(Pair(1, -1))
            add(Pair(0, -1))
            add(Pair(0, 1))
        }
    }
}