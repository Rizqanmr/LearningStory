package com.rizqanmr.learningstory.util

import android.annotation.TargetApi
import android.graphics.PorterDuff
import android.os.Build
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.rizqanmr.learningstory.databinding.LayoutToolbarBinding

abstract class BaseAppCompatActivity : AppCompatActivity() {

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
}