package com.hesham.medicalRepApp.ui.doctors

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.hesham.medicalRepApp.R
import com.hesham.medicalRepApp.databinding.FragmentAddDoctorBinding
import com.hesham.medicalRepApp.models.DoctorModel
import com.kizitonwose.calendar.core.daysOfWeek

class AddDoctorFragment : Fragment() {
    val REQUEST_CODE =123
    private val EXTRA_DATA = "EXTRA_DATA"
    private var _binding: FragmentAddDoctorBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: DoctorsViewModel
    var location:List<Double>? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[DoctorsViewModel::class.java]
        _binding = FragmentAddDoctorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiInit()
        onClickButtons()

    }

    private fun uiInit() {
        var city= listOf<String>()

        val genders = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, genders)
        binding.autoCompleteGender.setAdapter(arrayAdapter)
        addChipToGroup()

        val cities =resources.getStringArray(R.array.cities)
        val citiesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, cities)
        binding.autoCompleteCity.setAdapter(citiesAdapter)
//        binding.autoCompleteCity.addTextChangedListener { it->
//            city =viewModel.getAreas(it.toString())
//            Log.i("Test", it.toString())
//        }
//            val areasAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, city)
//            binding.autoCompleteArea.setAdapter(areasAdapter)
    }
    private fun onClickButtons(){
        binding.SaveButton.setOnClickListener {
            checkEmptyFields()
            val doctor=onSave()
            viewModel.addDoctor(doctor)
            viewModel.addCity(doctor.city!!,doctor.area!!)
            findNavController().navigateUp()
        }
        binding.LocationBtn.setOnClickListener {
            val intent=Intent(requireActivity(),MapActivity::class.java)
            startActivityForResult(intent,REQUEST_CODE)
        }
        binding.LastVisitBtn.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setView(requireActivity().layoutInflater.inflate(R.layout.pick_time_dialog, null))
            builder.create()
            builder.show()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==REQUEST_CODE && resultCode == Activity.RESULT_OK){
             location= data?.extras?.get(EXTRA_DATA) as List<Double>
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun addChipToGroup(){
        val weekList = daysOfWeek()
        for (day in weekList) {
            val chip = Chip(context)
            var name = ""
            for (i in 0..2) {
                name += day.name[i]
            }
            chip.text = name
            chip.isCheckable = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                chip.id = day.value
            }
            binding.chipDaysGroup.addView(chip as View)
        }
    }

    private fun onSave(): DoctorModel {
        return DoctorModel(
            "dummy",
            name = binding.addDoctorName.editText!!.text.toString(),
            specialty = binding.specialty.editText!!.text.toString(),
            phoneNum = binding.PhoneNum.editText!!.text.toString(),
            days = binding.chipDaysGroup.checkedChipIds,
            gender = binding.gender.editText!!.text.toString(),
            location = location,
            visitsByMonth = binding.radioGroup.checkedRadioButtonId,
            city = binding.city.editText!!.text.toString(),
            area = binding.area.editText!!.text.toString(),
            center = binding.center.editText!!.text.toString(),
            products = binding.ProductsGroup.checkedChipIds,
            notes = binding.notes.editText!!.text.toString(),
            lastVisit = "15/2/2009"
        )
    }
    private fun checkEmptyFields() {

    }
}