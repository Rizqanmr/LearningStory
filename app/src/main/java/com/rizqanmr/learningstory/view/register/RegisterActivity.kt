package com.rizqanmr.learningstory.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import com.google.android.material.snackbar.Snackbar
import com.rizqanmr.learningstory.R
import com.rizqanmr.learningstory.data.model.reqbody.RegisterReqBody
import com.rizqanmr.learningstory.databinding.ActivityRegisterBinding
import com.rizqanmr.learningstory.base.BaseAppCompatActivity
import com.rizqanmr.learningstory.view.ViewModelFactory
import com.rizqanmr.learningstory.view.login.LoginActivity

class RegisterActivity : BaseAppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
        subscribeToLiveData()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.show(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
        setStatusBarSolidColor(R.color.sky_blue, true)
    }

    private fun setupAction() {
        binding.btnSignup.setOnClickListener {
            val email = binding.edRegisterEmail.text.toString()
            val name = binding.edRegisterName.text.toString()
            val password = binding.edRegisterPassword.text.toString()
            viewModel.registerUser(RegisterReqBody(name, email, password))
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivRegister, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvTitleRegisterPage, View.ALPHA, 1f).setDuration(100)
        val nameTextView =
            ObjectAnimator.ofFloat(binding.tvName, View.ALPHA, 1f).setDuration(100)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.etNameLayout, View.ALPHA, 1f).setDuration(100)
        val emailTextView =
            ObjectAnimator.ofFloat(binding.tvEmail, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.etEmailLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.tvPassword, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.etPasswordLayout, View.ALPHA, 1f).setDuration(100)
        val signup = ObjectAnimator.ofFloat(binding.btnSignup, View.ALPHA, 1f).setDuration(100)


        AnimatorSet().apply {
            playSequentially(
                title,
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                signup
            )
            startDelay = 100
        }.start()
    }

    private fun subscribeToLiveData() {
        viewModel.getIsLoading().observe(this) {
            showLoading(it)
        }
        viewModel.registerLiveData().observe(this) {
            handleSuccessRegister()
        }
        viewModel.errorRegisterLiveData().observe(this) {
            showSnackbarError(it)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.layoutLoading.progressLoading.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showSnackbarError(message: String) {
        Snackbar.make(binding.containerRegister, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun handleSuccessRegister() {
        LoginActivity.startThisActivity(this, bundleOf())
    }
}