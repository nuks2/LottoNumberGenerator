package com.example.lottonumbergenerator.utils

import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun context(): Context = applicationContext

    companion object {
        lateinit var instance: App
            private set
    }
}

