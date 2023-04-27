package com.hesham.medicalRepApp.listeners

import com.hesham.medicalRepApp.models.DoctorModel

interface DoctorsListener {
    fun getDoctorsList(list:List<DoctorModel>)
}