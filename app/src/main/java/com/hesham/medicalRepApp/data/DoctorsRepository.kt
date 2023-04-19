package com.hesham.medicalRepApp.data

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hesham.medicalRepApp.models.DoctorModel
import com.hesham.medicalRepApp.ui.doctors.DoctorsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DoctorsRepository {
    private val database = FirebaseDatabase.getInstance()
    private val doctorsRef = database.getReference("Doctors")
    private val ref = database.reference

    fun getAreasList(city: String): ArrayList<String> {
        val areaList = arrayListOf<String>()
        areaList.clear()
        val snapshot = ref.child(city).get().result
        for (n in snapshot.children) {
            val area = n.value
            areaList.add(0, area!!.toString())
        }
        return areaList
    }

    fun addDoctor(doctor: DoctorModel) {
        doctor.id = doctorsRef.push().key
        ref.child("Doctors").child(doctor.id!!).setValue(doctor)
    }

    fun addCity(city: String, area: String) {
        ref.child("City").child(city).child(area).setValue(area)
    }
}