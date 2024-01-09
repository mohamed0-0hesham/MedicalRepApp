package com.hesham0_0.medicalRepApp.ui.doctors


import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hesham0_0.medicalRepApp.data.DoctorsRepository
import com.hesham0_0.medicalRepApp.listeners.DoctorsListener
import com.hesham0_0.medicalRepApp.models.DoctorClinic
import com.hesham0_0.medicalRepApp.models.DoctorForCompany
import com.hesham0_0.medicalRepApp.models.DoctorModel

class DoctorsViewModel : ViewModel() {

    private val repository = DoctorsRepository()
    val doctorList: MutableLiveData<List<DoctorModel>> = MutableLiveData()
    val searchDoctorList: MutableLiveData<List<DoctorModel>> = MutableLiveData()
    val searchText: MutableLiveData<String> = MutableLiveData()
    val selectedDoctor: MutableLiveData<DoctorModel> = MutableLiveData()
    val addDoctorLocation: MutableLiveData<List<Double>> = MutableLiveData()
    val addClinic: MutableLiveData<Boolean> = MutableLiveData(false)

    fun addDoctor(doctorModel: DoctorModel, bitmap: Bitmap?, doctorCompany:DoctorForCompany) {
        repository.addDoctor(doctorModel,bitmap, doctorCompany)
    }

    fun addClinic(clinic: DoctorClinic) {
        repository.addClinic(clinic)
    }

    fun addCity(city: String, area: String) {
        repository.addCity(city, area)
    }

    fun getAreas(city: String): ArrayList<String> {
        return repository.getAreasList(city)
    }

    fun getDoctorsList() {
        repository.getDoctors(object : DoctorsListener {
            override fun getDoctorsList(list: List<DoctorModel>) {
                doctorList.value=list
            }
        })
    }

    fun getSearchDoctorList(name:String) {
        repository.searchDoctor(object : DoctorsListener {
            override fun getDoctorsList(list: List<DoctorModel>) {
                searchDoctorList.value=list
            }
        }, name)
    }
}