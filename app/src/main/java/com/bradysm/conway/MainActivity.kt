package com.bradysm.conway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.bradysm.conway.ui.screens.game.composables.MainGameScreen
import com.bradysm.conway.ui.screens.game.GameScreenViewModel
import com.bradysm.conway.ui.theme.ConwayTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<GameScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConwayTheme {
                //TODO: turn this into collect as state with lifecycle
                val screenState by viewModel.screenState.collectAsState()
                MainGameScreen(
                    screenState,
                    remember { viewModel::emit }
                )
            }
        }
    }
}
