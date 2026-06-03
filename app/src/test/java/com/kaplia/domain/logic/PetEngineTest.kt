package com.kaplia.domain.logic

import com.kaplia.domain.model.LifecycleStage
import com.kaplia.domain.model.Pet
import com.kaplia.domain.model.PetGenome
import com.kaplia.ui.model.Pronoun
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PetEngineTest {
    private fun pet(
        birthEpochMs: Long = 0L,
        lastSeenEpochMs: Long = 0L,
        hunger: Float = 1f,
        energy: Float = 1f,
        mood: Float = 1f,
        health: Float = 1f,
    ): Pet {
        return Pet(
            name = "Test",
            pronoun = Pronoun.THEY,
            genome = PetGenome(),
            birthEpochMs = birthEpochMs,
            lastSeenEpochMs = lastSeenEpochMs,
            hunger = hunger,
            energy = energy,
            mood = mood,
            health = health,
        )
    }

    @Test
    fun computeStageMatchesAdrBoundaries() {
        assertEquals(LifecycleStage.CHILDHOOD, PetEngine.computeStage(1))
        assertEquals(LifecycleStage.CHILDHOOD, PetEngine.computeStage(7))
        assertEquals(LifecycleStage.ADOLESCENCE, PetEngine.computeStage(8))
        assertEquals(LifecycleStage.ADOLESCENCE, PetEngine.computeStage(14))
        assertEquals(LifecycleStage.MATURITY, PetEngine.computeStage(15))
        assertEquals(LifecycleStage.MATURITY, PetEngine.computeStage(21))
        assertEquals(LifecycleStage.AGING, PetEngine.computeStage(22))
        assertEquals(LifecycleStage.AGING, PetEngine.computeStage(30))
        assertEquals(LifecycleStage.DEAD, PetEngine.computeStage(31))
    }

    @Test
    fun dayForProgressesWithGameDays() {
        assertEquals(1, PetEngine.dayFor(0L, 0L))
        assertEquals(2, PetEngine.dayFor(0L, PetEngine.GAME_DAY_MS))
        assertEquals(8, PetEngine.dayFor(0L, PetEngine.GAME_DAY_MS * 7))
    }

    @Test
    fun noDeathBeforeDay8EvenWithZeroMetrics() {
        val p = pet(birthEpochMs = 0L, lastSeenEpochMs = 0L, hunger = 0f, health = 0f)
        val result = PetEngine.advanceTo(p, 0L)
        assertFalse(result.isDead)
    }

    @Test
    fun deathFromDay8WhenHungerZero() {
        val birth = -PetEngine.GAME_DAY_MS * 8
        val p = pet(birthEpochMs = birth, lastSeenEpochMs = 0L, hunger = 0f)
        val result = PetEngine.advanceTo(p, 0L)
        assertTrue(result.isDead)
        assertEquals(LifecycleStage.DEAD, result.stage)
    }

    @Test
    fun decayReducesHungerOverTime() {
        val p = pet(birthEpochMs = 0L, lastSeenEpochMs = 0L, hunger = 1f)
        val result = PetEngine.advanceTo(p, 60_000L)
        assertTrue(result.hunger < 1f)
    }

    @Test
    fun feedRaisesHunger() {
        assertTrue(PetEngine.feed(pet(hunger = 0.2f)).hunger > 0.2f)
    }
}
