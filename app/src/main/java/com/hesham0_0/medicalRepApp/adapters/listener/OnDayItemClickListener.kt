package com.hesham0_0.medicalRepApp.adapters.listener

import com.hesham0_0.medicalRepApp.databinding.CalendarDayLayoutBinding
import java.util.Date

interface OnDayItemClickListener {
    fun onItemClick(position: Int, dayItem: Date, bindingItem: CalendarDayLayoutBinding)
}