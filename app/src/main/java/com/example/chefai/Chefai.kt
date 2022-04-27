package com.example.chefai

import android.app.Application
import com.google.android.material.color.DynamicColors

class Chefai: Application() {
    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

    }
}