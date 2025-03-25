package com.bradysm.conway.domain.state.modles

/**
 * Represents a cell within the Game of life. This contains the Row and Col position of the cell
 * within the map.
 */
data class GameCell(
    val row: Int,
    val col: Int
)
