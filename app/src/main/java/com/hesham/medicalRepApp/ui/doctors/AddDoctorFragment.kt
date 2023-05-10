package com.hesham.medicalRepApp.ui.doctors

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.chip.Chip
import com.hesham.medicalRepApp.R
import com.hesham.medicalRepApp.databinding.FragmentAddDoctorBinding
import com.hesham.medicalRepApp.methods.Utilities.Companion.getBitmapFromUri
import com.hesham.medicalRepApp.methods.Utilities.Companion.uploadToStorage
import com.hesham.medicalRepApp.models.DoctorModel
import com.kizitonwose.calendar.core.daysOfWeek
import java.util.*

class AddDoctorFragment : Fragment() {

    val REQUEST_CODE = 123
    private val PICK_IMAGE_REQUEST = 1
    private val EXTRA_DATA = "EXTRA_DATA"
    private var _binding: FragmentAddDoctorBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: DoctorsViewModel
    private var location: List<Double>? = null
    private val selectedDate = Calendar.getInstance()
    private var bitmap:Bitmap?=null

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
        var city = listOf<String>()

        val genders = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, genders)
        binding.autoCompleteGender.setAdapter(arrayAdapter)
        addChipToGroup()

        val cities = resources.getStringArray(R.array.cities)
        val citiesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, cities)
        binding.autoCompleteCity.setAdapter(citiesAdapter)
//        binding.autoCompleteCity.addTextChangedListener { it->
//            city =viewModel.getAreas(it.toString())
//            Log.i("Test", it.toString())
//        }
//            val areasAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, city)
//            binding.autoCompleteArea.setAdapter(areasAdapter)
    }

    private fun onClickButtons() {

        binding.SaveButton.setOnClickListener {
            val doctor = onSave()
            if (doctor != null) {
                doctor.id=doctor.name+doctor.phoneNum
                viewModel.addDoctor(doctor)
                viewModel.addCity(doctor.city!!, doctor.area!!)
                uploadToStorage(bitmap,doctor)
                findNavController().navigateUp()
            }
        }

        binding.LocationBtn.setOnClickListener {
            val intent = Intent(requireActivity(), MapActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE)
        }

        binding.LastVisitBtn.setOnClickListener {
            datePicker()
        }

        binding.doctorImageView.setOnClickListener {
            selectPhoto()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            location = data?.extras?.get(EXTRA_DATA) as List<Double>
        }
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            if (selectedImageUri != null) {
                bitmap =getBitmapFromUri(selectedImageUri, requireActivity().contentResolver, 500, 500)
                binding.doctorImageView.setImageBitmap(bitmap)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
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
        )
        for (inputLayout in inputLayoutList) {
            if (inputLayout.editText!!.text.trim().isEmpty()) {
                inputLayout.apply {
                    error="${inputLayout.editText!!.hint} is required"
                    boxStrokeErrorColor= ColorStateList.valueOf(resources.getColor(R.color.red))
                    errorIconDrawable=resources.getDrawable(R.drawable.error_svgrepo_com,theme)
                }
                return null
            }
        }


        return DoctorModel(
            "dummy",
            name = binding.addDoctorName.editText!!.text.toString(),
            specialty = binding.specialty.editText!!.text.toString(),
            photoUrl = "",
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
            lastVisit = selectedDate.timeInMillis.toString()
        )
    }

    private fun datePicker() {
        val initialYear = selectedDate.get(Calendar.YEAR)
        val initialMonth = selectedDate.get(Calendar.MONTH)
        val initialDay = selectedDate.get(Calendar.DAY_OF_MONTH)
        val initialHour = selectedDate.get(Calendar.HOUR_OF_DAY)
        val initialMinute = selectedDate.get(Calendar.MINUTE)

        val timeSetListener = TimePickerDialog.OnTimeSetListener { viewTime, hourOfDay, minute ->
            selectedDate.set(Calendar.HOUR_OF_DAY, hourOfDay)
            selectedDate.set(Calendar.MINUTE, minute)
            selectedDate.timeInMillis
        }
        val timePickerDialog = TimePickerDialog(
            context,
            timeSetListener,
            initialHour, initialMinute,
            false
        )

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { viewDate, year, month, dayOfMonth ->
                selectedDate.set(year, month, dayOfMonth)
                timePickerDialog.show()
            }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            dateSetListener,
            initialYear, initialMonth, initialDay
        )
        datePickerDialog.show()
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
}