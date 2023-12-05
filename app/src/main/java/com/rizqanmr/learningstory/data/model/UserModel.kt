package com.rizqanmr.learningstory.data.model

data class UserModel(
    val id: String,
    val token: String,
    val isLogin: Boolean = false
)
