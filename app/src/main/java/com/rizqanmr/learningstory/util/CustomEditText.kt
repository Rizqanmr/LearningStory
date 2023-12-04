package com.rizqanmr.learningstory.util

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.rizqanmr.learningstory.R

class CustomEditText(context: Context, attrs: AttributeSet): AppCompatEditText(context, attrs) {

    private var errorText: String? = null

    init {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable) {
                val password = s.toString()
                if (password.length < 8) {
                    errorText = resources.getText(R.string.invalid_password).toString()
                    showError()
                } else {
                    errorText = null
                    hideError()
                }
            }
        })
    }

    private fun showError() {
        errorText?.let {
            setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
            error = it
        }
    }

    private fun hideError() {
        setTextColor(ContextCompat.getColor(context, android.R.color.black))
        error = null
    }

}