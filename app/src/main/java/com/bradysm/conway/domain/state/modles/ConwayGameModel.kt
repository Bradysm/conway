package com.bradysm.conway.domain.state.modles

/**
 * Data model representing the current state of the Conway Game of Life.
 * This is the model that is used within the Store and gives the full game
 * state.
 */
data class ConwayGameModel(
    val generation:Int = 0,
    val alivePosSet: Set<GameCell> = setOf(),
    val isMenuExpanded: Boolean = true
) {
    companion object {
        const val MAP_HEIGHT = 50
        const val MAP_WIDTH = 50
    }
}