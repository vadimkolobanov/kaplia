package com.kaplia.domain.model

import com.kaplia.ui.model.Pronoun

/**
 * Domain state of a living Kaplia.
 *
 * Time is the source of truth: [day] and [stage] are derived from [birthEpochMs],
 * and metric decay is applied for real time elapsed since [lastSeenEpochMs].
 */
data class Pet(
    val name: String,
    val pronoun: Pronoun,
    val genome: PetGenome = PetGenome(),
    val birthEpochMs: Long,
    val lastSeenEpochMs: Long,
    val day: Int = 1,
    val hunger: Float = 0.75f,
    val energy: Float = 0.82f,
    val mood: Float = 0.88f,
    val health: Float = 0.90f,
    val stage: LifecycleStage = LifecycleStage.CHILDHOOD,
    val isDead: Boolean = false,
    val deathEpochMs: Long? = null,
)
