package com.kaplia.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Single-row table holding the one living Kaplia. */
@Entity(tableName = "kapla")
data class KaplaEntity(
    @PrimaryKey val id: Int = SINGLE_ROW_ID,
    val name: String,
    val hungerRate: Float,
    val energyRate: Float,
    val moodRate: Float,
    val vitality: Float,
    val birthEpochMs: Long,
    val lastSeenEpochMs: Long,
    val day: Int,
    val hunger: Float,
    val energy: Float,
    val mood: Float,
    val health: Float,
    val stage: String,
    val isDead: Boolean,
    val deathEpochMs: Long?,
) {
    companion object {
        const val SINGLE_ROW_ID = 0
    }
}
