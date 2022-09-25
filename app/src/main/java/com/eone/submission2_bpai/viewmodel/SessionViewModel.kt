package com.eone.submission2_bpai.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.eone.submission2_bpai.SessionPreference
import kotlinx.coroutines.launch

class SessionViewModel(private val _sessionPreference: SessionPreference) : ViewModel() {
    fun getRealtimeToken() = _sessionPreference.getRealtimeToken().asLiveData()

    fun saveToken(token: String) {
        viewModelScope.launch {
            _sessionPreference.saveToken(token)
        }
    }

    fun removeToken() {
        viewModelScope.launch {
            _sessionPreference.removeToken()
        }
    }
}