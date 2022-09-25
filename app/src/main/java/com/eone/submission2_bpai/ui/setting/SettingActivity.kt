package com.eone.submission2_bpai.ui.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import com.eone.submission2_bpai.ui.auth.AuthViewModel
import com.eone.submission2_bpai.databinding.ActivitySettingBinding
import com.eone.submission2_bpai.ui.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalPagingApi
class SettingActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingBinding
    private val viewModel : AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView(){
        binding.apply {
            tvLanguage.setOnClickListener {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            tvLogout.setOnClickListener {
                viewModel.removeAuthToken()
                startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
                finishAffinity()
            }

            ivBack.setOnClickListener {
                onBackPressed()
            }
        }
    }
}