package com.hesham.medicalRepApp.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.hesham.medicalRepApp.data.UserRepository
import com.hesham.medicalRepApp.databinding.FragmentProfileBinding
import com.hesham.medicalRepApp.methods.Utilities
import com.hesham.medicalRepApp.methods.Utilities.Companion.PICK_IMAGE_REQUEST

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var profileViewModel: ProfileViewModel
    private val binding get() = _binding!!
    private var userRepository=UserRepository.getInstance()
    private val authUser=FirebaseAuth.getInstance().currentUser
    private var bitmap:Bitmap?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        profileViewModel = ViewModelProvider(this)[ProfileViewModel::class.java]
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            val selectedImageUri: Uri? = data.data
            if (selectedImageUri != null) {
                bitmap = Utilities.getBitmapFromUri(
                    selectedImageUri,
                    requireActivity().contentResolver,
                    500,
                    500
                )
                binding.profileImage.setImageBitmap(bitmap)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun initUi(){
        binding.itemXml = userRepository.currentUser
        binding.profileImage.setOnClickListener {
            selectPhoto()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }
}