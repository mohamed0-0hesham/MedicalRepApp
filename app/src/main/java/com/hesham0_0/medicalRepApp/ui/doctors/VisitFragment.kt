package com.hesham0_0.medicalRepApp.ui.doctors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.hesham0_0.medicalRepApp.adapters.ClinicsAdapter
import com.hesham0_0.medicalRepApp.databinding.FragmentVisitBinding
import com.hesham0_0.medicalRepApp.methods.Utilities.Companion.datePicker
import com.hesham0_0.medicalRepApp.methods.Utilities.Companion.formattedDateOf
import java.util.*

class VisitFragment : Fragment() {
    private var _binding: FragmentVisitBinding? = null
    private val binding get() = _binding!!
    private  val viewModel: DoctorsViewModel by activityViewModels()
    private lateinit var clinicsAdapter: ClinicsAdapter
    private var calendar=Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVisitBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiInit()
        onClickButtons()
    }

    private fun uiInit() {
        binding.itemXml = viewModel.selectedDoctor.value
        binding.visitTime.text = "At ${formattedDateOf(calendar.timeInMillis,"h:mm a dd MMM yyyy")}"
        clinicsAdapter = ClinicsAdapter(requireContext())
        binding.clinicRecycler.adapter =clinicsAdapter
        clinicsAdapter.setData(viewModel.selectedDoctor.value!!.clinics!!)
    }

    private fun onClickButtons() {
//        binding.SaveButton.setOnClickListener {
//            val Visit = onSave()
//            if (!Utilities.isNetworkConnected(requireContext())) {
//                Utilities.showNoInternetToast(requireContext())
//            }else if(doctor != null){
//                doctor.id=doctor.name+doctor.phoneNum
//                viewModel.addDoctor(doctor,bitmap)
//                viewModel.addCity(doctor.city!!, doctor.area!!)
//                findNavController().navigateUp()
//            }
//        }
//
        binding.visitTime.setOnClickListener {
            datePicker(calendar,requireContext(),binding.visitTime)
        }
//
//        binding.doctorImageView.setOnClickListener {
//            selectPhoto()
//        }
    }

}