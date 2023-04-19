package com.hesham.medicalRepApp.ui.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

class LoginViewModel : ViewModel() {
    private var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks? = null

    var phoneNum: String? = null

    private var mToken: PhoneAuthProvider.ForceResendingToken? = null

    var mActivity: Activity? = null

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    private val _createRequestState: MutableLiveData<String> = MutableLiveData()
    val createRequestState: LiveData<String> = _createRequestState

    private val _loginRequestState: MutableLiveData<String> = MutableLiveData()
    val loginRequestState: LiveData<String> = _loginRequestState

    fun createAccount(
        activity: Activity,
        name: String,
        email: String,
        phone: String,
        password: String
    ) {
        mActivity = activity
        val emailAuth = auth.createUserWithEmailAndPassword(email, password)
        emailAuth.addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                _createRequestState.value = "Authentication Successful"
                Log.w("Test", "Authentication Successful.")
                sendEmailVerification()
                setName(name)
                verifyPhoneNum(phone)

            } else {
                _createRequestState.value = "${task.exception?.message}"
            }
        }
    }

    private fun setName(name: String) {
        val profileUpdatesRequest = UserProfileChangeRequest.Builder()
        if (name.isNullOrBlank() || name != user?.displayName) {
            profileUpdatesRequest.displayName = name
            val profileUpdates = profileUpdatesRequest.build()
            user?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _createRequestState.value = "User name updated."
                        Log.d("Test", "User name updated.")
                        Log.d("Test", "User name is ${user.displayName}")

                    } else {
                        _createRequestState.value =
                            "isNotSuccessful ${task.exception?.message}"
                        Log.d("Test", "isNotSuccessful ${task.exception?.message}")
                    }
                }
        }
    }

    private fun sendEmailVerification() {
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _createRequestState.value = "email created Successfully please check your verification mail"
                    Log.d("Test", "User email send verification ")
                } else {
                    _createRequestState.value =
                        "isNotSuccessful ${task.exception?.message}"
                    Log.d("Test", "isNotSuccessful ${task.exception?.message}")
                }
            }
    }

    fun login(activity: Activity, email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user!!.isEmailVerified) {
                        _loginRequestState.value = "Successful Login"
                    } else {
                        _loginRequestState.value =
                            "Please check your Email for verification"
                    }
                } else {
                    _loginRequestState.value =
                        "login failed ${task.exception!!.message}"
                }
            }
    }

    private fun verifyPhoneNum(phone: String) {
        phoneNum = phone
        mCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                Log.i("Test","Verification Completed")
//                updateUserPhoneNum(phoneAuthCredential, mActivity!!)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                _createRequestState.value="onVerificationFailed ${e.message}"
                Log.i("Test", "onVerificationFailed ${e.message}")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)
                Log.i("Test", "onCodeSent ${verificationId}")
                mVerificationId= verificationId
                mToken = token
            }
        }
        startPhoneNumberVerification(phone)
    }

    private fun startPhoneNumberVerification(phone: String) {
        val option = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(120L, TimeUnit.SECONDS)
            .setActivity(mActivity!!)
            .setCallbacks(mCallBack!!)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(option)
    }

    fun resendVerificationCade() {
        val option = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNum!!)
            .setTimeout(120L, TimeUnit.SECONDS)
            .setActivity(mActivity!!)
            .setCallbacks(mCallBack!!)
            .setForceResendingToken(mToken!!)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(option)
    }

//    fun verifyWithCode(activity: Activity,code: String) {
//        val credential = PhoneAuthProvider.getCredential(mVerificationId, code)
//        auth.signInWithCredential(credential)
//            .addOnCompleteListener(activity) { task ->
//                if (task.isSuccessful) {
//                    _createRequestState.value = "PhoneNumber Verified Successfully"
//                    Log.i("Test", "PhoneNumber Verified Successfully")
//                    updateUserPhoneNum(credential,activity)
//                } else {
//                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
//                        _createRequestState.value = "Invalid OTP"
//                    }
//                }
//            }
//    }

    fun updateUserPhoneNum(activity: Activity,code:String ) {
        val credential = PhoneAuthProvider.getCredential(mVerificationId, code)
        user!!.updatePhoneNumber(credential).addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                _createRequestState.value = "updatePhoneNumber isSuccessful"
                Log.i("Test","updatePhoneNumber isSuccessful")
            } else {
                Log.i("Test","updatePhoneNumber failed ${task.exception?.message}")
            }
        }
    }
    companion object{
        var mVerificationId:String=""
    }
}
