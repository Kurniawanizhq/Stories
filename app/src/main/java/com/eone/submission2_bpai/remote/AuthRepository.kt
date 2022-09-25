package com.eone.submission2_bpai.remote

import android.util.Log
import com.eone.submission2_bpai.SessionPreference
import com.eone.submission2_bpai.data.model.response.BaseResponse
import com.eone.submission2_bpai.data.model.response.LoginResponse
import com.eone.submission2_bpai.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val apiService: ApiService,
    private val pref: SessionPreference
) {

    suspend fun userLogin(email: String, password: String): Flow<Result<LoginResponse>> = flow {
        try {
            val response = apiService.login(email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)


    suspend fun userRegister(
        name: String,
        email: String,
        password: String
    ): Flow<Result<BaseResponse>> = flow {
        try {
            val response = apiService.register(name, email, password)
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("Error register : ", e.printStackTrace().toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveAuthToken(token: String) {
        pref.saveToken(token)
    }

    suspend fun removeAuthToken() {
        pref.removeToken()
    }

    fun getAuthToken(): Flow<String?> = pref.getRealtimeToken()
}