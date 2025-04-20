package com.bradysm.conway.ui.screens.game.aliases

import com.bradysm.conway.ui.screens.game.GameScreenEvent

/**
 * Type alias representing a callback which emits a game screen event
 *
 * This callback will eventually bubble up to the ViewModel as the main entrypoint
 * for the
 */
typealias GameScreenEventEmitter = (GameScreenEvent) -> Unit