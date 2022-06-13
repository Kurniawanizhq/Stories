package com.eone.submission1_bpai.ui.addstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.eone.submission1_bpai.SessionPreference
import com.eone.submission1_bpai.data.model.response.BaseResponse
import com.eone.submission1_bpai.network.ApiConfig
import com.eone.submission1_bpai.remote.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddStoryViewModel(private val pref: SessionPreference) : ViewModel() {
    private val _uploadResponse = MutableLiveData<Resource<String>>()
    val uploadResponse: LiveData<Resource<String>> = _uploadResponse

    suspend fun postStory(imageMultipart: MultipartBody.Part, description: RequestBody) {
        _uploadResponse.postValue(Resource.Loading())
        val client = ApiConfig.apiInstance.postStory(
            token = "Bearer ${pref.getRealtimeToken().first()}",
            imageMultipart,
            description
        )

        client.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    _uploadResponse.postValue(Resource.Success(response.body()?.message))
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        BaseResponse::class.java
                    )
                    _uploadResponse.postValue(Resource.Error(errorResponse.message))
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.e(
                    AddStoryViewModel::class.java.simpleName,
                    "onFailure upload"
                )
                _uploadResponse.postValue(Resource.Error(t.message))
            }
        })
    }
}