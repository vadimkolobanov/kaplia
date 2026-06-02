package com.kaplia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.kaplia.ui.model.Pronoun
import com.kaplia.ui.screen.HomeScreen
import com.kaplia.ui.screen.OnboardingScreen
import com.kaplia.ui.screen.PetState
import com.kaplia.ui.theme.KapliaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KapliaTheme {
                var petState by remember { mutableStateOf<PetState?>(null) }

                val current = petState
                if (current == null) {
                    OnboardingScreen(
                        onComplete = { name, pronoun ->
                            petState = PetState(name = name, pronoun = pronoun)
                        },
                    )
                } else {
                    HomeScreen(pet = current)
                }
            }
        }
    }
}
