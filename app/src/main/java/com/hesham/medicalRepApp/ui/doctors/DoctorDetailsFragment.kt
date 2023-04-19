package com.hesham.medicalRepApp.ui.doctors

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.hesham.medicalRepApp.R
import com.hesham.medicalRepApp.databinding.FragmentDoctorDetailsBinding
import com.hesham.medicalRepApp.databinding.FragmentDoctorsBinding
import kotlin.math.log

class DoctorDetailsFragment : Fragment() {
    private var _binding: FragmentDoctorDetailsBinding? = null
    private val binding get() = _binding!!
    private  val viewModel: DoctorsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.itemXml = viewModel.selectedDoctor.value
    }
}