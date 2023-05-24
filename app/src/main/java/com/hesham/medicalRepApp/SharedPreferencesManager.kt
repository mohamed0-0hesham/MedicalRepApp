package com.hesham.medicalRepApp

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.google.firebase.platforminfo.DefaultUserAgentPublisher
import com.hesham.medicalRepApp.methods.Utilities.Companion.SHARED_DEFAULT_CITY

class SharedPreferencesManager(context: Context) {
    private var instance: SharedPreferencesManager? = null
    private var sharedPreferences: SharedPreferences? = null
    init {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    fun getInstance(context: Context): SharedPreferencesManager? {
        if (instance == null) {
            instance = SharedPreferencesManager(context)
        }
        return instance
    }

    fun setDefaultCity(defaultCity: String){
        val editor =sharedPreferences!!.edit()
        editor.putString(SHARED_DEFAULT_CITY,defaultCity)
        editor.apply()
    }

    fun getDefaultCity(){
        sharedPreferences!!.getString(SHARED_DEFAULT_CITY,"Alexandria")
    }

}
