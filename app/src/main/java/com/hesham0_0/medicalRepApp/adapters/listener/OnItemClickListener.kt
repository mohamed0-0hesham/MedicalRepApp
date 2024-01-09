package com.hesham0_0.medicalRepApp.adapters.listener

import com.hesham0_0.medicalRepApp.models.DoctorModel

interface OnItemClickListener {
    fun onItemClick(position: Int, doctorModel: DoctorModel,visitBtn:Boolean)
}