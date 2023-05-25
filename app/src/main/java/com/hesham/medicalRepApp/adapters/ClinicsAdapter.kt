package com.hesham.medicalRepApp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hesham.medicalRepApp.adapters.listener.OnDayItemClickListener
import com.hesham.medicalRepApp.adapters.listener.OnItemClickListener
import com.hesham.medicalRepApp.databinding.CalendarDayLayoutBinding
import com.hesham.medicalRepApp.databinding.ClinicVisitItemBinding
import com.hesham.medicalRepApp.databinding.DoctorItemBinding
import com.hesham.medicalRepApp.models.DoctorClinic
import java.util.*

class ClinicsAdapter(context: Context
//                     ,private val listener: OnDayItemClickListener
                     ) :
    RecyclerView.Adapter<ClinicsAdapter.ClinicsViewHolder>() {
    private var itemList: List<DoctorClinic> = emptyList()
    private var itemSelected: ClinicVisitItemBinding? =null
    private val date = Calendar.getInstance().time
    private val context = context
    private var selectDay: Int = date.date - 1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClinicsAdapter.ClinicsViewHolder {
        return ClinicsViewHolder(
            ClinicVisitItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ClinicsAdapter.ClinicsViewHolder, position: Int) {
        holder.binding.itemXml=itemList[position]
    }

    override fun getItemCount(): Int = itemList.size

    fun setData(itemList: List<DoctorClinic>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }

    inner class ClinicsViewHolder(var binding: ClinicVisitItemBinding
//    , listener: OnDayItemClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
//                listener.onItemClick(bindingAdapterPosition, itemList[bindingAdapterPosition],false)
            }
        }
    }
}