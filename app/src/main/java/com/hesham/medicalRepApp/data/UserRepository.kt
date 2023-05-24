package com.hesham.medicalRepApp.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.hesham.medicalRepApp.methods.Utilities.Companion.END_LOCATION_KEY
import com.hesham.medicalRepApp.methods.Utilities.Companion.END_TIME_KEY
import com.hesham.medicalRepApp.methods.Utilities.Companion.REPORTS_COLLECTION
import com.hesham.medicalRepApp.methods.Utilities.Companion.USERS_COLLECTION
import com.hesham.medicalRepApp.methods.Utilities.Companion.VISITS_COLLECTION
import com.hesham.medicalRepApp.models.ReportModel
import com.hesham.medicalRepApp.models.UserModel
import com.hesham.medicalRepApp.models.VisitModel
import java.lang.ref.WeakReference

class UserRepository private constructor(
//    mContext: Context
) {
    private var db = FirebaseFirestore.getInstance()
    private var mAuth = FirebaseAuth.getInstance()
    private val authCurrentUser = mAuth.currentUser
    private var storage: FirebaseStorage? = FirebaseStorage.getInstance()
    private var storageRef: StorageReference? = storage!!.reference
    private val fireAuthUser = FirebaseAuth.getInstance().currentUser
    var currentUser: UserModel? = null
//    private var applicationContext = mContext.applicationContext

    companion object {
        private var instance: UserRepository? = null
        fun getInstance(): UserRepository {
            if (instance == null) {
                instance = UserRepository()
            }
            return instance!!
        }
    }

    init {
        if (mAuth.currentUser != null) {
            getLiveCurrentUserData(mAuth.currentUser!!.uid)
        }
    }

     fun setProfileUpdatesRequest(name: String?, imgUrl: String?) {
        val profileUpdatesRequest = UserProfileChangeRequest.Builder()
        if (!name.isNullOrBlank() || !imgUrl.isNullOrBlank()) {
            if (!name.isNullOrBlank()) {
                profileUpdatesRequest.displayName = name
            }
            if (!imgUrl.isNullOrBlank()) {
                profileUpdatesRequest.photoUri = imgUrl.toUri()
            }
            val profileUpdates = profileUpdatesRequest.build()
            authCurrentUser?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Test", "User name updated.")
                        Log.d("Test", "User name is ${authCurrentUser.displayName}")

                    } else {
                        Log.d("Test", "isNotSuccessful ${task.exception?.message}")
                    }
                }
        }
    }

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

     fun getLiveCurrentUserData(userId: String) {
        db.collection(USERS_COLLECTION)
            .document(userId)
            .addSnapshotListener(EventListener { value, error ->
                if (error != null) {
                    Log.e("test " + javaClass.name, error.toString())
                } else if (value != null) {
                    currentUser = value.toObject(UserModel::class.java)
                } else {
                    Log.e("test " + javaClass.name, "there isn't profile for the currentUser")
                }
            })
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
            .document(fireAuthUser!!.uid)
            .collection(VISITS_COLLECTION)
            .document() //we could create it by doctor.name+date
        visit.id = visitRef.id
        visitRef.set(visit)
    }

    fun startLocation(report: ReportModel) {
        db.collection(USERS_COLLECTION)
            .document(fireAuthUser!!.uid)
            .collection(REPORTS_COLLECTION)
            .document(report.date!!)//we could create it by date
            .set(report).addOnCompleteListener { task ->
            }
    }

    fun endLocation(report: ReportModel) {
        db.collection(USERS_COLLECTION)
            .document(fireAuthUser!!.uid)
            .collection(REPORTS_COLLECTION)
            .document(report.date!!)//we could create it by date
            .update(mapOf(END_LOCATION_KEY to report.endLocation, END_TIME_KEY to report.endTime))
            .addOnCompleteListener { task ->
            }
    }
}