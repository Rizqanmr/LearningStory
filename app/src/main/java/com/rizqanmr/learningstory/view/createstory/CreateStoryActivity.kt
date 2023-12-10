package com.rizqanmr.learningstory.view.createstory

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import com.rizqanmr.learningstory.R
import com.rizqanmr.learningstory.base.BaseAppCompatActivity
import com.rizqanmr.learningstory.databinding.ActivityCreateStoryBinding

class CreateStoryActivity : BaseAppCompatActivity() {

    private lateinit var binding: ActivityCreateStoryBinding
    private var currentImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()
    }

    private fun setupView() {
        setupToolbar()
        setStatusBarSolidColor(R.color.white, true)
    }

    private fun setupAction() {
        binding.btnGallery.setOnClickListener { openGallery() }
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

    private fun showImage() {
        currentImageUri?.let {
            binding.ivPreview.setImageURI(it)
        }
    }

    companion object {
        fun startThisActivity(activity: Activity, bundle: Bundle) {
            activity.startActivity(Intent(activity, CreateStoryActivity::class.java).apply {
                putExtras(bundle)
            })
        }
    }
}