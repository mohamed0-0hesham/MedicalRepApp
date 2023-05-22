package com.hesham.medicalRepApp

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class SharedPreferencesManager(context: Context) {
    private var instance: SharedPreferencesManager? = null
    private var sharedPreferences: SharedPreferences? = null
    init {
        sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getInstance(context: Context): SharedPreferencesManager? {
        if (instance == null) {
            instance = SharedPreferencesManager(context)
        }
        return instance
    }
    
}
