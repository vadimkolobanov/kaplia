package com.kaplia.ui.model

import com.kaplia.domain.model.LifecycleStage

data class PetState(
    val name: String,
    val day: Int = 1,
    val hunger: Float = 0.75f,
    val energy: Float = 0.82f,
    val mood: Float = 0.88f,
    val health: Float = 0.90f,
    val stage: LifecycleStage = LifecycleStage.CHILDHOOD,
    val isDead: Boolean = false,
)
