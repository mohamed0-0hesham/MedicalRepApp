package com.hesham.medicalRepApp.ui.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.core.view.get
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
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

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var daysAdapter: DaysAdapter
    private lateinit var scheduledAdapter: DoctorScheduleAdapter
    private lateinit var homeViewModel: HomeViewModel
    private val calendar = Calendar.getInstance()
    private lateinit var firstSelectedCard: CardView
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
        homeViewModel.selectedDay.observe(viewLifecycleOwner) { date ->
            daysAdapter.setSelectDay(date.date - 1)
            val dateFormatter = SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH)
            binding.monthPickerbutton.text = dateFormatter.format(date)
            homeViewModel.getScheduledDoctorsList(date!!)
        }

        homeViewModel.selectedDayItem.observe(viewLifecycleOwner) { layout ->
            if (homeViewModel.lastSelectedDayItem.value != null) {
                homeViewModel.lastSelectedDayItem.value!!.apply {
                    calendarDayText.setBackgroundColor(resources.getColor(R.color.white))
                    dayOfMonth.setTextColor(resources.getColor(R.color.blueOp_30))
                    dayOfWeek.setTextColor(resources.getColor(R.color.blueOp_30))
                }
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
        homeViewModel.getScheduledDoctorsList(homeViewModel.selectedDay.value!!)
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
                homeViewModel.selectedDoctor.value = doctorModel
                findNavController().navigate(R.id.action_nav_home_to_doctorDetailsFragment)
            }
        }
        )
        binding.scheduleRecycler.adapter = scheduledAdapter

        homeViewModel.doctorList.observe(viewLifecycleOwner) { list ->
            scheduledAdapter.setData(list)
        }
        binding.monthPickerbutton.setOnClickListener {
            datePicker()
        }

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