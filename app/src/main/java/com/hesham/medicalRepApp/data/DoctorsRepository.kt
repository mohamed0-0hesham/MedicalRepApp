package com.hesham.medicalRepApp.data

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.hesham.medicalRepApp.listeners.DoctorsListener
import com.hesham.medicalRepApp.methods.Utilities.Companion.CITY
import com.hesham.medicalRepApp.methods.Utilities.Companion.CLINICS_COLLECTION
import com.hesham.medicalRepApp.methods.Utilities.Companion.DOCTORS_COLLECTION
import com.hesham.medicalRepApp.methods.Utilities.Companion.DOCTOR_DAYS
import com.hesham.medicalRepApp.methods.Utilities.Companion.LAST_VISIT
import com.hesham.medicalRepApp.methods.Utilities.Companion.NAME_KEY
import com.hesham.medicalRepApp.models.DoctorModel
import com.hesham.medicalRepApp.ui.doctors.DoctorsViewModel
import java.util.Calendar

class DoctorsRepository {
    private val database = FirebaseDatabase.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val ref = database.reference
    val calendar = Calendar.getInstance()

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
        val ref = db.collection(DOCTORS_COLLECTION).document()
        doctor.id = ref.id
        ref.set(doctor)
        db.collection(DOCTORS_COLLECTION)
            .document(ref.id)
            .collection(CLINICS_COLLECTION)
            .document(doctor.city + doctor.area)
            .set(doctor)
    }

    fun getDoctors(listener: DoctorsListener) {
        val list: ArrayList<DoctorModel> = arrayListOf()
        db.collection(DOCTORS_COLLECTION)
            .orderBy(LAST_VISIT)
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

    fun searchDoctor(listener: DoctorsListener, name: String) {
        val list: ArrayList<DoctorModel> = arrayListOf()
        db.collection(DOCTORS_COLLECTION)
            .whereGreaterThanOrEqualTo(NAME_KEY, name)
            .whereLessThanOrEqualTo(NAME_KEY, name + "\uf8ff")
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

    fun getScheduledDoctors(
        listener: DoctorsListener,
        beforeTwoWeek: String,
        day: Int,
        city: String
    ) {
        val list: ArrayList<DoctorModel> = arrayListOf()
        db.collection(DOCTORS_COLLECTION)
            .whereLessThanOrEqualTo(LAST_VISIT, beforeTwoWeek)
            .whereArrayContains(DOCTOR_DAYS, day)
            .whereEqualTo(CITY, city)
            .orderBy(LAST_VISIT)
            .limit(10)
            .addSnapshotListener { value, error ->
                list.clear()
                if (error == null) {
                    for (doc in value!!) {
                        val doctor = doc.toObject(DoctorModel::class.java)
                        list.add(doctor)
                    }
                    DoctorsViewModel().doctorList.value = list
                    listener.getDoctorsList(list)
                    Log.i("Test", "getScheduledDoctors error == null")
                }
                Log.i("Test", "getScheduledDoctors error =" + error?.message)
            }
    }

    fun addCity(city: String, area: String) {
        ref.child("City").child(city).child(area).setValue(area)
    }
}