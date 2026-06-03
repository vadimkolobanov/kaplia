package com.kaplia.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [KaplaEntity::class], version = 2, exportSchema = true)
abstract class KapliaDatabase : RoomDatabase() {
    abstract fun kaplaDao(): KaplaDao
}
