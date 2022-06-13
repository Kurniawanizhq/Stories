package com.eone.submission1_bpai.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.eone.submission1_bpai.R
import com.eone.submission1_bpai.SessionPreference
import com.eone.submission1_bpai.databinding.ActivitySettingBinding
import com.eone.submission1_bpai.ui.auth.AuthViewModel
import com.eone.submission1_bpai.ui.auth.LoginActivity
import com.eone.submission1_bpai.utils.ViewModelFactory

class SettingActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var binding : ActivitySettingBinding
    private lateinit var viewModel : AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
    }

    private fun setupViewModel() {
        val pref = SessionPreference.getInstance(dataStore)
        viewModel = ViewModelProvider(this, ViewModelFactory(pref,application))[AuthViewModel::class.java]
    }

    private fun setupView(){
        binding.apply {
            tvLanguage.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            tvLogout.setOnClickListener {
                viewModel.logout()
                startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
                finishAffinity()
            }

            ivBack.setOnClickListener {
                onBackPressed()
            }
        }
    }
}