package com.example.petclinic

import android.app.Application
import io.embrace.android.embracesdk.Embrace

class PetClinicApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Embrace.getInstance().start(this)
    }
}