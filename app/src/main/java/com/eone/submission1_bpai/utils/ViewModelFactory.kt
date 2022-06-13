package com.eone.submission1_bpai.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.eone.submission1_bpai.SessionPreference
import com.eone.submission1_bpai.ui.addstory.AddStoryViewModel
import com.eone.submission1_bpai.ui.auth.AuthViewModel
import com.eone.submission1_bpai.ui.main.MainViewModel

class ViewModelFactory(private val pref : SessionPreference, val app : Application) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            return AuthViewModel(pref) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(pref, app) as T
        }
        if (modelClass.isAssignableFrom(AddStoryViewModel::class.java)) {
            return AddStoryViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}