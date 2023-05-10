package com.hesham.medicalRepApp.adapters.listener

import com.hesham.medicalRepApp.databinding.CalendarDayLayoutBinding
import java.util.Date

interface OnDayItemClickListener {
    fun onItemClick(position: Int, dayItem: Date, bindingItem: CalendarDayLayoutBinding)
}