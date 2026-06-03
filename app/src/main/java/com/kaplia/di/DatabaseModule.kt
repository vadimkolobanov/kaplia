package com.kaplia.di

import android.content.Context
import androidx.room.Room
import com.kaplia.data.db.KaplaDao
import com.kaplia.data.db.KapliaDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): KapliaDatabase =
        Room.databaseBuilder(context, KapliaDatabase::class.java, "kaplia.db").build()

    @Provides
    fun provideKaplaDao(database: KapliaDatabase): KaplaDao = database.kaplaDao()
}
