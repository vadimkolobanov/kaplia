package com.kaplia.domain.model

import com.kaplia.ui.model.Pronoun

data class Pet(
    val name: String,
    val pronoun: Pronoun,
    val genome: PetGenome = PetGenome(),
    val day: Int = 1,
    val hunger: Float = 0.75f,
    val energy: Float = 0.82f,
    val mood: Float = 0.88f,
    val health: Float = 0.90f,
    val stage: LifecycleStage = LifecycleStage.NEWBORN,
    val isDead: Boolean = false,
)
