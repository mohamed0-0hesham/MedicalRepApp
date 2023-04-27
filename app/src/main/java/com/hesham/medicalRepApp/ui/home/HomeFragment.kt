package com.hesham.medicalRepApp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hesham.medicalRepApp.R
import com.hesham.medicalRepApp.adapters.DaysAdapter
import com.hesham.medicalRepApp.adapters.listener.OnDayItemClickListener
import com.hesham.medicalRepApp.databinding.CalendarDayLayoutBinding
import com.hesham.medicalRepApp.databinding.FragmentHomeBinding
import com.hesham.medicalRepApp.methods.BindingAdapters.Companion.selectDay
import com.hesham.medicalRepApp.models.DayModel
import java.util.Calendar
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var myAdapter :DaysAdapter
    private lateinit var homeViewModel:HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        uiInit()
        return root
    }

    private fun uiInit() {
        myAdapter= DaysAdapter(context,object : OnDayItemClickListener {
            override fun onItemClick(
                position: Int,
                dayItem: DayModel,
                bindingItem: CalendarDayLayoutBinding
            ) {
                if (homeViewModel.selectedDay.value!=dayItem){
                    homeViewModel.selectedDayItem.value=bindingItem
                    homeViewModel.selectedDay.value=dayItem
                    bindingItem.calendarDayText.setBackgroundColor(resources.getColor(R.color.colorSecondary))
                }
            }
        })

        homeViewModel.selectedDayItem.observe(viewLifecycleOwner) { layout ->
            layout.calendarDayText.setBackgroundColor(resources.getColor(R.color.colorSecondary))
            if (homeViewModel.lastSelectedDayItem.value!=null){
                homeViewModel.lastSelectedDayItem.value!!.calendarDayText
                    .setBackgroundColor(resources.getColor(R.color.white))
            }

            homeViewModel.lastSelectedDayItem.value=layout
        }

        binding.daysRecyclerView.adapter = myAdapter
        getDays()
        binding.daysRecyclerView.scrollToPosition(13)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getDays() {
        val list = ArrayList<DayModel>()
        val cal = Calendar.getInstance()
        val today = cal.get(Calendar.DAY_OF_MONTH)
        for (i in -15..15) {
            cal.set(Calendar.DAY_OF_MONTH, today + i)
            if (cal.get(Calendar.DAY_OF_MONTH) == 1) {
                for (k in 1..15 - i) {
                    cal.set(Calendar.DAY_OF_MONTH, k)
                    val dayModel = DayModel(
                        dayOfMonth = cal.get(Calendar.DAY_OF_MONTH),
                        dayOfWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK,0,Locale.ENGLISH),
                        month =cal.getDisplayName(Calendar.MONTH,0,Locale.ENGLISH),
                        year =cal.get(Calendar.YEAR),
                    )
                    list.add(dayModel)
                }
                myAdapter.setData(list)
                return
            }
            val dayModel = DayModel(
                dayOfMonth = cal.get(Calendar.DAY_OF_MONTH),
                dayOfWeek = cal.getDisplayName(Calendar.DAY_OF_WEEK,0,Locale.ENGLISH),
                month =cal.getDisplayName(Calendar.MONTH,0,Locale.ENGLISH),
                year =cal.get(Calendar.YEAR),
            )
            list.add(dayModel)
        }
    }

//        cal.set(Calendar.DAY_OF_MONTH,0)
//        cal.getMaximum(Calendar.DAY_OF_MONTH)
//        cal.getMinimum(Calendar.DAY_OF_MONTH)
}