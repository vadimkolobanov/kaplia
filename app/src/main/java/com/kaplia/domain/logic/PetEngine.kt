package com.kaplia.domain.logic

import com.kaplia.domain.model.LifecycleStage
import com.kaplia.domain.model.Pet

object PetEngine {
    private const val BASE_HUNGER_DECAY = 0.004f
    private const val BASE_ENERGY_DECAY = 0.003f
    private const val BASE_MOOD_DECAY = 0.002f
    private const val CRITICAL_THRESHOLD = 0.2f
    private const val HEALTH_REGEN = 0.001f
    private const val HEALTH_DECAY_ONE = 0.005f
    private const val HEALTH_DECAY_TWO = 0.015f
    private const val HEALTH_DECAY_THREE = 0.030f
    private const val FEED_HUNGER = 0.30f
    private const val FEED_MOOD = 0.05f
    private const val PLAY_MOOD = 0.25f
    private const val PLAY_ENERGY_COST = 0.10f
    private const val PLAY_HUNGER_COST = 0.05f
    private const val REST_ENERGY = 0.35f
    private const val REST_MOOD = 0.05f

    fun computeStage(day: Int): LifecycleStage =
        when (day) {
            in 1..3 -> LifecycleStage.NEWBORN
            in 4..7 -> LifecycleStage.GROWING
            in 8..15 -> LifecycleStage.MATURE
            in 16..25 -> LifecycleStage.AGING
            in 26..30 -> LifecycleStage.CRITICAL
            else -> LifecycleStage.DEAD
        }

    fun tick(pet: Pet): Pet {
        if (pet.isDead) return pet

        val g = pet.genome
        val newHunger = (pet.hunger - BASE_HUNGER_DECAY * g.hungerRate).coerceIn(0f, 1f)
        val newEnergy = (pet.energy - BASE_ENERGY_DECAY * g.energyRate).coerceIn(0f, 1f)
        val newMood = (pet.mood - BASE_MOOD_DECAY * g.moodRate).coerceIn(0f, 1f)

        val criticalCount = listOf(newHunger, newEnergy, newMood).count { it < CRITICAL_THRESHOLD }
        val healthDelta = when (criticalCount) {
            0 -> HEALTH_REGEN * g.vitality
            1 -> -HEALTH_DECAY_ONE
            2 -> -HEALTH_DECAY_TWO
            else -> -HEALTH_DECAY_THREE
        }
        val newHealth = (pet.health + healthDelta).coerceIn(0f, 1f)

        val canDie = pet.stage != LifecycleStage.NEWBORN && pet.stage != LifecycleStage.GROWING
        val isDead = canDie && (newHealth <= 0f || newHunger <= 0f)

        return pet.copy(
            hunger = newHunger,
            energy = newEnergy,
            mood = newMood,
            health = newHealth,
            isDead = isDead,
        )
    }

    fun feed(pet: Pet): Pet =
        pet.copy(
            hunger = (pet.hunger + FEED_HUNGER).coerceAtMost(1f),
            mood = (pet.mood + FEED_MOOD).coerceAtMost(1f),
        )

    fun play(pet: Pet): Pet =
        pet.copy(
            mood = (pet.mood + PLAY_MOOD).coerceAtMost(1f),
            energy = (pet.energy - PLAY_ENERGY_COST).coerceAtLeast(0f),
            hunger = (pet.hunger - PLAY_HUNGER_COST).coerceAtLeast(0f),
        )

    fun rest(pet: Pet): Pet =
        pet.copy(
            energy = (pet.energy + REST_ENERGY).coerceAtMost(1f),
            mood = (pet.mood + REST_MOOD).coerceAtMost(1f),
        )

    fun advanceDay(pet: Pet): Pet {
        val newDay = pet.day + 1
        val newStage = computeStage(newDay)
        return pet.copy(
            day = newDay,
            stage = newStage,
            isDead = newStage == LifecycleStage.DEAD,
        )
    }
}
