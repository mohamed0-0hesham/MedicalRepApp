package com.hesham.medicalRepApp.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.GeoPoint
import com.hesham.medicalRepApp.data.DoctorsRepository
import com.hesham.medicalRepApp.data.UserRepository
import com.hesham.medicalRepApp.databinding.CalendarDayLayoutBinding
import com.hesham.medicalRepApp.listeners.DoctorsListener
import com.hesham.medicalRepApp.methods.Utilities.Companion.formattedDateOf
import com.hesham.medicalRepApp.models.DoctorModel
import com.hesham.medicalRepApp.models.ReportModel
import java.util.Date

class HomeViewModel : ViewModel() {
    private val doctorsRepository = DoctorsRepository()
    private val userRepository = UserRepository.getInstance()
    val doctorList: MutableLiveData<List<DoctorModel>> = MutableLiveData()
    val selectedDay: MutableLiveData<Date> = MutableLiveData()
    val selectedCity: MutableLiveData<String> = MutableLiveData("Domiat")
    var selectedDayItem: MutableLiveData<CalendarDayLayoutBinding> = MutableLiveData()
    val lastSelectedDayItem: MutableLiveData<CalendarDayLayoutBinding> = MutableLiveData()
    val startedLocation : MutableLiveData<Boolean> = MutableLiveData(false)

    fun getScheduledDoctorsList(selectedDate:Date,city:String) {
        val futureDate = selectedDate.time - (14 * 24 * 60 * 60 * 1000)
        doctorsRepository.getScheduledDoctors(object : DoctorsListener {
            override fun getDoctorsList(list: List<DoctorModel>) {
                doctorList.value=list
            }
        },futureDate.toString(),selectedDate.day,city)
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