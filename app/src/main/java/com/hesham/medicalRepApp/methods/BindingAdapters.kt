package com.hesham.medicalRepApp.methods

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.hesham.medicalRepApp.R
import com.kizitonwose.calendar.core.daysOfWeek
import java.time.DayOfWeek

class BindingAdapters {

    companion object{
        val database = FirebaseDatabase.getInstance()
        val doctorsRef = database.getReference("Doctors")
        val ref = database.reference

        @BindingAdapter("imageUrl")
        @JvmStatic
        fun getImageByGlide(view: ImageView, image_url: String?) {
            if (image_url != null) {
                Glide.with(view.context)
                    .load(image_url)
                    .placeholder(R.drawable.loading_01_svgrepo_com)
                    .error(R.drawable.error_svgrepo_com)
                    .into(view)
            }
        }
        @RequiresApi(Build.VERSION_CODES.O)
        @BindingAdapter("daysBinding")
        @JvmStatic
        fun daysBinding(view: TextView, daysList:List<Int>?) {
            val weekList = daysOfWeek()
            var text=""
            daysList?.let {
                for (i in daysList) {
                    for (x in weekList) {
                        if (i == x.value) {
                            text += "${x.name[0]}"+"${x.name[1]}"+"${x.name[2]} "
                        }
                    }
                }
            }
            view.text=text
        }

        fun updateNavHeader(header: View) {
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser!!
            header.findViewById<TextView>(R.id.headerUserEmail).text = user.email
            header.findViewById<TextView>(R.id.headerUserName).text = user.displayName
            header.findViewById<TextView>(R.id.headerUserPhone).text = user.phoneNumber
            getImageByGlide(header.findViewById(R.id.headerUserImage),user.photoUrl.toString() )
        }

        fun createChip(name: String,id:Int, context: Context): Chip {
            val chip = Chip(context)
            chip.text = name
            chip.id = id
//            chip.setBackgroundResource(R.color.colorPrimaryVariant)
            chip.isClickable = true
            chip.isCheckable = true
            return chip
        }
//        @BindingAdapter("addGenreChip")
//        @JvmStatic
//        fun addChip(view: ChipGroup, list: List<String>?) {
//            if (list != null) {
//                for (id in list) {
//                    for (genre in daysList) {
//                        if (genre.id == genreId) {
//                            val chip = createChip(genre, view.context)
//                            chip.isClickable = false
//                            chip.isCheckable = false
//                            view.addView(chip as View)
//                        }
//                    }
//                }
//            }
//        }
    }
}