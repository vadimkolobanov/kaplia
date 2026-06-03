package com.kaplia

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KapliaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // Firebase.initialize(this) — after google-services.json added (#37)
        // Rive.init(this) — after animation library decision (#41)
    }
}
