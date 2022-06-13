package com.eone.submission1_bpai.ui.addstory

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.eone.submission1_bpai.R
import com.eone.submission1_bpai.SessionPreference
import com.eone.submission1_bpai.databinding.ActivityAddStoryBinding
import com.eone.submission1_bpai.remote.Resource
import com.eone.submission1_bpai.utils.*
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AddStoryActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_key")
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var viewModel: AddStoryViewModel
    private var getFile: File? = null
    private var imageScaleZoom = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        setupViewModel()
        setupView()
    }

    private fun setupView() {
        binding.apply {
            ivClose.setOnClickListener {
                onBackPressed()

            }

            imgStory.setOnClickListener {
                imageScaleZoom = !imageScaleZoom
                binding.imgStory.scaleType =
                    if (imageScaleZoom) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
            }

            btnCamera.setOnClickListener {
                val intent = Intent(this@AddStoryActivity, CameraActivity::class.java)
                launcherIntentCameraX.launch(intent)
            }

            btnGallery.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_GET_CONTENT
                intent.type = "image/*"
                val chooser = Intent.createChooser(intent, "Choose a Picture")
                launcherIntentGallery.launch(chooser)
            }

            btnPost.setOnClickListener {
                postStory()
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            binding.imgStory.setImageBitmap(result)
            getFile = myFile
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@AddStoryActivity)
            binding.imgStory.setImageURI(selectedImg)
            getFile = myFile
        }
    }

    private fun setupViewModel() {
        binding.apply {
            val pref = SessionPreference.getInstance(dataStore)
            viewModel =
                ViewModelProvider(
                    this@AddStoryActivity,
                    ViewModelFactory(pref, application)
                )[AddStoryViewModel::class.java]

            viewModel.uploadResponse.observe(this@AddStoryActivity) {
                when (it) {
                    is Resource.Loading -> {
                         showLoading(true)
                    }
                    is Resource.Success -> {
                        if (it.data != null) {
                            showLoading(false)
                            FancyToast.makeText(this@AddStoryActivity,"Berhasil memposting story",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show()
                            onBackPressed()
                        }
                    }
                    is Resource.Error -> {
                        showLoading(false)
                        FancyToast.makeText(this@AddStoryActivity,"Error : ${it.message}",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show()
                    }
                }
            }
        }
    }

    private fun postStory() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            val description =
                binding.etDescription.text.toString()
                    .toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            lifecycleScope.launch {
                viewModel.postStory(imageMultipart, description)
            }

        } else {
            Toast.makeText(
                this,
                resources.getString(R.string.input_picture_first),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.not_get_permission),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
        binding.btnPost.isEnabled = !state
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}