package com.hesham0_0.medicalRepApp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hesham0_0.medicalRepApp.R
import com.hesham0_0.medicalRepApp.adapters.listener.OnDayItemClickListener
import com.hesham0_0.medicalRepApp.databinding.CalendarDayLayoutBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class DaysAdapter(context: Context, private val listener: OnDayItemClickListener) :
    RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {
    private var itemList: List<Date> = emptyList()
    private var itemSelected: CalendarDayLayoutBinding? =null
    private val date = Calendar.getInstance().time
    private val context = context
    private var selectDay: Int = date.date - 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        return DaysViewHolder(
            CalendarDayLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        val dayOfWeek = SimpleDateFormat("EEE").format(itemList[position])
        val dayOfMonth = SimpleDateFormat("d").format(itemList[position])
        holder.binding.dayOfWeek.text = dayOfWeek
        holder.binding.dayOfMonth.text = dayOfMonth

        if (date.date == itemList[position].date && date.month == itemList[position].month) {
            holder.binding.dayOfMonth.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.dayOfWeek.setTextColor(context.resources.getColor(R.color.red))

        } else {
            holder.binding.dayOfMonth.setTextColor(context.resources.getColor(R.color.colorPrimaryVariant))
            holder.binding.dayOfWeek.setTextColor(context.resources.getColor(R.color.colorPrimaryVariant))
        }

        if (selectDay == position) {
            holder.binding.calendarDayText.setBackgroundColor(context.resources.getColor(R.color.colorPrimary))
            holder.binding.dayOfMonth.setTextColor(context.resources.getColor(R.color.backgroundColor))
            holder.binding.dayOfWeek.setTextColor(context.resources.getColor(R.color.backgroundColor))
            itemSelected=holder.binding
        } else {
            holder.binding.calendarDayText.setBackgroundColor(context.resources.getColor(R.color.white))
        }
    }

    override fun getItemCount(): Int = itemList.size


    fun setData(itemList: List<Date>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    fun setSelectDay(selectDay: Int) {
        this.selectDay = selectDay
    }

    fun getLayout(): CalendarDayLayoutBinding? {
        return itemSelected
    }

    inner class DaysViewHolder(
        var binding: CalendarDayLayoutBinding,
        listener: OnDayItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                selectDay = bindingAdapterPosition
                listener.onItemClick(
                    bindingAdapterPosition,
                    itemList[bindingAdapterPosition],
                    binding
                )
            }
        }
    }

}
