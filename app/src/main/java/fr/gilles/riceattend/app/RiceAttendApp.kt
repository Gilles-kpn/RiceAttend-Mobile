package fr.gilles.riceattend.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RiceAttendApp : Application() {
    companion object {
        lateinit var instance: RiceAttendApp private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}