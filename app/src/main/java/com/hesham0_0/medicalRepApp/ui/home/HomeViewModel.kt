package com.hesham0_0.medicalRepApp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.GeoPoint
import com.hesham0_0.medicalRepApp.data.DoctorsRepository
import com.hesham0_0.medicalRepApp.data.UserRepository
import com.hesham0_0.medicalRepApp.databinding.CalendarDayLayoutBinding
import com.hesham0_0.medicalRepApp.listeners.ScheduleDoctorsListener
import com.hesham0_0.medicalRepApp.methods.Utilities.Companion.formattedDateOf
import com.hesham0_0.medicalRepApp.models.DoctorForCompany
import com.hesham0_0.medicalRepApp.models.ReportModel
import java.util.Date

class HomeViewModel : ViewModel() {
    private val doctorsRepository = DoctorsRepository()
    private val userRepository = UserRepository.getInstance()
    val doctorList: MutableLiveData<List<DoctorForCompany>> = MutableLiveData()
    val scheduleDoctorsList: MutableLiveData<List<DoctorForCompany>> = MutableLiveData()
    val selectedDay: MutableLiveData<Date> = MutableLiveData()
    val selectedCity: MutableLiveData<String> = MutableLiveData("Domiat")
    var selectedDayItem: MutableLiveData<CalendarDayLayoutBinding> = MutableLiveData()
    val lastSelectedDayItem: MutableLiveData<CalendarDayLayoutBinding> = MutableLiveData()
    val startedLocation : MutableLiveData<Boolean> = MutableLiveData(false)

    fun getScheduledDoctorsList(selectedDate:Date,city:String) {
        val futureDate = selectedDate.time - (14 * 24 * 60 * 60 * 1000)
        doctorsRepository.getScheduledDoctors(object : ScheduleDoctorsListener {
            override fun getScheduleDoctors(list: List<DoctorForCompany>) {
                scheduleDoctorsList.value=list
            }
        },futureDate,selectedDate.day,city)
    }

    fun updateUser(userId:String,key:String,value:Any){
        userRepository.updateUserData(userId,key,value)
    }

    fun startLocation(startPoint: GeoPoint, uid: String, time: Long) {
        val report=ReportModel(null,uid,Date().time,formattedDateOf(time,"dd-MM-yyyy"),startPoint,null,null,null)
        userRepository.startLocation(report)
    }

    fun endLocation(endPoint: GeoPoint, uid: String, time: Long) {
        val report=ReportModel(null,uid,null,formattedDateOf(time,"dd-MM-yyyy"),null,null,endPoint,Date().time)
        userRepository.endLocation(report)
    }
}