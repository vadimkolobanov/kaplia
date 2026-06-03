package com.kaplia.ui.viewmodel

import com.kaplia.ui.model.PetState

/** Top-level UI state: still loading from Room, no pet yet (onboarding), or a living pet. */
sealed interface PetUiState {
    data object Loading : PetUiState

    data object NoPet : PetUiState

    data class Alive(val pet: PetState) : PetUiState
}
