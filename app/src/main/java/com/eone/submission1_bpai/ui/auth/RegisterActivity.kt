package com.eone.submission1_bpai.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.eone.submission1_bpai.SessionPreference
import com.eone.submission1_bpai.databinding.ActivityRegisterBinding
import com.eone.submission1_bpai.remote.Resource
import com.eone.submission1_bpai.utils.ViewModelFactory
import com.shashank.sony.fancytoastlib.FancyToast

class RegisterActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        playAnimation()
    }

    private fun playAnimation() {
        binding.apply {
            val img = ObjectAnimator.ofFloat(imageView2, View.ALPHA, 1f).setDuration(500)
            val tvRegister = ObjectAnimator.ofFloat(tvRegisterNow, View.ALPHA, 1f).setDuration(500)
            val name = ObjectAnimator.ofFloat(layoutNama, View.ALPHA, 1f).setDuration(500)
            val email =
                ObjectAnimator.ofFloat(layoutEmail, View.ALPHA, 1f).setDuration(500)
            val password = ObjectAnimator.ofFloat(layoutPassword, View.ALPHA, 1f).setDuration(500)
            val register = ObjectAnimator.ofFloat(btnRegister, View.ALPHA, 1f).setDuration(500)
            val toLogin =
                ObjectAnimator.ofFloat(tvHaveAccount, View.ALPHA, 1f).setDuration(500)

            AnimatorSet().apply {
                playSequentially(img,tvRegister,name,email,password,register,toLogin)
                start()
            }
        }
    }

    private fun setupView() {
        binding.apply {
            btnRegister.setOnClickListener {
                viewModel.register(
                    etNama.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
            }

            tvHaveAccount.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
    }

    private fun setupViewModel() {
        val pref = SessionPreference.getInstance(dataStore)
        viewModel =
            ViewModelProvider(this, ViewModelFactory(pref, application))[AuthViewModel::class.java]

        viewModel.authResponse.observe(this) {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Success -> {
                    showLoading(false)
                    FancyToast.makeText(this, it.data, FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show()
                    startActivity(
                        Intent(this, LoginActivity::class.java).addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                        )
                    )
                }
                is Resource.Error -> {
                    showLoading(false)
                    FancyToast.makeText(this, it.message, FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show()
                }
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !state
    }
}