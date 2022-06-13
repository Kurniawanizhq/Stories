package com.eone.submission1_bpai.data.model.response

import com.eone.submission1_bpai.data.model.common.Login
import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@SerializedName("error")
	val error: Boolean?,
	@SerializedName("message")
	val message: String?,
	@SerializedName("loginResult")
	val loginResult: Login?
)
