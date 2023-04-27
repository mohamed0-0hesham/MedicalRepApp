package com.hesham.medicalRepApp.ui.doctors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.hesham.medicalRepApp.R
import com.hesham.medicalRepApp.adapters.DoctorAdapter
import com.hesham.medicalRepApp.adapters.listener.OnItemClickListener
import com.hesham.medicalRepApp.databinding.FragmentDoctorsBinding
import com.hesham.medicalRepApp.models.DoctorModel

class DoctorsFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentDoctorsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DoctorsViewModel by activityViewModels()
    private var myAdapter = DoctorAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.doctorsRecycler.adapter = myAdapter
        viewModel.getDoctorsList()
        viewModel.doctorList.observe(viewLifecycleOwner) { list ->
            myAdapter.setData(list)
        }
        binding.DoctorAddButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_doctors_to_addDoctorFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int, doctorModel: DoctorModel) {
        viewModel.selectedDoctor.value = doctorModel
        findNavController().navigate(R.id.action_nav_doctors_to_doctorDetailsFragment)
    }
}