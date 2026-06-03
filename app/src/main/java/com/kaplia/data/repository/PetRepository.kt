package com.kaplia.data.repository

import com.kaplia.domain.model.Pet
import kotlinx.coroutines.flow.Flow

interface PetRepository {
    fun observePet(): Flow<Pet?>

    suspend fun loadPet(): Pet?

    suspend fun save(pet: Pet)

    suspend fun clear()
}
