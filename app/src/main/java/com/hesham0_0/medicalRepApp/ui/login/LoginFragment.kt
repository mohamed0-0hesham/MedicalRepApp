package com.hesham0_0.medicalRepApp.ui.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hesham0_0.medicalRepApp.MainActivity
import com.hesham0_0.medicalRepApp.R
import com.hesham0_0.medicalRepApp.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {
    private var _binding:FragmentLoginBinding ? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (viewModel.user!=null){
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loginRequestState.observe(viewLifecycleOwner) {
            Log.i("Test","loginRequestState changed")
            when (it) {
                "Successful Login" -> {
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                }
                else->{binding.massageTxtView.text= it}
            }
        }
        binding.loginBtn.setOnClickListener {
            val email=binding.edtLoginEmail.editText?.text.toString()
            val password=binding.edtLoginPassword.editText?.text.toString()
            if (email.isNotEmpty()&&password.isNotEmpty()) {
                viewModel.login(requireActivity(), email, password)
            }else {
                binding.massageTxtView.text= "Email And Password Mustn't Me Null"
            }
        }
        binding.createNewBtn.setOnClickListener {
            replaceFragment(SignUpFragment())
        }
        binding.resendVerificationBtn.setOnClickListener {
            viewModel.sendEmailVerification()
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        val tr = requireFragmentManager().beginTransaction()
        tr.replace(R.id.container, fragment).commit()
    }

}