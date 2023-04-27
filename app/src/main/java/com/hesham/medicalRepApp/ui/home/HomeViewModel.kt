package com.hesham.medicalRepApp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hesham.medicalRepApp.databinding.CalendarDayLayoutBinding
import com.hesham.medicalRepApp.models.DayModel
import com.hesham.medicalRepApp.models.DoctorModel

class HomeViewModel : ViewModel() {

    val selectedDay: MutableLiveData<DayModel> = MutableLiveData()
    var selectedDayItem: MutableLiveData<CalendarDayLayoutBinding> = MutableLiveData()
    val lastSelectedDayItem: MutableLiveData<CalendarDayLayoutBinding> = MutableLiveData()

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
}