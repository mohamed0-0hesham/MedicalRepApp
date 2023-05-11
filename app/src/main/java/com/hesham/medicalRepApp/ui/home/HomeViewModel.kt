package com.hesham.medicalRepApp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hesham.medicalRepApp.data.DoctorsRepository
import com.hesham.medicalRepApp.databinding.CalendarDayLayoutBinding
import com.hesham.medicalRepApp.listeners.DoctorsListener
import com.hesham.medicalRepApp.models.DoctorModel
import java.util.Date

class HomeViewModel : ViewModel() {
    private val repository = DoctorsRepository()
    val doctorList: MutableLiveData<List<DoctorModel>> = MutableLiveData()
    val selectedDay: MutableLiveData<Date> = MutableLiveData()
    var selectedDayItem: MutableLiveData<CalendarDayLayoutBinding> = MutableLiveData()
    val lastSelectedDayItem: MutableLiveData<CalendarDayLayoutBinding> = MutableLiveData()
    val selectedDoctor: MutableLiveData<DoctorModel> = MutableLiveData()

    fun getScheduledDoctorsList(selectedDate:Date,city:String) {
        val futureDate = selectedDate.time - (14 * 24 * 60 * 60 * 1000)
        repository.getScheduledDoctors(object : DoctorsListener {
            override fun getDoctorsList(list: List<DoctorModel>) {
                doctorList.value=list
            }
        },futureDate.toString(),selectedDate.day,city)
    }
}