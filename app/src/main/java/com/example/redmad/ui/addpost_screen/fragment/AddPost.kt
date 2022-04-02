package com.example.redmad.ui.addpost_screen.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.redmad.AddPostScreenInterface
import com.example.redmad.R
import com.example.redmad.data.viewmodel.AppViewModel
import com.example.redmad.data.viewmodel.AppViewModelFactory
import com.example.redmad.databinding.FragmentAddPostBinding
import com.example.redmad.ui.MainApplication
import com.example.redmad.utils.UploadStreamRequestBody
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import java.io.File
import java.util.*


class AddPost : Fragment() {

    private lateinit var binding: FragmentAddPostBinding
    private lateinit var appViewModel: AppViewModel
    private lateinit var bitmap: Bitmap
    private lateinit var selectedImage: Uri
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var isLocEnabled: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPostBinding.inflate(inflater, container, false)

        val repository = (activity?.application as MainApplication).appRepository
        appViewModel =
            ViewModelProvider(this, AppViewModelFactory(repository)).get(AppViewModel::class.java)
        setButtonActions()

        makeInvisible()

        if (isLocEnabled) {
            binding.locButton.setImageResource(R.drawable.loc_enabled)
        } else {
            binding.locButton.setImageResource(R.drawable.location_to_add_post)
        }

        return binding.root
    }

    private fun makeInvisible() {
        binding.deleteImage.visibility = View.GONE
        binding.blockForImage.visibility = View.GONE
    }

    private fun makeVisible() {
        binding.deleteImage.visibility = View.VISIBLE
        binding.blockForImage.visibility = View.VISIBLE
    }

    private fun pickImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            selectedImage = data?.data!!

            bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver, selectedImage)

            Glide.with(binding.blockForImage.context)
                .load(bitmap)
                .placeholder(R.drawable.back_for_add_image)
                .apply(RequestOptions.bitmapTransform(RoundedCorners(35)))
                .centerCrop()
                .into(binding.blockForImage)

            makeVisible()
        }
    }

    private fun setButtonActions() {

        val addPostScreenInterface = requireActivity() as AddPostScreenInterface

        binding.imageButton.setOnClickListener {
            pickImageGallery()
        }

        binding.deleteImage.setOnClickListener {
            makeInvisible()
            binding.blockForImage.setImageURI(null)
        }

        binding.sendButton.setOnClickListener {
            if (binding.textField.text.toString().isNotEmpty()) {
                lifecycleScope.launch {
                    try {
                        val stream = context?.contentResolver?.openInputStream(selectedImage)
                            ?: return@launch

                        val request =
                            RequestBody.create("image/*".toMediaTypeOrNull(), stream.readBytes())

                        val filePart =
                            MultipartBody.Part.createFormData("image_file", "image.jpg", request)

                        if (isLocEnabled) {
                            val task = fusedLocationProviderClient.lastLocation

                            if (ActivityCompat.checkSelfPermission(
                                    requireContext(),
                                    android.Manifest.permission.ACCESS_FINE_LOCATION
                                )
                                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                    requireContext(),
                                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                ActivityCompat.requestPermissions(
                                    requireActivity(),
                                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                                    101
                                )
                            }
                            task.addOnSuccessListener {
                                if (it != null) {
                                    val geocoder: Geocoder =
                                        Geocoder(requireContext(), Locale.getDefault())
                                    lifecycleScope.launch {
                                        val _result = runCatching {
                                            appViewModel.addMePost(
                                                binding.textField.text.toString(),
                                                it.longitude,
                                                it.latitude,
                                                filePart
                                            )
                                        }
                                        _result.onSuccess {
                                            addPostScreenInterface.closeAddPostScreen()
                                            selectedImage = Uri.EMPTY
                                            binding.textField.setText("")
                                            isLocEnabled = false
                                        }
                                        _result.onFailure {
                                            val animation = AnimationUtils.loadAnimation(context, R.anim.move)
                                            binding.sendButton.startAnimation(animation)
                                        }
                                    }
                                }
                            }

                        } else {
                            val _result = runCatching {
                                appViewModel.addMePost(
                                    binding.textField.text.toString(),
                                    0.0,
                                    0.0,
                                    filePart
                                )
                            }
                            _result.onSuccess {
                                addPostScreenInterface.closeAddPostScreen()
                                selectedImage = Uri.EMPTY
                                binding.textField.setText("")
                                isLocEnabled = false
                            }
                        }

                    } catch (e: Exception) {
                        val animation = AnimationUtils.loadAnimation(context, R.anim.move)
                        binding.sendButton.startAnimation(animation)
                    }
                }
            } else {
                val animation = AnimationUtils.loadAnimation(context, R.anim.move)
                binding.sendButton.startAnimation(animation)
            }
        }

        binding.locButton.setOnClickListener {

            fusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(requireActivity())

            if (isLocEnabled) {
                isLocEnabled = false
                binding.locButton.setImageResource(R.drawable.location_to_add_post)
            } else {
                isLocEnabled = true
                binding.locButton.setImageResource(R.drawable.loc_enabled)
            }

        }

        binding.exitArrow.setOnClickListener {
            addPostScreenInterface.closeAddPostScreen()
        }

    }

}