package com.eone.submission1_bpai.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.eone.submission1_bpai.SessionPreference
import com.eone.submission1_bpai.databinding.ActivitySplashBinding
import com.eone.submission1_bpai.ui.auth.AuthViewModel
import com.eone.submission1_bpai.ui.auth.LoginActivity
import com.eone.submission1_bpai.ui.main.MainActivity
import com.eone.submission1_bpai.utils.ViewModelFactory

class SplashActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var binding: ActivitySplashBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
    }

    private fun setupViewModel() {
        val pref = SessionPreference.getInstance(dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref, application))[AuthViewModel::class.java]

        Handler(mainLooper).postDelayed({
            viewModel.getUserKey().observe(this) {
                if (it.isNullOrEmpty()) {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finishAffinity()
                } else {
                    startActivity(Intent(this, MainActivity::class.java))
                    finishAffinity()
                }
            }

        }, SPLASH_DELAY)
    }

    companion object {
        const val SPLASH_DELAY = 2000L
    }
}