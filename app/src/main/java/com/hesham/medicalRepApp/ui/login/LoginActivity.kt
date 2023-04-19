package com.hesham.medicalRepApp.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hesham.medicalRepApp.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container,
                    LoginFragment()
                )
                .commitNow()
        }
    }
}