package com.eone.submission1_bpai.data.model.response

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
