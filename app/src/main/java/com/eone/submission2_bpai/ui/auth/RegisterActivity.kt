package com.eone.submission2_bpai.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.eone.submission2_bpai.databinding.ActivityRegisterBinding
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()
    private var registerJob: Job = Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
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
                playSequentially(img, tvRegister, name, email, password, register, toLogin)
                start()
            }
        }
    }

    private fun setupView() {
        binding.apply {
            btnRegister.setOnClickListener {
                registerProcced()
            }

            tvHaveAccount.setOnClickListener {
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
            }
        }
    }

    private fun registerProcced() {
        binding.apply {
            showLoading(true)

            lifecycleScope.launchWhenCreated {
                if (registerJob.isActive) registerJob.cancel()

                registerJob = launch {
                    viewModel.register(
                        etNama.text.toString(),
                        etEmail.text.toString(),
                        etPassword.text.toString()
                    ).collect {
                        it.onSuccess {
                            FancyToast.makeText(
                                this@RegisterActivity,
                                "Registrasi Berhasil",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.SUCCESS,
                                false
                            ).show()
                            startActivity(
                                Intent(this@RegisterActivity, LoginActivity::class.java).addFlags(
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                                )
                            )
                            finish()
                        }

                        it.onFailure { error ->
                            FancyToast.makeText(
                                this@RegisterActivity,
                                "Register gagal, silahkan coba lagi",
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

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        binding.btnRegister.isEnabled = !state
    }
}