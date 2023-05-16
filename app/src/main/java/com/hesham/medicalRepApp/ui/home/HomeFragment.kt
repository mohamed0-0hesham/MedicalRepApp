package com.hesham.medicalRepApp.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hesham.medicalRepApp.R
import com.hesham.medicalRepApp.adapters.DaysAdapter
import com.hesham.medicalRepApp.adapters.DoctorScheduleAdapter
import com.hesham.medicalRepApp.adapters.listener.OnDayItemClickListener
import com.hesham.medicalRepApp.adapters.listener.OnItemClickListener
import com.hesham.medicalRepApp.databinding.CalendarDayLayoutBinding
import com.hesham.medicalRepApp.databinding.FragmentHomeBinding
import com.hesham.medicalRepApp.models.DoctorModel
import com.hesham.medicalRepApp.ui.doctors.DoctorsViewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var daysAdapter: DaysAdapter
    private lateinit var scheduledAdapter: DoctorScheduleAdapter
    private lateinit var homeViewModel: HomeViewModel
    private  val viewModel: DoctorsViewModel by activityViewModels()
    private val calendar = Calendar.getInstance()
    private var selectedCity = "Domiat"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        uiInit()
        observe()
        return root
    }

    private fun observe() {
        homeViewModel.doctorList.observe(viewLifecycleOwner) { list ->
            scheduledAdapter.setData(list)
        }

        homeViewModel.selectedDay.observe(viewLifecycleOwner) { date ->
            daysAdapter.setSelectDay(date.date - 1)
            val dateFormatter = SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH)
            binding.monthPickerButton.text = dateFormatter.format(date)
            homeViewModel.getScheduledDoctorsList(date!!, selectedCity)
        }

        homeViewModel.selectedDayItem.observe(viewLifecycleOwner) { layout ->
            if (homeViewModel.lastSelectedDayItem.value == null) {
                homeViewModel.lastSelectedDayItem.value = daysAdapter.getLayout()
            }
            homeViewModel.lastSelectedDayItem.value!!.apply {
                calendarDayText.setBackgroundColor(resources.getColor(R.color.white))
                dayOfMonth.setTextColor(resources.getColor(R.color.colorPrimary))
                dayOfWeek.setTextColor(resources.getColor(R.color.colorPrimary))
            }
            layout.apply {
                calendarDayText.setBackgroundColor(resources.getColor(R.color.colorPrimary))
                dayOfMonth.setTextColor(resources.getColor(R.color.backgroundColor))
                dayOfWeek.setTextColor(resources.getColor(R.color.backgroundColor))
            }
            homeViewModel.lastSelectedDayItem.value = layout
        }

    }


    private fun uiInit() {
        if (homeViewModel.selectedDay.value == null) {
            homeViewModel.selectedDay.value = calendar.time
        }
        homeViewModel.getScheduledDoctorsList(homeViewModel.selectedDay.value!!, selectedCity)
        daysAdapter = DaysAdapter(requireContext(), object : OnDayItemClickListener {
            override fun onItemClick(
                position: Int,
                dayItem: Date,
                bindingItem: CalendarDayLayoutBinding
            ) {
                if (homeViewModel.selectedDay.value != dayItem) {
                    homeViewModel.selectedDayItem.value = bindingItem
                }
                homeViewModel.selectedDay.value = dayItem
            }
        })

        scheduledAdapter = DoctorScheduleAdapter(object : OnItemClickListener {
            override fun onItemClick(position: Int, doctorModel: DoctorModel) {
                viewModel.selectedDoctor.value=doctorModel
                findNavController().navigate(R.id.action_nav_home_to_doctorDetailsFragment)
            }
        }
        )
        binding.scheduleRecycler.adapter = scheduledAdapter

        binding.monthPickerButton.setOnClickListener {
            datePicker()
        }
        val cities = resources.getStringArray(R.array.cities)
        val citiesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, cities)
        binding.autoCompleteCity.setAdapter(citiesAdapter)

        val onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            selectedCity = parent.getItemAtPosition(position) as String
            homeViewModel.getScheduledDoctorsList(homeViewModel.selectedDay.value!!, selectedCity)
        }
        binding.autoCompleteCity.onItemClickListener=onItemClickListener

        binding.daysRecyclerView.adapter = daysAdapter
        getDays(calendar)
        binding.daysRecyclerView.scrollToPosition(homeViewModel.selectedDay.value!!.date - 3)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDays(calendar: Calendar) {
        val list = ArrayList<Date>()
        val cal = Calendar.getInstance()
        cal.time = calendar.time
        for (i in 1..cal.getMaximum(Calendar.DAY_OF_MONTH)) {
            cal.set(Calendar.DAY_OF_MONTH, i)
            list.add(cal.time)
        }
        daysAdapter.setData(list)
    }

    private fun datePicker() {
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = homeViewModel.selectedDay.value!!.date
        val dateSetListener =
            DatePickerDialog.OnDateSetListener { viewDate, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                homeViewModel.selectedDay.value = calendar.time
                getDays(calendar)
                binding.daysRecyclerView.smoothScrollToPosition(homeViewModel.selectedDay.value!!.date - 1)
            }

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            dateSetListener,
            initialYear, initialMonth, initialDay
        )
        datePickerDialog.show()
    }
}