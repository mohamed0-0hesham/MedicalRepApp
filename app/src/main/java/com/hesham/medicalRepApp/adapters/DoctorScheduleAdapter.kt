package com.hesham.medicalRepApp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.hesham.medicalRepApp.R
import com.hesham.medicalRepApp.adapters.listener.OnItemClickListener
import com.hesham.medicalRepApp.adapters.listener.OnSucheduleClickListener
import com.hesham.medicalRepApp.databinding.DoctorScheduleItemBinding
import com.hesham.medicalRepApp.models.DoctorForCompany
import com.hesham.medicalRepApp.models.DoctorModel
import com.hesham.medicalRepApp.ui.home.HomeFragment
import org.checkerframework.common.subtyping.qual.Bottom

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