package com.hesham.medicalRepApp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hesham.medicalRepApp.R
import com.hesham.medicalRepApp.adapters.listener.OnDayItemClickListener
import com.hesham.medicalRepApp.databinding.CalendarDayLayoutBinding
import com.hesham.medicalRepApp.models.DayModel
import java.util.Calendar

class DaysAdapter(context: Context?, private val listener: OnDayItemClickListener) :
    RecyclerView.Adapter<DaysAdapter.DaysViewHolder>() {
    private var itemList: List<DayModel> = emptyList()
    private val calendar=Calendar.getInstance()
    private val context=context
    var selectDay: Int=15
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        return DaysViewHolder(
            CalendarDayLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
        holder.binding.dayItemXml = itemList[position]
        if (position==15){
            holder.binding.calendarDayText.setBackgroundColor(context!!.resources.getColor(R.color.colorPrimary))
            holder.binding.textView8.setTextColor(context.resources.getColor(R.color.red))
        }else{
            holder.binding.textView8.setTextColor(context!!.resources.getColor(R.color.black))
        }
//        holder.binding.calendarDayText.setOnClickListener {
//            selectDay=position
////            notifyDataSetChanged();
//        }
        if(selectDay==position){
            holder.binding.calendarDayText.setBackgroundColor(context!!.resources.getColor(R.color.colorSecondaryVariant))
        }
        else {
            holder.binding.calendarDayText.setBackgroundColor(context!!.resources.getColor(R.color.white))
        }
    }

    override fun getItemCount(): Int = itemList.size


    fun setData(itemList: List<DayModel>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }


    inner class DaysViewHolder(var binding: CalendarDayLayoutBinding, listener: OnDayItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition, itemList[bindingAdapterPosition],binding)
            }
        }
    }
}