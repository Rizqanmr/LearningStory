package com.rizqanmr.learningstory.view.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.bundleOf
import androidx.core.util.Pair
import androidx.core.view.isVisible
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rizqanmr.learningstory.R
import com.rizqanmr.learningstory.data.model.response.StoryItemResponse
import com.rizqanmr.learningstory.databinding.ActivityMainBinding
import com.rizqanmr.learningstory.databinding.ItemStoryBinding
import com.rizqanmr.learningstory.base.BaseAppCompatActivity
import com.rizqanmr.learningstory.base.BaseListItem
import com.rizqanmr.learningstory.view.ViewModelFactory
import com.rizqanmr.learningstory.view.createstory.CreateStoryActivity
import com.rizqanmr.learningstory.view.landing.LandingActivity
import com.rizqanmr.learningstory.view.map.MapsActivity
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

        setupView()
        setupAction()
        viewModel.getStories()
        subscribeToLiveData()
    }

    private fun setupView() {
        setupToolbar()
        setStatusBarSolidColor(R.color.sky_blue, true)

        storyAdapter = StoryAdapter()
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter { storyAdapter.retry() }
            )
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
    }

    private fun setupAction() {
        storyAdapter.setStoryListener(object : StoryAdapter.StoryListener {
            override fun onItemClick(itemStory: ItemStoryBinding, itemResponse: StoryItemResponse) {
                val optionCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@MainActivity,
                        Pair(itemStory.ivItemPhoto, "item_photo"),
                        Pair(itemStory.tvItemName, "item_name"),
                        Pair(itemStory.tvDescription, "item_desc")
                    )
                StoryDetailActivity.startThisActivity(this@MainActivity, optionCompat.toBundle()
                    ?.apply {
                        putString("EXTRA_ID_STORY", itemResponse.id)
                    })
            }
        })

        binding.fabCreate.setOnClickListener {
            CreateStoryActivity.startThisActivity(this@MainActivity, bundleOf())
        }
    }

    private fun setupToolbar() {
        initToolbar(
            binding.toolbarMain,
            getString(R.string.app_name),
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
            R.id.menu_map -> {
                MapsActivity.startThisActivity(this, bundleOf())
                true
            }
            R.id.menu_setting -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
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

    private fun showListStory(list: PagingData<BaseListItem>) {
        storyAdapter.submitData(lifecycle, list)
    }

    companion object {
        @JvmStatic
        fun startThisActivity(activity: Activity, bundle: Bundle) {
            activity.startActivity(Intent(activity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtras(bundle)
            })
        }
    }
}