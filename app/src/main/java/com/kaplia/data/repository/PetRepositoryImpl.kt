package com.kaplia.data.repository

import com.kaplia.data.db.KaplaDao
import com.kaplia.data.db.KaplaEntity
import com.kaplia.domain.model.LifecycleStage
import com.kaplia.domain.model.Pet
import com.kaplia.domain.model.PetGenome
import com.kaplia.ui.model.Pronoun
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PetRepositoryImpl @Inject constructor(private val dao: KaplaDao) : PetRepository {
    override fun observePet(): Flow<Pet?> {
        return dao.observe().map { it?.toDomain() }
    }

    override suspend fun loadPet(): Pet? {
        return dao.getOnce()?.toDomain()
    }

    override suspend fun save(pet: Pet) {
        dao.upsert(pet.toEntity())
    }

    override suspend fun clear() {
        dao.clear()
    }
}

private fun KaplaEntity.toDomain(): Pet {
    return Pet(
        name = name,
        pronoun = Pronoun.valueOf(pronoun),
        genome = PetGenome(hungerRate, energyRate, moodRate, vitality),
        birthEpochMs = birthEpochMs,
        lastSeenEpochMs = lastSeenEpochMs,
        day = day,
        hunger = hunger,
        energy = energy,
        mood = mood,
        health = health,
        stage = LifecycleStage.valueOf(stage),
        isDead = isDead,
        deathEpochMs = deathEpochMs,
    )
}

private fun Pet.toEntity(): KaplaEntity {
    return KaplaEntity(
        name = name,
        pronoun = pronoun.name,
        hungerRate = genome.hungerRate,
        energyRate = genome.energyRate,
        moodRate = genome.moodRate,
        vitality = genome.vitality,
        birthEpochMs = birthEpochMs,
        lastSeenEpochMs = lastSeenEpochMs,
        day = day,
        hunger = hunger,
        energy = energy,
        mood = mood,
        health = health,
        stage = stage.name,
        isDead = isDead,
        deathEpochMs = deathEpochMs,
    )
}
