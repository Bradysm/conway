package com.bradysm.conway.ui.screens.game.extensions

import com.bradysm.conway.domain.state.modles.ConwayGameModel
import com.bradysm.conway.domain.state.modles.ConwayGameModel.Companion.MAP_HEIGHT
import com.bradysm.conway.domain.state.modles.ConwayGameModel.Companion.MAP_WIDTH

/**
 * Extension function to convert a Game Model to a model which can be presented
 * within the UI. The result of this function is effectively a "map" which can
 * be rendered onto the screen.
 */
fun ConwayGameModel.toScreenGrid(): List<List<Boolean>> {
    return List(MAP_HEIGHT) { MutableList(MAP_WIDTH) { false } }.apply {
        alivePosSet.filter { gameCell ->
            // check to see if the alive cell is within the current visible map
            // This is only necessary since we define a small map which is presented
            // when we add panning and zooming, we will need to change this, but
            // for now this is solid.
            val isRowVisible = (gameCell.row in 0..< MAP_HEIGHT)
            val isColVisible = (gameCell.col in 0..< MAP_WIDTH)
            isRowVisible && isColVisible
        }.forEach { gameCell -> this[gameCell.row][gameCell.col] = true }
    }
}