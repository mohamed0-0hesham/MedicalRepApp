package com.hesham.medicalRepApp.ui.doctors


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hesham.medicalRepApp.data.DoctorsRepository
import com.hesham.medicalRepApp.methods.BindingAdapters.Companion.doctorsRef
import com.hesham.medicalRepApp.models.DoctorModel
import kotlinx.coroutines.flow.Flow

class DoctorsViewModel : ViewModel() {

    private val repository = DoctorsRepository()
    val doctorList: MutableLiveData<ArrayList<DoctorModel>> = MutableLiveData()

    val selectedDoctor: MutableLiveData<DoctorModel> = MutableLiveData()

    fun addDoctor(doctorModel: DoctorModel) {
        repository.addDoctor(doctorModel)
    }

    fun addCity(city: String, area: String) {
        repository.addCity(city, area)
    }

    fun getAreas(city: String): ArrayList<String> {
        return repository.getAreasList(city)
    }

    fun getDoctorModelList() {
        val list = arrayListOf<DoctorModel>()
        doctorsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (n in snapshot.children) {
                    val doctor = n.getValue(DoctorModel::class.java)
                    list.add(0, doctor!!)
                }
                doctorList.value = list
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}