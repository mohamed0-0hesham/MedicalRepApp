package com.hesham.medicalRepApp.adapters.listener

import com.hesham.medicalRepApp.databinding.CalendarDayLayoutBinding
import com.hesham.medicalRepApp.models.DayModel

interface OnDayItemClickListener {
    fun onItemClick(position: Int, dayItem: DayModel, binding: CalendarDayLayoutBinding)
}