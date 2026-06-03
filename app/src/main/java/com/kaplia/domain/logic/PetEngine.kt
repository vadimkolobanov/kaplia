package com.kaplia.domain.logic

import com.kaplia.domain.model.LifecycleStage
import com.kaplia.domain.model.Pet
import com.kaplia.domain.model.PetGenome
import com.kaplia.ui.model.Pronoun

/**
 * Pure, time-driven lifecycle engine. No Android dependencies.
 *
 * Balance values below are DEMO speeds so the living behaviour is observable in a
 * short session. Production tuning (ADR-0001 = 30 real days) is Phase 2 work.
 */
object PetEngine {
    /** One in-game day in real milliseconds. DEMO: 2 min/day → full 30-day life ≈ 1 hour. */
    const val GAME_DAY_MS: Long = 2 * 60 * 1000L

    /** Permadeath unlocks at this day (start of adolescence) — ADR-0003. */
    const val PERMADEATH_DAY: Int = 8

    private const val LIFESPAN_DAYS = 30
    private const val CRITICAL_THRESHOLD = 0.2f

    // Per-real-minute metric decay (scaled by genome rates).
    private const val HUNGER_DECAY_PER_MIN = 0.06f
    private const val ENERGY_DECAY_PER_MIN = 0.05f
    private const val MOOD_DECAY_PER_MIN = 0.04f

    // Per-real-minute health change by number of critical metrics.
    private const val HEALTH_REGEN_PER_MIN = 0.03f
    private const val HEALTH_DECAY_ONE = 0.04f
    private const val HEALTH_DECAY_TWO = 0.12f
    private const val HEALTH_DECAY_THREE = 0.25f

    // Instant action boosts.
    private const val FEED_HUNGER = 0.30f
    private const val FEED_MOOD = 0.05f
    private const val PLAY_MOOD = 0.25f
    private const val PLAY_ENERGY_COST = 0.10f
    private const val PLAY_HUNGER_COST = 0.05f
    private const val REST_ENERGY = 0.35f
    private const val REST_MOOD = 0.05f

    fun newPet(
        name: String,
        pronoun: Pronoun,
        genome: PetGenome,
        nowMs: Long,
    ): Pet {
        return Pet(
            name = name,
            pronoun = pronoun,
            genome = genome,
            birthEpochMs = nowMs,
            lastSeenEpochMs = nowMs,
            day = 1,
            stage = LifecycleStage.CHILDHOOD,
        )
    }

    fun dayFor(
        birthEpochMs: Long,
        nowMs: Long,
    ): Int {
        val elapsed = (nowMs - birthEpochMs).coerceAtLeast(0L)
        return (elapsed / GAME_DAY_MS).toInt() + 1
    }

    fun computeStage(day: Int): LifecycleStage {
        return when (day) {
            in 1..7 -> LifecycleStage.CHILDHOOD
            in 8..14 -> LifecycleStage.ADOLESCENCE
            in 15..21 -> LifecycleStage.MATURITY
            in 22..LIFESPAN_DAYS -> LifecycleStage.AGING
            else -> LifecycleStage.DEAD
        }
    }

    /**
     * Advances the pet to [nowMs]: applies metric decay for elapsed real time,
     * recomputes day/stage from birth, resolves health and permadeath.
     * Same path for online ticks and offline (app-closed) progression.
     */
    fun advanceTo(
        pet: Pet,
        nowMs: Long,
    ): Pet {
        if (pet.isDead) return pet

        val elapsedMin = (nowMs - pet.lastSeenEpochMs).coerceAtLeast(0L) / 60_000f
        val g = pet.genome

        val hunger = (pet.hunger - HUNGER_DECAY_PER_MIN * g.hungerRate * elapsedMin).coerceIn(0f, 1f)
        val energy = (pet.energy - ENERGY_DECAY_PER_MIN * g.energyRate * elapsedMin).coerceIn(0f, 1f)
        val mood = (pet.mood - MOOD_DECAY_PER_MIN * g.moodRate * elapsedMin).coerceIn(0f, 1f)

        val criticals = listOf(hunger, energy, mood).count { it < CRITICAL_THRESHOLD }
        val healthRate = when (criticals) {
            0 -> HEALTH_REGEN_PER_MIN * g.vitality
            1 -> -HEALTH_DECAY_ONE
            2 -> -HEALTH_DECAY_TWO
            else -> -HEALTH_DECAY_THREE
        }
        val health = (pet.health + healthRate * elapsedMin).coerceIn(0f, 1f)

        val day = dayFor(pet.birthEpochMs, nowMs)
        val stage = computeStage(day)
        val canDie = day >= PERMADEATH_DAY
        val dead = stage == LifecycleStage.DEAD || (canDie && (health <= 0f || hunger <= 0f))

        return pet.copy(
            hunger = hunger,
            energy = energy,
            mood = mood,
            health = health,
            day = day,
            stage = if (dead) LifecycleStage.DEAD else stage,
            lastSeenEpochMs = nowMs,
            isDead = dead,
            deathEpochMs = if (dead && pet.deathEpochMs == null) nowMs else pet.deathEpochMs,
        )
    }

    fun feed(pet: Pet): Pet {
        return pet.copy(
            hunger = (pet.hunger + FEED_HUNGER).coerceAtMost(1f),
            mood = (pet.mood + FEED_MOOD).coerceAtMost(1f),
        )
    }

    fun play(pet: Pet): Pet {
        return pet.copy(
            mood = (pet.mood + PLAY_MOOD).coerceAtMost(1f),
            energy = (pet.energy - PLAY_ENERGY_COST).coerceAtLeast(0f),
            hunger = (pet.hunger - PLAY_HUNGER_COST).coerceAtLeast(0f),
        )
    }

    fun rest(pet: Pet): Pet {
        return pet.copy(
            energy = (pet.energy + REST_ENERGY).coerceAtMost(1f),
            mood = (pet.mood + REST_MOOD).coerceAtMost(1f),
        )
    }
}
