package com.eone.submission2_bpai.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eone.submission2_bpai.data.model.response.LoginResponse
import com.eone.submission2_bpai.remote.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {
    fun getAuthToken(): Flow<String?> = authRepository.getAuthToken()

    suspend fun login(email: String, password: String): Flow<Result<LoginResponse>> =
        authRepository.userLogin(email, password)

    suspend fun register(name: String, email: String, password: String) =
        authRepository.userRegister(name, email, password)

    fun saveAuthToken(token: String) {
        viewModelScope.launch {
            authRepository.saveAuthToken(token)
        }
    }

    fun removeAuthToken() {
        viewModelScope.launch {
            authRepository.removeAuthToken()
        }
    }
}