package com.hesham0_0.medicalRepApp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.hesham0_0.medicalRepApp.R
import com.hesham0_0.medicalRepApp.adapters.listener.OnSucheduleClickListener
import com.hesham0_0.medicalRepApp.databinding.DoctorScheduleItemBinding
import com.hesham0_0.medicalRepApp.models.DoctorForCompany

class DoctorScheduleAdapter(private val listener: OnSucheduleClickListener) :
    RecyclerView.Adapter<DoctorScheduleAdapter.MyViewHolder>() {
    private var itemList: List<DoctorForCompany> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DoctorScheduleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.itemXml = itemList[position]
    }

    override fun getItemCount(): Int = itemList.size


    fun setData(itemList: List<DoctorForCompany>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }


    inner class MyViewHolder(var binding: DoctorScheduleItemBinding, listener: OnSucheduleClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(bindingAdapterPosition, itemList[bindingAdapterPosition],false)
            }
            itemView.findViewById<Button>(R.id.visitButton).setOnClickListener {
                listener.onItemClick(bindingAdapterPosition, itemList[bindingAdapterPosition],true)
            }
        }
    }
}