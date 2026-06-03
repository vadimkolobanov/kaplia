package com.kaplia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.kaplia.ui.screen.HomeScreen
import com.kaplia.ui.screen.OnboardingScreen
import com.kaplia.ui.theme.DeepSpace
import com.kaplia.ui.theme.KapliaTheme
import com.kaplia.ui.viewmodel.PetUiState
import com.kaplia.ui.viewmodel.PetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KapliaTheme {
                val vm: PetViewModel = hiltViewModel()
                val uiState by vm.state.collectAsState()

                when (val state = uiState) {
                    PetUiState.Loading ->
                        Box(Modifier.fillMaxSize().background(DeepSpace))

                    PetUiState.NoPet ->
                        OnboardingScreen(
                            onComplete = { name, pronoun -> vm.startPet(name, pronoun) },
                        )

                    is PetUiState.Alive ->
                        HomeScreen(
                            pet = state.pet,
                            onFeed = vm::feed,
                            onPlay = vm::play,
                            onRest = vm::rest,
                        )
                }
            }
        }
    }
}
