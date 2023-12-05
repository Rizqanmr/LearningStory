package com.rizqanmr.learningstory.data.model.reqbody

import com.google.gson.annotations.SerializedName

data class LoginReqBody(
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?
)
