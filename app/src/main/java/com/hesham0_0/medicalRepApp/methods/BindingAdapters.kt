package com.hesham0_0.medicalRepApp.methods

import android.os.Build
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.hesham0_0.medicalRepApp.R
import com.hesham0_0.medicalRepApp.methods.Utilities.Companion.getFormattedDate
import com.kizitonwose.calendar.core.daysOfWeek
import java.util.*

class BindingAdapters {
    companion object {
        @BindingAdapter("getImageByGlide")
        @JvmStatic
        fun getImageByGlide(view: ImageView, image_url: String?) {
            if (image_url != null) {
                Glide.with(view.context)
                    .load(image_url)
                    .placeholder(R.drawable.doctor_svgrepo_com)
                    .error(R.drawable.doctor_svgrepo_com)
                    .into(view)
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @BindingAdapter("daysBinding")
        @JvmStatic
        fun daysBinding(view: TextView, daysList: List<Int>?) {
            val weekList = daysOfWeek()
            var text = ""
            daysList?.let {
                for (i in daysList) {
                    for (x in weekList) {
                        if (i == x.value) {
                            text += "${x.name[0]}" + "${x.name[1]}" + "${x.name[2]} "
                        }
                    }
                }
            }
            view.text = text
        }

        @BindingAdapter("getDate")
        @JvmStatic
        fun getDate(view: TextView, date: String?) {
            if (date != null) {
                view.text = getFormattedDate(date).toString()
            }
        }

        @BindingAdapter("capitalizeName")
        @JvmStatic
        fun capitalizeName(view: TextView, name: String?) {
            view.text = name?.let {
                if (it.isNotEmpty()) {
                    it.substring(0, 1).uppercase(Locale.ROOT) + it.substring(1)
                } else {
                    ""
                }
            }
        }

//        @SuppressLint("ResourceAsColor")
//        @BindingAdapter("selectDayBinding")
//        @JvmStatic
//        fun selectDayBinding(view: ConstraintLayout, day: Int?) {
//            when (day) {
//                calendar.get(Calendar.DAY_OF_MONTH) -> view.setBackgroundColor(R.color.colorSecondaryVariant)
//                selectDay -> view.setBackgroundColor(R.color.red)
//                else -> view.setBackgroundColor(R.color.red)
//            }
//        }
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