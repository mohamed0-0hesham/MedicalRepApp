package com.hesham0_0.medicalRepApp.ui.doctors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.hesham0_0.medicalRepApp.R
import com.hesham0_0.medicalRepApp.adapters.ClinicsAdapter
import com.hesham0_0.medicalRepApp.databinding.FragmentDoctorDetailsBinding

class DoctorDetailsFragment : Fragment() {
    private var _binding: FragmentDoctorDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var clinicsAdapter: ClinicsAdapter
    private  val viewModel: DoctorsViewModel by activityViewModels()

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
        binding.visitButton.setOnClickListener {
            findNavController().navigate(R.id.action_doctorDetailsFragment_to_visitFragment)
        }
        initUi()
    }
    private fun initUi(){
        clinicsAdapter = ClinicsAdapter(requireContext())
        binding.clinicRecycler.adapter =clinicsAdapter
        clinicsAdapter.setData(viewModel.selectedDoctor.value!!.clinics!!)
        binding.AddClinicBtn.setOnClickListener {
            viewModel.addClinic.value=true
            findNavController().navigate(R.id.action_doctorDetailsFragment_to_addDoctorFragment)
        }
    }
}