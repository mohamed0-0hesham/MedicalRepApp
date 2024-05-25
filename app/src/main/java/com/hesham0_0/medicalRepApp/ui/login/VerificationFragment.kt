package com.hesham0_0.medicalRepApp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.hesham0_0.medicalRepApp.R
import com.google.firebase.auth.*
import com.hesham0_0.medicalRepApp.databinding.FragmentVerificationBinding


class VerificationFragment : Fragment() {
    private var _binding: FragmentVerificationBinding? = null
    private val binding get() = _binding!!
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVerificationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.createRequestState.observe(viewLifecycleOwner) {
            when (it) {
                "updatePhoneNumber isSuccessful" -> {
                    Toast.makeText(context, "Phone Number Verified Successfully", Toast.LENGTH_LONG)
                        .show()
                    replaceFragment(LoginFragment())
                }
            }
        }

        binding.skipBtn.setOnClickListener {
            replaceFragment(LoginFragment())
        }
        binding.verifyBtn.setOnClickListener {
            val code = binding.edtSignUpFullNamed.editText?.text?.trim().toString()
            if (code.isNotEmpty()) {
                viewModel.updateUserPhoneNum(requireActivity(), code)
            } else {
                Toast.makeText(context, "Enter The code", Toast.LENGTH_LONG).show()
            }
        }
        binding.resendBtn.setOnClickListener {
            viewModel.resendVerificationCade()
        }
    }


    private fun replaceFragment(fragment: Fragment) {
        val tr = requireFragmentManager().beginTransaction()
        tr.replace(R.id.container, fragment).commit()
    }
}