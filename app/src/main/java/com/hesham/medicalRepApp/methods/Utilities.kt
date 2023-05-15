package com.hesham.medicalRepApp.methods

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.google.android.material.chip.Chip
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.hesham.medicalRepApp.R
import com.hesham.medicalRepApp.models.DoctorModel
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class Utilities {
    companion object {
        val calendar = Calendar.getInstance()
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        private const val LOCATION_REQUEST_CODE = 100
        const val DOCTORS_COLLECTION = "Doctors"
        const val PHOTO_URL = "photoUrl"
        val DOCTORS_RECYCLER = "DoctorsFragment"
        val DOCTOR_SCHEDULE = "SCHEDULE"
        const val LAST_VISIT = "lastVisit"
        const val DOCTOR_DAYS = "days"
        const val CITY = "city"

        fun colorStatusBarIcons(window: Window, color: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                window.statusBarColor = Color.WHITE
            } else {
                window.statusBarColor = color
            }
        }

        fun updateNavHeader(header: View) {
            val auth = FirebaseAuth.getInstance()
            val user = auth.currentUser!!
            header.findViewById<TextView>(R.id.headerUserEmail).text = user.email
            header.findViewById<TextView>(R.id.headerUserName).text = user.displayName
            header.findViewById<TextView>(R.id.headerUserPhone).text = user.phoneNumber
            BindingAdapters.getImageByGlide(
                header.findViewById(R.id.headerUserImage),
                user.photoUrl.toString()
            )
        }

        fun getFormattedDate(time: String): String? {
            calendar.timeInMillis = time.toLong()
            val dateFormatter = SimpleDateFormat("dd MMM yyyy")
            return dateFormatter.format(calendar.time)
        }

        fun showAlert(activity: Activity) {
            val dialog = AlertDialog.Builder(activity)
            dialog.setView(
                activity.layoutInflater.inflate(
                    R.layout.pick_time_dialog,
                    null
                )
            )
            dialog.create()

            dialog.show()
        }

        fun createChip(name: String, id: Int, context: Context): Chip {
            val chip = Chip(context)
            chip.text = name
            chip.id = id
//            chip.setBackgroundResource(R.color.colorPrimaryVariant)
            chip.isClickable = true
            chip.isCheckable = true
            return chip
        }

        //    public void sentNotification(){
        //        String topic = "highScores";
        //        Bundle bundle = new Bundle();
        //
        //        Message message = new RemoteMessage(bundle);
        //        FirebaseMessaging.getInstance().send(message);
        //        System.out.println("Successfully sent message: ");
        //    }
        fun getBitmapFromUri(
            uri: Uri,
            contentResolver: ContentResolver,
            maxWidth: Int,
            maxHeight: Int
        ): Bitmap? {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, options)

            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight)
            options.inJustDecodeBounds = false
            return BitmapFactory.decodeStream(contentResolver.openInputStream(uri), null, options)
        }

        private fun calculateInSampleSize(
            options: BitmapFactory.Options,
            reqWidth: Int,
            reqHeight: Int
        ): Int {
            val height = options.outHeight
            val width = options.outWidth
            var inSampleSize = 1

            if (height > reqHeight || width > reqWidth) {
                val heightRatio = height.toFloat() / reqHeight.toFloat()
                val widthRatio = width.toFloat() / reqWidth.toFloat()
                inSampleSize = Math.ceil(Math.min(heightRatio, widthRatio).toDouble()).toInt()
            }

            return inSampleSize
        }

        fun uploadToStorage(bitmap: Bitmap?, doctor: DoctorModel) {
            val db = FirebaseFirestore.getInstance()
            if (bitmap != null) {
                val riversRef: StorageReference = storageRef.child("images/" + bitmap.toString())
                val byteStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteStream)
                val photo = byteStream.toByteArray()
                val uploadTask = riversRef.putBytes(photo)
                uploadTask.addOnCompleteListener { task: Task<UploadTask.TaskSnapshot> ->
                    if (task.isSuccessful) {
                        Log.d("Storage", "onSuccess " + task.result)
                        storageRef.child("images/" + bitmap.toString()).downloadUrl
                            .addOnSuccessListener { uri ->
                                db.collection(DOCTORS_COLLECTION)
                                    .document(doctor.name + doctor.phoneNum)
                                    .update(PHOTO_URL, uri.toString())
                            }
                    } else {
                        Log.d("Storage", "onFailure" + task.exception!!.message)
                    }
                }
            }
        }

        fun startLocation(db: FirebaseFirestore?, activity: Activity) {
            checkPermissions(activity)
            val fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(activity)
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val geoPoint = GeoPoint(location.latitude, location.longitude)
                        if (FirebaseAuth.getInstance().currentUser != null) {
//                            updateCurrentUserFromDb(db, "userLocation", geoPoint)
                        }
                    }
                }
            } else {
                ActivityCompat.requestPermissions(
                    activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            }
        }

        fun showSettingAlert(context: Context) {
            val alertDialog = AlertDialog.Builder(context)
            alertDialog.setTitle("GPS setting!")
            alertDialog.setMessage("GPS is not enabled, Do you want to go to settings menu? ")
            alertDialog.setPositiveButton("Setting") { dialog, which ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }
            alertDialog.setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
            alertDialog.show()
        }

        fun checkPermissions(activity: Activity) {
            LocationServices.getFusedLocationProviderClient(activity)
            if (ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            }
            val locationManager =
                activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showSettingAlert(activity)
            }
        }
    }
}