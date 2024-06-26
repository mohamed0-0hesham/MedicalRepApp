package com.hesham0_0.medicalRepApp.ui.doctors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.hesham0_0.medicalRepApp.R
import com.hesham0_0.medicalRepApp.adapters.DoctorAdapter
import com.hesham0_0.medicalRepApp.adapters.listener.OnItemClickListener
import com.hesham0_0.medicalRepApp.databinding.FragmentDoctorsBinding
import com.hesham0_0.medicalRepApp.models.DoctorModel

class DoctorsFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentDoctorsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DoctorsViewModel by activityViewModels()
    private var myAdapter = DoctorAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.getDoctorsList()
        super.onCreate(savedInstanceState)
    }

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
        viewModel.doctorList.observe(viewLifecycleOwner) { list ->
            if (viewModel.searchText.value.isNullOrBlank()){
                myAdapter.setData(list)
            }
        }
        viewModel.searchDoctorList.observe(viewLifecycleOwner) { list ->
            if (!viewModel.searchText.value.isNullOrBlank()){
                myAdapter.setData(list)
            }
        }
        binding.DoctorAddButton.setOnClickListener {
            viewModel.addClinic.value=false
            findNavController().navigate(R.id.action_nav_doctors_to_addDoctorFragment)
        }
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return search(query?.lowercase())
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return search(newText?.lowercase())
            }
        })
    }

    fun search(queryText: String?):Boolean{
        viewModel.searchText.value = queryText
        if (!queryText.isNullOrBlank()){
            viewModel.getSearchDoctorList(queryText.toString())
        }else{
            if (viewModel.doctorList.value!=null){
                myAdapter.setData(viewModel.doctorList.value!!)
            }
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int, doctorModel: DoctorModel,visitBtn:Boolean) {
        viewModel.selectedDoctor.value = doctorModel
        findNavController().navigate(R.id.action_nav_doctors_to_doctorDetailsFragment)
    }
}