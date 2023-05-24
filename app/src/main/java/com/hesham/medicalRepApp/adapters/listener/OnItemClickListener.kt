package com.hesham.medicalRepApp.adapters.listener

import com.hesham.medicalRepApp.models.DoctorModel

interface OnItemClickListener {
    fun onItemClick(position: Int, doctorModel: DoctorModel,visitBtn:Boolean)
}