package com.kaplia.ui.model

import androidx.annotation.StringRes
import com.kaplia.R

enum class Pronoun(
    @StringRes val labelRes: Int,
) {
    HE(R.string.onboarding_pronoun_he),
    SHE(R.string.onboarding_pronoun_she),
    THEY(R.string.onboarding_pronoun_they),
    IT(R.string.onboarding_pronoun_it),
}
