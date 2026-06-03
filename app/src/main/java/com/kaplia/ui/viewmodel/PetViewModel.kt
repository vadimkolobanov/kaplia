package com.kaplia.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kaplia.data.repository.PetRepository
import com.kaplia.domain.logic.PetEngine
import com.kaplia.domain.model.Pet
import com.kaplia.domain.model.PetGenome
import com.kaplia.ui.model.PetState
import com.kaplia.ui.model.Pronoun
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

private const val TICK_INTERVAL_MS = 5_000L
private const val GENOME_BASE = 0.8f
private const val GENOME_SPREAD = 0.4f

@HiltViewModel
class PetViewModel
    @Inject
    constructor(
        private val repository: PetRepository,
    ) : ViewModel() {
        private val _state = MutableStateFlow<PetUiState>(PetUiState.Loading)
        val state: StateFlow<PetUiState> = _state.asStateFlow()

        private var pet: Pet? = null
        private var tickJob: Job? = null

        init {
            viewModelScope.launch {
                val loaded = repository.loadPet()
                if (loaded == null) {
                    _state.value = PetUiState.NoPet
                } else {
                    pet = PetEngine.advanceTo(loaded, now())
                    persistAndPublish()
                    startTicking()
                }
            }
        }

        fun startPet(
            name: String,
            pronoun: Pronoun,
        ) {
            val genome = PetGenome(
                hungerRate = GENOME_BASE + Random.nextFloat() * GENOME_SPREAD,
                energyRate = GENOME_BASE + Random.nextFloat() * GENOME_SPREAD,
                moodRate = GENOME_BASE + Random.nextFloat() * GENOME_SPREAD,
                vitality = GENOME_BASE + Random.nextFloat() * GENOME_SPREAD,
            )
            pet = PetEngine.newPet(name, pronoun, genome, now())
            persistAndPublish()
            startTicking()
        }

        fun feed() = action(PetEngine::feed)

        fun play() = action(PetEngine::play)

        fun rest() = action(PetEngine::rest)

        private fun action(transform: (Pet) -> Pet) {
            val current = pet ?: return
            pet = transform(PetEngine.advanceTo(current, now()))
            persistAndPublish()
            if (pet?.isDead == true) tickJob?.cancel()
        }

        private fun startTicking() {
            tickJob?.cancel()
            tickJob = viewModelScope.launch {
                while (true) {
                    delay(TICK_INTERVAL_MS)
                    val current = pet ?: break
                    pet = PetEngine.advanceTo(current, now())
                    persistAndPublish()
                    if (pet?.isDead == true) break
                }
            }
        }

        private fun persistAndPublish() {
            val current = pet ?: return
            _state.value = PetUiState.Alive(current.toUiState())
            viewModelScope.launch { repository.save(current) }
        }

        private fun now(): Long = System.currentTimeMillis()

        override fun onCleared() {
            tickJob?.cancel()
            super.onCleared()
        }
    }

private fun Pet.toUiState(): PetState =
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
