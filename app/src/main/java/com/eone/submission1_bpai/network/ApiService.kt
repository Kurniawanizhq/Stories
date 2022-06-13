package com.eone.submission1_bpai.network

import com.eone.submission1_bpai.data.model.request.LoginRequest
import com.eone.submission1_bpai.data.model.request.RegisterRequest
import com.eone.submission1_bpai.data.model.response.BaseResponse
import com.eone.submission1_bpai.data.model.response.LoginResponse
import com.eone.submission1_bpai.data.model.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST("login")
    fun login(
        @Body body: LoginRequest,
    ): Call<LoginResponse>

    @POST("register")
    fun register(
        @Body body: RegisterRequest,
    ): Call<BaseResponse>

    @GET("stories")
    fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null
    ): Call<StoryResponse>

    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody): Call<BaseResponse>
}