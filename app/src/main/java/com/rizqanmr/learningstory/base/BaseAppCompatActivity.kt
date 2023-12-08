package com.rizqanmr.learningstory.base

import android.graphics.PorterDuff
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.rizqanmr.learningstory.R
import com.rizqanmr.learningstory.databinding.LayoutErrorEmptyBinding
import com.rizqanmr.learningstory.databinding.LayoutToolbarBinding

abstract class BaseAppCompatActivity : AppCompatActivity() {

    enum class LayoutType(val value: String) {
        NO_INTERNET("no_internet"),
        EMPTY_LIST("empty_list"),
        ERROR("error")
    }

    fun initToolbar(
        binding: LayoutToolbarBinding,
        title: String,
        @ColorRes textColor: Int,
        @ColorRes btnBackTint: Int,
        @ColorRes bgToolbarColor: Int,
        @DrawableRes btnBackR: Int
    ) {
        binding.tvTitle.text = title
        binding.tvTitle.setTextColor(ContextCompat.getColor(this, textColor))
        binding.btnBack.setImageResource(btnBackR)
        binding.btnBack.setColorFilter(
            ContextCompat.getColor(this, btnBackTint),
            PorterDuff.Mode.SRC_IN
        )
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.appBarBase.setBackgroundColor(ContextCompat.getColor(this, bgToolbarColor))

        setSupportActionBar(binding.toolbarBase)
    }

    fun setStatusBarSolidColor(@ColorRes color: Int, isLightStatusBar: Boolean) {
        window?.let {
            it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (isLightStatusBar) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    it.decorView.windowInsetsController?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    it.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
            it.statusBarColor = ContextCompat.getColor(this, color)
        }
    }

    fun showErrorEmptyLayout(
        binding: LayoutErrorEmptyBinding,
        layoutType: LayoutType
    ) {
        with(binding) {
            when (layoutType) {
                LayoutType.EMPTY_LIST -> {
                    tvTitle.text = getString(R.string.no_data)
                    tvTitle.isVisible = true
                }
                LayoutType.NO_INTERNET -> {
                    tvTitle.text = getString(R.string.no_connection)
                    tvTitle.isVisible = true
                    tvMessage.text = getString(R.string.please_check_connection)
                    btnRefresh.isVisible = true
                }
                LayoutType.ERROR -> {
                    tvTitle.text = getString(R.string.there_is_an_error)
                    tvTitle.isVisible = true
                    tvMessage.text = getString(R.string.try_again_in_a_few_moment)
                    btnRefresh.isVisible = true
                }
            }
        }
    }
}