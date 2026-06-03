package com.kaplia.ui.component

import com.kaplia.ui.model.PetState

/** Facial expression of the blob, derived from the pet's current state. */
enum class BlobExpression { HAPPY, NEUTRAL, HUNGRY, SLEEPY, SICK, DEAD }

private const val SICK_HEALTH = 0.3f
private const val LOW_ENERGY = 0.25f
private const val LOW_HUNGER = 0.25f
private const val HAPPY_MOOD = 0.75f

fun expressionFor(pet: PetState): BlobExpression =
    when {
        pet.isDead -> BlobExpression.DEAD
        pet.health < SICK_HEALTH -> BlobExpression.SICK
        pet.energy < LOW_ENERGY -> BlobExpression.SLEEPY
        pet.hunger < LOW_HUNGER -> BlobExpression.HUNGRY
        pet.mood > HAPPY_MOOD -> BlobExpression.HAPPY
        else -> BlobExpression.NEUTRAL
    }
