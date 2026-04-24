package com.leonestudio.tawba

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.leonestudio.tawba.data.TawbaDatabase
import com.leonestudio.tawba.data.UserPreferences

class TawbaApplication : Application() {

    val database: TawbaDatabase by lazy { TawbaDatabase.getInstance(this) }
    val userPreferences: UserPreferences by lazy { UserPreferences(this) }

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) {}
    }
}
