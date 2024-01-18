package com.rizqanmr.learningstory.view.createstory

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rizqanmr.learningstory.R
import com.rizqanmr.learningstory.base.BaseAppCompatActivity
import com.rizqanmr.learningstory.databinding.ActivityCreateStoryBinding
import com.rizqanmr.learningstory.util.CommonFunction.reduceFileImage
import com.rizqanmr.learningstory.util.CommonFunction.uriToFile
import com.rizqanmr.learningstory.view.ViewModelFactory
import com.rizqanmr.learningstory.view.createstory.CameraActivity.Companion.CAMERAX_RESULT
import com.rizqanmr.learningstory.view.main.MainActivity

class CreateStoryActivity : BaseAppCompatActivity() {

    private val viewModel by viewModels<CreateStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityCreateStoryBinding
    private var currentImageUri: Uri? = null
    private var isLocationEnable: Boolean = false
    private var latitude = 0.0
    private var longitude = 0.0

    private val cameraPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                openCamera()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()
        subscribeToLiveData()
    }

    private fun setupView() {
        setupToolbar()
        setStatusBarSolidColor(R.color.sky_blue, true)
    }

    private fun setupAction() {
        binding.btnGallery.setOnClickListener { openGallery() }
        binding.btnCamera.setOnClickListener { openCamera() }
        binding.btnUpload.setOnClickListener { uploadImage() }
        binding.cbLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                isLocationEnable = true
                getLocation()
            } else {
                isLocationEnable = false
                latitude = 0.0
                longitude = 0.0
            }
        }
    }

    private fun setupToolbar() {
        initToolbar(
            binding.toolbarCreateStory,
            getString(R.string.create_story),
            R.color.black,
            R.color.black,
            R.color.sky_blue,
            R.drawable.ic_arrow_back
        )
    }

    private fun subscribeToLiveData() {
        viewModel.getIsLoading().observe(this) {
            showLoading(it)
        }
        viewModel.createStoryLiveData().observe(this) {
            handleSuccessCreateStory()
        }
        viewModel.errorCreateStoryLiveData().observe(this) {
            showSnackbar(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.layoutLoading.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(binding.containerCreateStory, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun openGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun openCamera() {
        if (!cameraPermissionsGranted()) {
            cameraPermissionLauncher.launch(CAMERA_PERMISSION)
        } else {
            val intent = Intent(this, CameraActivity::class.java)
            launcherIntentCameraX.launch(intent)
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(this, getString(R.string.no_media_selected), Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERAX_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.ivPreview.setImageURI(it)
        }
    }

    private fun cameraPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            CAMERA_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.edAddDescription.text.toString()

            if (isLocationEnable) {
                viewModel.createStory(imageFile, description, latitude.toString(), longitude.toString())
            } else {
                viewModel.createStory(imageFile, description, null, null)
            }
        } ?: showSnackbar(getString(R.string.empty_image_warning))
    }

    private fun handleSuccessCreateStory() {
        MainActivity.startThisActivity(this, bundleOf())
    }

    private fun getLocation() {
        val fusedLocationProviderClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(this, LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(LOCATION_PERMISSION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isLocationEnable = true
                getLocation()
            } else {
                isLocationEnable = false
            }
        }
    }

    companion object {
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        private const val LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION
        private const val LOCATION_PERMISSION_REQUEST_CODE = 123

        @JvmStatic
        fun startThisActivity(activity: Activity, bundle: Bundle) {
            activity.startActivity(Intent(activity, CreateStoryActivity::class.java).apply {
                putExtras(bundle)
            })
        }
    }
}