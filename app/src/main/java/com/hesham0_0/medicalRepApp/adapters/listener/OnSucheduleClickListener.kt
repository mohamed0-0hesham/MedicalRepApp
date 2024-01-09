package com.hesham0_0.medicalRepApp.adapters.listener

import com.hesham0_0.medicalRepApp.models.DoctorForCompany

interface OnSucheduleClickListener {
    fun onItemClick(position: Int, doctorModel: DoctorForCompany,visitBtn:Boolean)
}