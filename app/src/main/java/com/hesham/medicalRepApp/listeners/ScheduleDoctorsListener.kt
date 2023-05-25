package com.hesham.medicalRepApp.listeners

import com.hesham.medicalRepApp.models.DoctorForCompany
import com.hesham.medicalRepApp.models.DoctorModel

interface ScheduleDoctorsListener {
    fun getScheduleDoctors(list:List<DoctorForCompany>)
}