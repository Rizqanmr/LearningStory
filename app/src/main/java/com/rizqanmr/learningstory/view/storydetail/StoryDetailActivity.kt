package com.rizqanmr.learningstory.view.storydetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.rizqanmr.learningstory.R
import com.rizqanmr.learningstory.data.model.response.StoryDetailResponse
import com.rizqanmr.learningstory.databinding.ActivityStoryDetailBinding
import com.rizqanmr.learningstory.base.BaseAppCompatActivity
import com.rizqanmr.learningstory.view.ViewModelFactory

class StoryDetailActivity : BaseAppCompatActivity() {

    private val viewModel by viewModels<StoryDetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityStoryDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initExtraData(intent)
        setupView()
        viewModel.getStoryDetail()
        subscribeToLiveData()
    }

    private fun setupView() {
        setupToolbar()
        setStatusBarSolidColor(R.color.white, true)
    }

    private fun setupToolbar() {
        initToolbar(
            binding.toolbarDetail,
            "Detail Story",
            R.color.black,
            R.color.black,
            R.color.white,
            R.drawable.ic_arrow_back
        )
    }

    private fun initExtraData(intent: Intent) {
        viewModel.setStoryId(intent.getStringExtra("EXTRA_ID_STORY").orEmpty())
    }

    private fun subscribeToLiveData() {
        viewModel.getIsLoading().observe(this) {
            showLoading(it)
        }
        viewModel.storyDetailLiveData().observe(this) {
            if (it != null) {
                showStoryDetail(it)
            }
        }
        viewModel.errorStoryDetailLiveData().observe(this) {
            showSnackbarError(it)
        }
    }

    private fun showStoryDetail(data: StoryDetailResponse) {
        with(binding) {
            tvDetailName.text = data.story?.name
            tvDetailCreated.text = getString(R.string.created_at, viewModel.getDate(data.story?.createdAt.toString()))
            tvDetailDescription.text = data.story?.description
            Glide.with(root.context)
                .load(data.story?.photoUrl)
                .apply(RequestOptions.placeholderOf(R.drawable.ic_loading).error(R.drawable.ic_error))
                .into(ivDetailPhoto)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.layoutLoading.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSnackbarError(message: String) {
        Snackbar.make(binding.containerStoryDetail, message, Snackbar.LENGTH_SHORT).show()
    }

    companion object {
        @JvmStatic
        fun startThisActivity(activity: Activity, bundle: Bundle?) {
            activity.startActivity(Intent(activity, StoryDetailActivity::class.java).apply {
                bundle?.let { putExtras(it) }
            })
        }
    }
}