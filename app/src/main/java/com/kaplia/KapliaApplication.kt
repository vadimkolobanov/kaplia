package com.kaplia

import android.app.Application

class KapliaApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Firebase.initialize(this) — after google-services.json added (#37)
        // Hilt.createApplicationComponent(this) — after DI setup (#39)
        // Rive.init(this) — after animation library decision (#41)
    }
}
