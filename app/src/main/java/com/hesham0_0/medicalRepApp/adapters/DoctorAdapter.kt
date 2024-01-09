package com.hesham0_0.medicalRepApp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hesham0_0.medicalRepApp.adapters.listener.OnItemClickListener
import com.hesham0_0.medicalRepApp.databinding.DoctorItemBinding
import com.hesham0_0.medicalRepApp.models.DoctorModel

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
                listener.onItemClick(bindingAdapterPosition, itemList[bindingAdapterPosition],false)
            }
        }
    }
}