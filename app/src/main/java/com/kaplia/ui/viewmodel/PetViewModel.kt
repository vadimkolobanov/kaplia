package com.kaplia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaplia.domain.logic.PetEngine
import com.kaplia.domain.model.Pet
import com.kaplia.domain.model.PetGenome
import com.kaplia.ui.model.PetState
import com.kaplia.ui.model.Pronoun
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import kotlin.random.Random

private const val TICK_INTERVAL_MS = 3_000L

class PetViewModel : ViewModel() {
    private val _state = MutableStateFlow<PetState?>(null)
    val state: StateFlow<PetState?> = _state.asStateFlow()

    private var pet: Pet? = null
    private var tickJob: Job? = null

    fun startPet(
        name: String,
        pronoun: Pronoun,
    ) {
        val genome = PetGenome(
            hungerRate = 0.8f + Random.nextFloat() * 0.4f,
            energyRate = 0.8f + Random.nextFloat() * 0.4f,
            moodRate = 0.8f + Random.nextFloat() * 0.4f,
            vitality = 0.8f + Random.nextFloat() * 0.4f,
        )
        pet = Pet(name = name, pronoun = pronoun, genome = genome)
        pushState()
        startTicking()
    }

    fun feed() = applyAction(PetEngine::feed)

    fun play() = applyAction(PetEngine::play)

    fun rest() = applyAction(PetEngine::rest)

    fun advanceDay() = applyAction(PetEngine::advanceDay)

    private fun applyAction(action: (Pet) -> Pet) {
        pet = pet?.let(action)
        pushState()
        if (pet?.isDead == true) tickJob?.cancel()
    }

    private fun pushState() {
        _state.value = pet?.toUiState()
    }

    private fun startTicking() {
        tickJob?.cancel()
        tickJob = viewModelScope.launch {
            while (true) {
                delay(TICK_INTERVAL_MS)
                applyAction(PetEngine::tick)
                if (pet?.isDead == true) break
            }
        }
    }

    override fun onCleared() {
        tickJob?.cancel()
        super.onCleared()
    }
}

private fun Pet.toUiState() =
    PetState(
        name = name,
        pronoun = pronoun,
        day = day,
        hunger = hunger,
        energy = energy,
        mood = mood,
        health = health,
        stage = stage,
        isDead = isDead,
    )
