package com.eone.submission2_bpai.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.eone.submission2_bpai.databinding.ActivityLoginBinding
import com.eone.submission2_bpai.ui.main.MainActivity
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()
    private var loginJob: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            setupView()
        }
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
                playSequentially(img, email, password, login, toRegister)
                start()
            }
        }
    }

    private fun setupView() {
        binding.apply {
            btnLogin.setOnClickListener {
                if (validLogin()) {
                    loginProcced()
                } else {
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

    private fun loginProcced() {
        binding.apply {
            showLoading(true)

            lifecycleScope.launchWhenCreated {
                if (loginJob.isActive) loginJob.cancel()

                loginJob = launch {
                    viewModel.login(etEmail.text.toString(), etPassword.text.toString()).collect {
                        it.onSuccess { response ->
                            response.loginResult?.token?.let { token ->
                                viewModel.saveAuthToken(token)
                                startActivity(
                                    Intent(this@LoginActivity, MainActivity::class.java).addFlags(
                                        Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    ).putExtra(MainActivity.EXTRA_TOKEN,token)
                                )
                                finish()

                            }
                        }

                        it.onFailure {
                            FancyToast.makeText(
                                this@LoginActivity,
                               "Login gagal, silahkan coba kembali",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.ERROR,
                                false
                            ).show()

                            showLoading(false)
                        }
                    }
                }
            }
        }
    }

    private fun validLogin() =
        binding.etEmail.error == null && binding.etPassword.error == null && !binding.etEmail.text.isNullOrEmpty() && !binding.etPassword.text.isNullOrEmpty()

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !state
    }
}