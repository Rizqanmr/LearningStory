package com.rizqanmr.learningstory.data.model.reqbody

import com.google.gson.annotations.SerializedName

data class RegisterReqBody(
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?
)
