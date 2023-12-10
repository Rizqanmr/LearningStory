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
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.android.material.snackbar.Snackbar
import com.rizqanmr.learningstory.R
import com.rizqanmr.learningstory.base.BaseAppCompatActivity
import com.rizqanmr.learningstory.databinding.ActivityCreateStoryBinding
import com.rizqanmr.learningstory.view.createstory.CameraActivity.Companion.CAMERAX_RESULT

class CreateStoryActivity : BaseAppCompatActivity() {

    private lateinit var binding: ActivityCreateStoryBinding
    private var currentImageUri: Uri? = null
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }
    }

    private fun setupView() {
        setupToolbar()
        setStatusBarSolidColor(R.color.white, true)
    }

    private fun setupAction() {
        binding.btnGallery.setOnClickListener { openGallery() }
        binding.btnCamera.setOnClickListener { openCamera() }
    }

    private fun setupToolbar() {
        initToolbar(
            binding.toolbarCreateStory,
            "Create Story",
            R.color.black,
            R.color.black,
            R.color.white,
            R.drawable.ic_arrow_back
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.layoutLoading.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSnackbarError(message: String) {
        Snackbar.make(binding.containerCreateStory, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun openGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun openCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Toast.makeText(this, "No media selected", Toast.LENGTH_SHORT).show()
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

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA

        fun startThisActivity(activity: Activity, bundle: Bundle) {
            activity.startActivity(Intent(activity, CreateStoryActivity::class.java).apply {
                putExtras(bundle)
            })
        }
    }
}