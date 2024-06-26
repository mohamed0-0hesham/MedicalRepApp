package com.hesham0_0.medicalRepApp.data

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hesham0_0.medicalRepApp.listeners.DoctorsListener
import com.hesham0_0.medicalRepApp.listeners.ScheduleDoctorsListener
import com.hesham0_0.medicalRepApp.methods.Utilities
import com.hesham0_0.medicalRepApp.methods.Utilities.Companion.CLINICS_KEY
import com.hesham0_0.medicalRepApp.methods.Utilities.Companion.COMPANIES_COLLECTION
import com.hesham0_0.medicalRepApp.methods.Utilities.Companion.COMPANY_ID
import com.hesham0_0.medicalRepApp.methods.Utilities.Companion.DOCTORS_COLLECTION
import com.hesham0_0.medicalRepApp.methods.Utilities.Companion.DOCTOR_DAYS
import com.hesham0_0.medicalRepApp.methods.Utilities.Companion.LAST_VISIT
import com.hesham0_0.medicalRepApp.methods.Utilities.Companion.NAME_KEY
import com.hesham0_0.medicalRepApp.models.DoctorClinic
import com.hesham0_0.medicalRepApp.models.DoctorForCompany
import com.hesham0_0.medicalRepApp.models.DoctorModel
import com.hesham0_0.medicalRepApp.ui.doctors.DoctorsViewModel
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

    fun addDoctor(doctor: DoctorModel, bitmap: Bitmap?, doctorCompany: DoctorForCompany) {
        val ref = db.collection(DOCTORS_COLLECTION).document()
        doctor.id = ref.id
        ref.set(doctor).addOnSuccessListener {
            Utilities.uploadToStorage(bitmap, doctor.id!!)
        }
        doctorCompany.doctorId = ref.id
        addDoctorToCompany(doctorCompany)
    }

    private fun addDoctorToCompany(doctorCompany: DoctorForCompany) {
        db.collection(COMPANIES_COLLECTION)
            .document(COMPANY_ID)
            .collection(DOCTORS_COLLECTION)
            .document(doctorCompany.doctorId!!)
            .set(doctorCompany)
    }

    fun getDoctors(listener: DoctorsListener) {
        val list: ArrayList<DoctorModel> = arrayListOf()
        db.collection(DOCTORS_COLLECTION)
            .orderBy(NAME_KEY)
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
        listener: ScheduleDoctorsListener,
        beforeTwoWeek: Long,
        day: Int,
        city: String
    ) {
        val list: ArrayList<DoctorForCompany> = arrayListOf()
        db.collection(COMPANIES_COLLECTION)
            .document(COMPANY_ID)
            .collection(DOCTORS_COLLECTION)
            .whereLessThanOrEqualTo(LAST_VISIT, beforeTwoWeek)
            .whereArrayContains(DOCTOR_DAYS, mapOf(city to day))
            .orderBy(LAST_VISIT)
            .limit(10)
            .addSnapshotListener { value, error ->
                list.clear()
                if (error == null) {
                    if (value==null||value.isEmpty){
                        Log.i("Test", "doctor ")
                        listener.getScheduleDoctors(list)
                    }
                    for (doc in value!!) {
                        val doctor = doc.toObject(DoctorForCompany::class.java)
                        Log.i("Test", "doctor $doctor")
                        db.collection(DOCTORS_COLLECTION).document(doctor.doctorId!!).get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    doctor.doctorData =
                                        task.result.toObject(DoctorModel::class.java)
                                    Log.i("Test", "after doctor $doctor")
                                    list.add(doctor)
                                    listener.getScheduleDoctors(list)
                                }
                            }
                        Log.i("Test", "getScheduledDoctors error == null")
                    }
                }
                Log.i("Test", "getScheduledDoctors error =" + error?.message)
            }
    }


    fun addClinic(clinic: DoctorClinic) {
        val list = mutableListOf<Map<String,Int>>()
        for (day in clinic.days!!){
            list.add(mapOf(clinic.city!! to day))
        }
        val updates = mapOf(
            CLINICS_KEY to FieldValue.arrayUnion(clinic),
            DOCTOR_DAYS to FieldValue.arrayUnion(list)
        )
        db.collection(DOCTORS_COLLECTION)
            .document(clinic.doctorId!!)
            .update(updates)
    }

    fun addCity(city: String, area: String) {
        ref.child("City").child(city).child(area).setValue(area)
    }
}