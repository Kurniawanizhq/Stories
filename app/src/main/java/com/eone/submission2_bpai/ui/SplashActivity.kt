package com.eone.submission2_bpai.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.eone.submission2_bpai.ui.auth.AuthViewModel
import com.eone.submission2_bpai.databinding.ActivitySplashBinding
import com.eone.submission2_bpai.ui.auth.LoginActivity
import com.eone.submission2_bpai.ui.main.MainActivity
import com.eone.submission2_bpai.ui.main.MainActivity.Companion.EXTRA_TOKEN
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
@ExperimentalPagingApi
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
    }

    private fun setupViewModel() {
        lifecycleScope.launchWhenCreated {
            launch {
                viewModel.getAuthToken().collect {
                    if (it.isNullOrEmpty()) {
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@SplashActivity, MainActivity::class.java)
                            .putExtra(EXTRA_TOKEN,it))
                        finish()
                    }
                }
            }
        }
    }

}