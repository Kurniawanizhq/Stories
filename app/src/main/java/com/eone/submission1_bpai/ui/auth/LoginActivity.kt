package com.eone.submission1_bpai.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.eone.submission1_bpai.R
import com.eone.submission1_bpai.SessionPreference
import com.eone.submission1_bpai.databinding.ActivityLoginBinding
import com.eone.submission1_bpai.remote.Resource
import com.eone.submission1_bpai.ui.main.MainActivity
import com.eone.submission1_bpai.utils.ViewModelFactory
import com.shashank.sony.fancytoastlib.FancyToast

class LoginActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()
        playAnimation()
    }

    private fun playAnimation() {
        binding.apply {
            val img = ObjectAnimator.ofFloat(imageView, View.ALPHA, 1f).setDuration(500)
            val email =
                ObjectAnimator.ofFloat(layoutEmail, View.ALPHA, 1f).setDuration(500)
            val password = ObjectAnimator.ofFloat(layoutPassword, View.ALPHA, 1f).setDuration(500)
            val login = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f).setDuration(500)
            val toRegister =
                ObjectAnimator.ofFloat(tvRegister, View.ALPHA, 1f).setDuration(500)

            AnimatorSet().apply {
                playSequentially(img,email,password,login,toRegister)
                start()
            }
        }
    }

    private fun setupView() {
        binding.apply {
            btnLogin.setOnClickListener {
                if (validLogin()){
                    viewModel.login(etEmail.text.toString(), etPassword.text.toString())
                }else{
                    Toast.makeText(
                        this@LoginActivity,
                        " Cek inputan anda!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            tvRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
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
                    startActivity(
                        Intent(this, MainActivity::class.java).addFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                        )
                    )
                    finish()
                }
                is Resource.Error -> {
                    showLoading(false)
                    FancyToast.makeText(this, it.message, FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show()
                }
            }
        }
    }

    private fun validLogin() = binding.etEmail.error == null && binding.etPassword.error == null && !binding.etEmail.text.isNullOrEmpty() && !binding.etPassword.text.isNullOrEmpty()

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !state
    }
}