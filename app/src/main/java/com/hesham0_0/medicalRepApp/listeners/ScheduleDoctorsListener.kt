package com.hesham0_0.medicalRepApp.listeners

import com.hesham0_0.medicalRepApp.models.DoctorForCompany

interface ScheduleDoctorsListener {
    fun getScheduleDoctors(list:List<DoctorForCompany>)
}