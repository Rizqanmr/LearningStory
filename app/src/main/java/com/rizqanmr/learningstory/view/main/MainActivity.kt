package com.rizqanmr.learningstory.view.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rizqanmr.learningstory.R
import com.rizqanmr.learningstory.data.repository.Result
import com.rizqanmr.learningstory.databinding.ActivityMainBinding
import com.rizqanmr.learningstory.util.BaseAppCompatActivity
import com.rizqanmr.learningstory.view.ViewModelFactory
import com.rizqanmr.learningstory.view.landing.LandingActivity

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

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                startActivity(Intent(this, LandingActivity::class.java))
                finish()
            }
        }

        setupView()
        setupAction()
    }

    private fun setupView() {
        setupToolbar()
        setStatusBarSolidColor(R.color.sky_blue, true)

        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = storyAdapter
        }

        showListStory()
    }

    private fun setupAction() {
        //
    }

    private fun showListStory() {
        viewModel.getStories().observe(this) {
            if (it != null) {
                when (it) {
                    is Result.Loading -> {
                        showLoading(true)
                    }
                    is Result.Success -> {
                        showLoading(false)
                        storyAdapter.updateItems(it.data.listStory)
                    }
                    is Result.Error -> {
                        showLoading(false)
                        showSnackbarError(it.error)
                    }
                }
            }
        }
    }

    private fun setupToolbar() {
        initToolbar(
            binding.toolbarMain,
            resources.getText(R.string.app_name).toString(),
            R.color.black,
            R.color.black,
            R.color.white,
            R.drawable.ic_arrow_back
        )
        supportActionBar?.elevation = 5f
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
        binding.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSnackbarError(message: String) {
        Snackbar.make(binding.containerMain, message, Snackbar.LENGTH_SHORT).show()
    }
}