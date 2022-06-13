package com.eone.submission1_bpai.data.model.common

import com.google.gson.annotations.SerializedName

data class Login(
    @SerializedName("userId")
    val userId : String,
    @SerializedName("name")
    val name : String,
    @SerializedName("token")
    val token : String
)
