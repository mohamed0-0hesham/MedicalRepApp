package com.hesham.medicalRepApp.data

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.hesham.medicalRepApp.listeners.DoctorsListener
import com.hesham.medicalRepApp.models.DoctorModel
import com.hesham.medicalRepApp.ui.doctors.DoctorsViewModel

class DoctorsRepository {
    private val database = FirebaseDatabase.getInstance()
    private val db = FirebaseFirestore.getInstance()
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
        db.collection("Doctors")
            .document()
            .set(doctor)
    }

    fun getDoctors(listener: DoctorsListener) {
        val list: ArrayList<DoctorModel> = arrayListOf()
        db.collection("Doctors")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (doc in task.result) {
                        val doctor = doc.toObject(DoctorModel::class.java)
                        list.add(doctor)
                    }
                    DoctorsViewModel().doctorList.value = list
                    listener.getDoctorsList(list)
                }
            }
    }

    fun addCity(city: String, area: String) {
        ref.child("City").child(city).child(area).setValue(area)
    }
}