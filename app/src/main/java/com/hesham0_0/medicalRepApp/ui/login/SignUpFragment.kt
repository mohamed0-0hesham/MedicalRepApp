package com.hesham0_0.medicalRepApp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.hesham0_0.medicalRepApp.R
import com.hesham0_0.medicalRepApp.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.createRequestState.observe(viewLifecycleOwner) {
            when (it) {
                "email created Successfully please check your verification mail" -> {
                    binding.SignUpProgressBar.visibility = View.GONE
                    Toast.makeText(context, "the Mail created Successfully", Toast.LENGTH_LONG)
                        .show()
                    replaceFragment(VerificationFragment())
                }
                else -> {
                    binding.SignUpProgressBar.visibility=View.GONE
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
        }
        binding.goLoginBtn.setOnClickListener {
            replaceFragment(LoginFragment())
        }
        binding.signUpBtn.setOnClickListener {
            binding.SignUpProgressBar.visibility = View.VISIBLE
            onClickSignUp()
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val tr = requireFragmentManager().beginTransaction()
        tr.replace(R.id.container, fragment).commit()
    }

    private fun onClickSignUp() {
        var notNull = false
        var isSame = false
        val name = binding.edtSignUpFullNamed
        val email = binding.edtSignUpEmail
        val phoneNum = binding.edtSignUpMobile
        val password = binding.edtSignUpPassword
        val passwordConfirm = binding.edtSignUpConfirmPassword
        val arr = arrayOf(name, email, phoneNum, password, passwordConfirm)
        for (i in arr) {
            if (i.editText?.text.toString() == "") {
                required(i)
            } else {
                notNull = true
            }
        }
        if (password.editText?.text.toString() != passwordConfirm.editText?.text.toString()) {
            binding.confirmErrorTxt.visibility = View.VISIBLE
        } else {
            isSame = true
        }
        if (isSame && notNull) {
            viewModel.createAccount(
                requireActivity(),
                name.editText?.text.toString(),
                email.editText?.text.toString(),
                phoneNum.editText?.text.toString(),
                password.editText?.text.toString()
            )
        }
    }

    private fun required(inputLayout: TextInputLayout) {
        inputLayout.helperText = "required*"
        inputLayout.boxStrokeColor = resources.getColor(R.color.red)
        inputLayout.hintTextColor = resources.getColorStateList(R.color.red)
    }

}