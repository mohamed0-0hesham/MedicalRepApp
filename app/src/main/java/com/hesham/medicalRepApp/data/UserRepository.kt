package com.hesham.medicalRepApp.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.hesham.medicalRepApp.methods.Utilities.Companion.END_LOCATION_KEY
import com.hesham.medicalRepApp.methods.Utilities.Companion.END_TIME_KEY
import com.hesham.medicalRepApp.methods.Utilities.Companion.REPORTS_COLLECTION
import com.hesham.medicalRepApp.methods.Utilities.Companion.USERS_COLLECTION
import com.hesham.medicalRepApp.methods.Utilities.Companion.VISITS_COLLECTION
import com.hesham.medicalRepApp.models.ReportModel
import com.hesham.medicalRepApp.models.UserModel
import com.hesham.medicalRepApp.models.VisitModel

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    fun saveUserData(user: UserModel) {
        db.collection(USERS_COLLECTION)
            .document(user.id!!)
            .set(user)
    }

    fun updateUserData(userId: String, key: String, value: Any) {
        db.collection(USERS_COLLECTION)
            .document(userId)
            .update(mapOf(key to value))
    }

    fun addToFireStoreArray(userId: String, key: String, value: Any) {
        db.collection(USERS_COLLECTION)
            .document(userId)
            .update(key, FieldValue.arrayUnion(value))
    }

    fun removeFromFireStoreArray(userId: String, key: String, value: Any) {
        db.collection(USERS_COLLECTION)
            .document(userId)
            .update(key, FieldValue.arrayRemove(value))
    }

    fun createVisit(visit: VisitModel) {
        val visitRef = db.collection(USERS_COLLECTION)
            .document(currentUser!!.uid)
            .collection(VISITS_COLLECTION)
            .document() //we could create it by doctor.name+date
        visit.id = visitRef.id
        visitRef.set(visit)
    }

    fun startLocation(report: ReportModel) {
        db.collection(USERS_COLLECTION)
            .document(currentUser!!.uid)
            .collection(REPORTS_COLLECTION)
            .document(report.date!!)//we could create it by date
            .set(report).addOnCompleteListener { task ->
            }
    }

    fun endLocation(report: ReportModel) {
        db.collection(USERS_COLLECTION)
            .document(currentUser!!.uid)
            .collection(REPORTS_COLLECTION)
            .document(report.date!!)//we could create it by date
            .update(mapOf(END_LOCATION_KEY to report.endLocation, END_TIME_KEY to report.endTime))
            .addOnCompleteListener { task ->
            }
    }
}