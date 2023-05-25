package com.hesham.medicalRepApp.ui.doctors

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.hesham.medicalRepApp.R
import com.hesham.medicalRepApp.databinding.FragmentAddDoctorBinding
import com.hesham.medicalRepApp.methods.Utilities.Companion.EXTRA_DATA
import com.hesham.medicalRepApp.methods.Utilities.Companion.PICK_IMAGE_REQUEST
import com.hesham.medicalRepApp.methods.Utilities.Companion.REQUEST_CODE
import com.hesham.medicalRepApp.methods.Utilities.Companion.datePicker
import com.hesham.medicalRepApp.methods.Utilities.Companion.getBitmapFromUri
import com.hesham.medicalRepApp.methods.Utilities.Companion.isNetworkConnected
import com.hesham.medicalRepApp.methods.Utilities.Companion.showNoInternetToast
import com.hesham.medicalRepApp.models.DoctorClinic
import com.hesham.medicalRepApp.models.DoctorForCompany
import com.hesham.medicalRepApp.models.DoctorModel
import com.kizitonwose.calendar.core.daysOfWeek
import java.util.*

class AddDoctorFragment : Fragment() {
    private var _binding: FragmentAddDoctorBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: DoctorsViewModel
    private var location: List<Double>? = null
    private val selectedDate = Calendar.getInstance()
    private var bitmap: Bitmap? = null
    private var doctorCompany: DoctorForCompany? = null

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
        val genders = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, genders)
        binding.autoCompleteGender.setAdapter(arrayAdapter)
        addChipToGroup()

        val cities = resources.getStringArray(R.array.egypt_cities_array)
        val citiesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, cities)
        binding.autoCompleteCity.setAdapter(citiesAdapter)

        val specialties = resources.getStringArray(R.array.specialties_array)
        val specialtiesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, specialties)
        binding.specialtyAutoCompleteText.setAdapter(specialtiesAdapter)

        viewModel.addDoctorLocation.observe(viewLifecycleOwner) { location ->
            try {
                val geoCoder = Geocoder(requireContext(), Locale.ENGLISH).getFromLocation(
                    location[0],
                    location[1],
                    1
                )
                if (geoCoder != null) {
//                binding.autoCompleteCity.setText(geoCoder[0].adminArea?:"")
                    binding.autoCompleteArea.setText(geoCoder[0].locality ?: "")
                    binding.addressText.text = geoCoder[0].getAddressLine(0) ?: ""
                }
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onClickButtons() {
        binding.SaveButton.setOnClickListener {
            val doctor = onSave()
            if (!isNetworkConnected(requireContext())) {
                showNoInternetToast(requireContext())
            } else if (doctor != null) {
                doctor.id = doctor.name + doctor.phoneNum
                viewModel.addDoctor(doctor, bitmap, doctorCompany!!)
//                viewModel.addCity(doctor.city!!, doctor.area!!)
                findNavController().navigateUp()
            }
        }

        binding.LocationBtn.setOnClickListener {
            val intent = Intent(requireActivity(), MapActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        binding.LastVisitBtn.setOnClickListener {
            datePicker(selectedDate, requireContext(), binding.LastVisitBtn)
        }

        binding.doctorImageView.setOnClickListener {
            selectPhoto()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            viewModel.addDoctorLocation.value = data?.extras?.get(EXTRA_DATA) as List<Double>
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            if (selectedImageUri != null) {
                bitmap =
                    getBitmapFromUri(selectedImageUri, requireActivity().contentResolver, 500, 500)
                binding.doctorImageView.setImageBitmap(bitmap)
            }
        }
    }

    private fun addChipToGroup() {
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

    private fun onSave(): DoctorModel? {
        val theme: Resources.Theme = requireContext().theme
        val inputLayoutList = arrayListOf(
            binding.addDoctorName,
            binding.specialty,
            binding.PhoneNum,
            binding.gender,
            binding.city,
            binding.center,
        )
        for (inputLayout in inputLayoutList) {
            if (inputLayout.editText!!.text.trim().isEmpty()) {
                inputLayout.apply {
                    error = "${inputLayout.editText!!.hint} is required"
                    boxStrokeErrorColor = ColorStateList.valueOf(resources.getColor(R.color.red))
                    errorIconDrawable =
                        ResourcesCompat.getDrawable(resources, R.drawable.error_svgrepo_com, theme)
                }
                return null
            }
        }

        val dates = mutableListOf<Map<String, Int>>()
        val city = binding.city.editText!!.text.toString()
        val daysIds = binding.chipDaysGroup.checkedChipIds
        for (day in daysIds) {
            val map = mapOf(city to day)
            dates.add(map)
        }
        val clinic = DoctorClinic(
            center = binding.center.editText!!.text.toString(),
            days = binding.chipDaysGroup.checkedChipIds,
            location = location,
            city = binding.city.editText!!.text.toString(),
            area = binding.area.editText!!.text.toString()
        )
        val clinics = listOf(clinic)

        doctorCompany = DoctorForCompany(
            doctorId = "dummy",
            days = dates,
            doctorData = null,
            visitsByMonth = binding.radioGroup.checkedRadioButtonId,
            lastVisit = selectedDate.timeInMillis,
            products = null,
            notes = binding.notes.editText?.text.toString(),
            visitsList = null,
            orderList = null,
            clinics = null,
            lastState = null,
            totalYearOrders = null
        )

        return DoctorModel(
            id = "dummy",
            name = binding.addDoctorName.editText!!.text.toString().lowercase(),
            specialty = binding.specialty.editText!!.text.toString(),
            photoUrl = "",
            phoneNum = "20" + binding.PhoneNum.editText!!.text.toString(),
            days = dates,
            gender = binding.gender.editText!!.text.toString(),
            clinics = clinics,
        )
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
}