package com.hesham.medicalRepApp.listeners

import android.location.Location

interface LocationListener {
    fun getLocation(location: Location?)
}