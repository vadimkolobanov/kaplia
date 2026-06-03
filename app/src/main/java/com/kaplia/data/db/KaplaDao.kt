package com.kaplia.data.db

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface KaplaDao {
    @Query("SELECT * FROM kapla WHERE id = 0")
    fun observe(): Flow<KaplaEntity?>

    @Query("SELECT * FROM kapla WHERE id = 0")
    suspend fun getOnce(): KaplaEntity?

    @Upsert
    suspend fun upsert(entity: KaplaEntity)

    @Query("DELETE FROM kapla")
    suspend fun clear()
}
