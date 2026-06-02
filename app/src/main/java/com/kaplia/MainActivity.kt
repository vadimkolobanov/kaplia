package com.kaplia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kaplia.ui.screen.HomeScreen
import com.kaplia.ui.screen.OnboardingScreen
import com.kaplia.ui.theme.KapliaTheme
import com.kaplia.ui.viewmodel.PetViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KapliaTheme {
                val vm: PetViewModel = viewModel()
                val petState by vm.state.collectAsState()

                val pet = petState
                if (pet == null) {
                    OnboardingScreen(
                        onComplete = { name, pronoun ->
                            vm.startPet(name, pronoun)
                        },
                    )
                } else {
                    HomeScreen(
                        pet = pet,
                        onFeed = vm::feed,
                        onPlay = vm::play,
                        onRest = vm::rest,
                        onNextDay = vm::advanceDay,
                    )
                }
            }
        }
    }
}
