package com.bradysm.conway.domain.engine.models


/**
 * Enumeration defining the different states of an Engine
 */
enum class EngineState {
    /**
     * The engine is running. i.e, it's processing updates every "tic" through an active job
     */
    RUNNING,

    /**
     * The engine is not running and there is no active processing.
     */
    STOPPED
}