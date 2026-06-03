package com.kaplia.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Application-scoped dependency graph entry point.
 *
 * Bindings are added here as the data/domain layers land:
 * Room repositories (#19), Health Connect (#54), LLM client (#53).
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule
