package com.hesham0_0.medicalRepApp.ui.home

import android.app.DatePickerDialog
import android.location.Location
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.GeoPoint
import com.hesham0_0.medicalRepApp.R
import com.hesham0_0.medicalRepApp.adapters.DaysAdapter
import com.hesham0_0.medicalRepApp.adapters.DoctorScheduleAdapter
import com.hesham0_0.medicalRepApp.adapters.listener.OnDayItemClickListener
import com.hesham0_0.medicalRepApp.adapters.listener.OnSucheduleClickListener
import com.hesham0_0.medicalRepApp.data.UserRepository
import com.hesham0_0.medicalRepApp.databinding.CalendarDayLayoutBinding
import com.hesham0_0.medicalRepApp.databinding.FragmentHomeBinding
import com.hesham0_0.medicalRepApp.listeners.LocationListener
import com.hesham0_0.medicalRepApp.methods.Utilities.Companion.getCurrentLocation
import com.hesham0_0.medicalRepApp.methods.Utilities.Companion.isNetworkConnected
import com.hesham0_0.medicalRepApp.methods.Utilities.Companion.showNoInternetToast
import com.hesham0_0.medicalRepApp.models.DoctorForCompany
import com.hesham0_0.medicalRepApp.ui.doctors.DoctorsViewModel
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var daysAdapter: DaysAdapter
    private lateinit var scheduledAdapter: DoctorScheduleAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var userRepository: UserRepository
    private val viewModel: DoctorsViewModel by activityViewModels()
    private val calendar = Calendar.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser
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
        onClick()
        return root
    }

    private fun observe() {
        homeViewModel.scheduleDoctorsList.observe(viewLifecycleOwner) { list ->
            binding.progressBar.visibility=View.GONE
            scheduledAdapter.setData(list)
        }

        homeViewModel.selectedDay.observe(viewLifecycleOwner) { date ->
            daysAdapter.setSelectDay(date.date - 1)
            val dateFormatter = SimpleDateFormat("d MMM yyyy", Locale.ENGLISH)
            binding.monthPickerButton.text = dateFormatter.format(date)
            binding.progressBar.visibility=View.VISIBLE
            homeViewModel.getScheduledDoctorsList(date!!, homeViewModel.selectedCity.value!!)
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

        homeViewModel.startedLocation.observe(viewLifecycleOwner) {
            if (it) {
                binding.startCard.setOnClickListener {
                    if (isNetworkConnected(requireContext())) {
                        getCurrentLocation(requireActivity(), object : LocationListener {
                            override fun getLocation(location: Location?) {
                                if (location != null) {
                                    val startPoint = GeoPoint(location.latitude, location.longitude)
                                    homeViewModel.endLocation(
                                        startPoint,
                                        currentUser!!.uid,
                                        calendar.timeInMillis
                                    )
                                    homeViewModel.startedLocation.value = false
                                    binding.startCard.setCardBackgroundColor(resources.getColor(R.color.colorPrimary))
                                    binding.startTextView.text="Start Location"
                                }
                            }
                        })
                    } else {
                        showNoInternetToast(requireContext())
                    }
                }
            } else {
                binding.startCard.setOnClickListener {
                    if (isNetworkConnected(requireContext())) {
                        getCurrentLocation(requireActivity(), object : LocationListener {
                            override fun getLocation(location: Location?) {
                                if (location != null) {
                                    val startPoint = GeoPoint(location.latitude, location.longitude)
                                    homeViewModel.startLocation(
                                        startPoint,
                                        currentUser!!.uid,
                                        calendar.timeInMillis
                                    )
                                    homeViewModel.startedLocation.value = true
                                    binding.startCard.setCardBackgroundColor(resources.getColor(R.color.red))
                                    binding.startTextView.text="End Location"
                                }
                            }
                        })
                    } else {
                        showNoInternetToast(requireContext())
                    }
                }
            }
        }
    }


    private fun uiInit() {
        userRepository = UserRepository.getInstance()

        if (homeViewModel.selectedDay.value == null) {
            homeViewModel.selectedDay.value = calendar.time
        }
        homeViewModel.getScheduledDoctorsList(
            homeViewModel.selectedDay.value!!,
            homeViewModel.selectedCity.value!!
        )

        if (homeViewModel.startedLocation.value!!) {
            binding.startCard.setCardBackgroundColor(resources.getColor(R.color.red))
        } else {
            binding.startCard.setCardBackgroundColor(resources.getColor(R.color.colorPrimary))
        }

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

        scheduledAdapter = DoctorScheduleAdapter(object : OnSucheduleClickListener {
            override fun onItemClick(
                position: Int,
                doctorModel: DoctorForCompany,
                visitBtn: Boolean
            ) {
                viewModel.selectedDoctor.value = doctorModel.doctorData
                if (visitBtn) {
                    findNavController().navigate(R.id.action_nav_home_to_visitFragment)
                } else {
                    findNavController().navigate(R.id.action_nav_home_to_doctorDetailsFragment)
                }
            }
        })
        binding.scheduleRecycler.adapter = scheduledAdapter

        binding.monthPickerButton.setOnClickListener {
            datePicker()
        }

        binding.LocationBtn.apply {
            text = homeViewModel.selectedCity.value
            setOnClickListener {
                binding.LocationBtn.visibility = View.INVISIBLE
                binding.cityTextInput.visibility = View.VISIBLE
            }
        }

        val cities = resources.getStringArray(R.array.egypt_cities_array)
        val citiesAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, cities)
        binding.autoCompleteCity.setAdapter(citiesAdapter)

        val onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            homeViewModel.selectedCity.value = parent.getItemAtPosition(position) as String
            homeViewModel.getScheduledDoctorsList(
                homeViewModel.selectedDay.value!!,
                homeViewModel.selectedCity.value!!
            )
            binding.cityTextInput.visibility = View.INVISIBLE
            binding.LocationBtn.apply {
                visibility = View.VISIBLE
                text = homeViewModel.selectedCity.value
            }
        }
        binding.autoCompleteCity.onItemClickListener = onItemClickListener

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

    private fun onClick(){
        binding.AddVisitCard.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_doctors)
        }
        binding.AddOrderCard.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_doctors)
        }
    }
}