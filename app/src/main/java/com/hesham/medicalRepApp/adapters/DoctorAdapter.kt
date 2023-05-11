package com.hesham.medicalRepApp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hesham.medicalRepApp.R
import com.hesham.medicalRepApp.adapters.listener.OnItemClickListener
import com.hesham.medicalRepApp.databinding.DoctorItemBinding
import com.hesham.medicalRepApp.databinding.DoctorScheduleItemBinding
import com.hesham.medicalRepApp.methods.Utilities.Companion.DOCTORS_RECYCLER
import com.hesham.medicalRepApp.models.DoctorModel

class DoctorAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<DoctorAdapter.MyViewHolder>() {
    private var itemList: List<DoctorModel> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            DoctorItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ), listener
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.itemXml = itemList[position]
    }

    override fun getItemCount(): Int = itemList.size


    fun setData(itemList: List<DoctorModel>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }


    inner class MyViewHolder(var binding: DoctorItemBinding, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition, itemList[adapterPosition])
            }
        }
    }
}