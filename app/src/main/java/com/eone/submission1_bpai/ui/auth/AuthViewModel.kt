package com.eone.submission1_bpai.ui.auth

import android.util.Log
import androidx.lifecycle.*
import com.eone.submission1_bpai.SessionPreference
import com.eone.submission1_bpai.data.model.request.LoginRequest
import com.eone.submission1_bpai.data.model.request.RegisterRequest
import com.eone.submission1_bpai.data.model.response.BaseResponse
import com.eone.submission1_bpai.data.model.response.LoginResponse
import com.eone.submission1_bpai.network.ApiConfig
import com.eone.submission1_bpai.remote.Resource
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel(private val pref: SessionPreference) : ViewModel() {
    private val _authResponse = MutableLiveData<Resource<String>>()
    val authResponse: LiveData<Resource<String>> = _authResponse

    fun login(email: String, password: String) {
        _authResponse.postValue(Resource.Loading())
        val client = ApiConfig.apiInstance.login(LoginRequest(email, password))

        client.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResult = response.body()?.loginResult?.token

                    loginResult?.let { saveUserKey(it) }
                    _authResponse.postValue(Resource.Success(loginResult))
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        BaseResponse::class.java
                    )
                    _authResponse.postValue(Resource.Error(errorResponse.message))
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e(
                    AuthViewModel::class.java.simpleName,
                    "onFailure login"
                )
                _authResponse.postValue(Resource.Error(t.message))
            }
        })
    }

    fun register(name: String, email: String, password: String) {
        _authResponse.postValue(Resource.Loading())
        val client = ApiConfig.apiInstance.register(RegisterRequest(name, email, password))

        client.enqueue(object : Callback<BaseResponse> {
            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
                if (response.isSuccessful) {
                    val message = response.body()?.message.toString()
                    _authResponse.postValue(Resource.Success(message))
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        BaseResponse::class.java
                    )
                    _authResponse.postValue(Resource.Error(errorResponse.message))
                }
            }

            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
                Log.e(
                    AuthViewModel::class.java.simpleName,
                    "onFailure register"
                )
                _authResponse.postValue(Resource.Error(t.message))
            }
        })
    }


    fun logout() = deleteUserKey()

    private fun deleteUserKey() {
        viewModelScope.launch {
            pref.removeToken()
        }
    }

    fun getUserKey() = pref.getRealtimeToken().asLiveData()

    private fun saveUserKey(key: String) {
        viewModelScope.launch {
            pref.saveToken(key)
        }
    }

}