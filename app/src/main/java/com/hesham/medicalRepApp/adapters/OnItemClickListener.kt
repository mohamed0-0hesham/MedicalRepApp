package com.hesham.medicalRepApp.adapters

import com.hesham.medicalRepApp.models.DoctorModel

interface OnItemClickListener {
    fun onItemClick(position: Int, doctorModel: DoctorModel)
}