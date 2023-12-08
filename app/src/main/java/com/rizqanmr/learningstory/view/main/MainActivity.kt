package com.rizqanmr.learningstory.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rizqanmr.learningstory.R
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse
import com.rizqanmr.learningstory.databinding.ActivityMainBinding
import com.rizqanmr.learningstory.databinding.ItemStoryBinding
import com.rizqanmr.learningstory.base.BaseAppCompatActivity
import com.rizqanmr.learningstory.view.ViewModelFactory
import com.rizqanmr.learningstory.view.landing.LandingActivity
import com.rizqanmr.learningstory.view.storydetail.StoryDetailActivity

class MainActivity : BaseAppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding

    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        storyAdapter = StoryAdapter()

        setupView()
        setupAction()
        viewModel.getStories()
        subscribeToLiveData()
    }

    private fun setupView() {
        setupToolbar()
        setStatusBarSolidColor(R.color.sky_blue, true)

        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = storyAdapter
        }
    }

    private fun subscribeToLiveData() {
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LandingActivity::class.java))
                finish()
            }
        }
        viewModel.getIsLoading().observe(this) {
            showLoading(it)
        }
        viewModel.listStoryLiveData().observe(this) {
            showListStory(it)
        }
        viewModel.errorListStoryLiveData().observe(this) {
            handleError(it)
        }
    }

    private fun setupAction() {
        storyAdapter.setStoryListener(object : StoryAdapter.StoryListener {
            override fun onItemClick(view: ItemStoryBinding, position: Int, item: StoryItemResponse) {
                val optionCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@MainActivity,
                        Pair(view.ivItemPhoto, "item_photo"),
                        Pair(view.tvItemName, "item_name"),
                        Pair(view.tvDescription, "item_desc")
                    )
                StoryDetailActivity.startThisActivity(this@MainActivity, optionCompat.toBundle()
                    ?.apply {
                        putString("EXTRA_ID_STORY", item.id)
                    })
            }
        })
    }

    private fun setupToolbar() {
        initToolbar(
            binding.toolbarMain,
            resources.getText(R.string.app_name).toString(),
            R.color.black,
            R.color.black,
            R.color.sky_blue,
            R.drawable.ic_arrow_back
        )
        binding.toolbarMain.btnBack.isVisible = false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_setting -> {
                // go to setting page
                true
            }
            R.id.menu_logout -> {
                viewModel.logout()
                true
            }
            else -> true
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.layoutLoading.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSnackbarError(message: String) {
        Snackbar.make(binding.containerMain, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showListStory(list: List<StoryItemResponse>) {
        if (list.isNotEmpty()) {
            binding.rvStory.isVisible = true
            storyAdapter.updateItems(list)
        } else {
            binding.rvStory.isVisible = false
            showErrorEmptyLayout(binding.layoutError, layoutType = LayoutType.EMPTY_LIST)
        }
    }

    private fun handleError(error: String) {
        showSnackbarError(error)
        showErrorEmptyLayout(binding.layoutError, layoutType = LayoutType.ERROR)
        binding.layoutError.btnRefresh.setOnClickListener { viewModel.getStories() }
    }
}