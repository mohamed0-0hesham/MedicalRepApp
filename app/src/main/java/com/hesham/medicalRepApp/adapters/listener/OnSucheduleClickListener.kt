package com.hesham.medicalRepApp.adapters.listener

import com.hesham.medicalRepApp.models.DoctorForCompany
import com.hesham.medicalRepApp.models.DoctorModel

interface OnSucheduleClickListener {
    fun onItemClick(position: Int, doctorModel: DoctorForCompany,visitBtn:Boolean)
}