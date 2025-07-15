package com.example.foodbook.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.foodbook.R
import com.example.foodbook.databinding.FragmentAddNewMealBinding
import com.example.foodbook.pojo.MyMeals
import com.example.foodbook.viewModel.MyMealsViewModel

class AddNewMealFragment : Fragment() {

    private lateinit var binding: FragmentAddNewMealBinding

    private var imageUri: Uri? = null

    private val viewModelMyMeal: MyMealsViewModel by activityViewModels()


    val pickImageLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            binding.resultImage.setImageURI(it)
            if (it != null) {
                requireActivity().contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            imageUri = it
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddNewMealBinding.inflate(inflater)
        setupAddMealImageButton()

        binding.btnAdd.setOnClickListener {
            val meal = MyMeals(
                binding.edName.text.toString(),
                binding.edInstructions.text.toString(),
                imageUri
            )

            //MealManager.add(meal)
            viewModelMyMeal.addMeal(meal)
            findNavController().navigate(R.id.action_addNewMealFragment_to_myMealsFragment)

        }

        binding.resultImage.setOnClickListener {
            pickImageLauncher.launch(arrayOf("image/*"))
        }





        return binding.root
    }

    private val CAMERA_REQUEST_CODE = 2


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    private fun setupAddMealImageButton() {
        binding.btnAddMealImg.setOnClickListener {
            showImageSourceDialog()
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Gallery", "Camera")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select an image source")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> {
                        // בחירת תמונה מהגלריה
                        val galleryIntent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        startActivityForResult(galleryIntent, 1)
                    }

                    1 -> {
                        // בחירת תמונה מהמצלמה
                        if (ContextCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            openCamera()
                        } else {
                            requestCameraPermission()
                        }
                    }
                }
            }
        builder.show()
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, 2)
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // הרשאה למצלמה אושרה
                    openCamera()
                } else {
                    // המשתמש סירב להעניש את הרשאת המצלמה
                    // ניתן להוסיף טיפול נוסף כגון הודעה למשתמש או פעולה נוספת
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    // Image from gallery
                    data?.data?.let { uri ->
                        imageUri = uri
                    }
                }

                2 -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap?
                    binding.resultImage.setImageBitmap(imageBitmap)
                }
            }
        }
    }
}





