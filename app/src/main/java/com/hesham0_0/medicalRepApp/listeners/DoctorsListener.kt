package com.hesham0_0.medicalRepApp.listeners

import com.hesham0_0.medicalRepApp.models.DoctorModel

interface DoctorsListener {
    fun getDoctorsList(list:List<DoctorModel>)
}