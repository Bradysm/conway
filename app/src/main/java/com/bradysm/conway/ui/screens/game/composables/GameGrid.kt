package com.bradysm.conway.ui.screens.game.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bradysm.conway.domain.state.modles.GameCell
import com.bradysm.conway.ui.screens.game.GameScreenEvent
import com.bradysm.conway.ui.screens.game.aliases.GameScreenEventEmitter
import com.bradysm.conway.ui.theme.TerminalGreen


/**
 * Draws a Game Grid on a Canvas, providing support for cell clicks and returning the GameCell
 * for each click that is acted upon within the grid.
 *
 * This grid is performant by drawing directly to the canvas and only having one Composable being
 * returned. Instead of using Row and Col, we directly we use a Canvas, and draw within that canvas.
 */
@Composable
fun GameGrid(
    gameMap: List<List<Boolean>>,
    modifier: Modifier = Modifier,
    cellSize: Dp = 24.dp,
    emitEvent: GameScreenEventEmitter,
) {
    val density = LocalDensity.current
    val cellSizePx = with(density) { cellSize.toPx() }
    val onCellClicked: (GameCell) -> Unit =  { gameCell ->
        emitEvent(GameScreenEvent.TogglePosition(gameCell))
    }

    val gameCanvasModifier =  modifier
        .size(cellSize * gameMap.size)
        .detectCellClick(cellSizePx, onCellClicked)

    Canvas(gameCanvasModifier) {
        gameMap.forEachIndexed { rowIndex, cellRow ->
            cellRow.forEachIndexed { colIndex, isAlive ->
                drawCell(isAlive, colIndex, rowIndex)
            }
        }
    }
}


private fun Modifier.detectCellClick(cellSizePx: Float, onCellClicked: (GameCell) -> Unit): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures (
            onTap = { offset ->
            // calculate the row and column by dividing by the size of a cell
            // this will give the digit representing the cell offset
            val row = (offset.y / cellSizePx).toInt()
            val col = (offset.x / cellSizePx).toInt()
            onCellClicked(GameCell(row, col))
        })
    }
}

private fun DrawScope.drawCell(
    isAlive: Boolean,
    colIndex:Int,
    rowIndex:Int,
    sizePx: Float = 24.dp.toPx()
) {
    val cellColor = if (isAlive) Color.Green else Color.Black
    val cellSize = Size(sizePx, sizePx)
    val topLeftOffset = Offset(colIndex * sizePx, rowIndex * sizePx)

    // Square cell
    drawRect(
        color = cellColor,
        topLeft = topLeftOffset,
        size = cellSize,
    )
    // Cell borders
    drawRect(
        color = TerminalGreen,
        topLeft = topLeftOffset,
        size = cellSize,
        style = Stroke(width = 1f)
    )
}

