package com.eone.submission2_bpai.ui.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.eone.submission2_bpai.utils.CameraActivity
import com.eone.submission2_bpai.utils.reduceFileImage
import com.eone.submission2_bpai.utils.rotateBitmap
import com.eone.submission2_bpai.utils.uriToFile
import com.eone.submission2_bpai.R
import com.eone.submission2_bpai.databinding.ActivityAddStoryBinding
import com.shashank.sony.fancytoastlib.FancyToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
@ExperimentalPagingApi
class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private val viewModel: AddStoryViewModel by viewModels()
    private var getFile: File? = null
    private var imageScaleZoom = true
    private var location: Location? = null
    private var token = ""


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
            lifecycleScope.launchWhenCreated {
                launch {
                    viewModel.getAuthToken().collect { authToken ->
                        if (!authToken.isNullOrEmpty()) token = authToken
                    }
                }
            }
        }
    }

    private fun postStory() {
        if (getFile != null) {
            showLoading(true)
            val file = reduceFileImage(getFile as File)

            val description = binding.etDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            // Collect story location information
            var lat: RequestBody? = null
            var lon: RequestBody? = null

            if (location != null) {
                lat =
                    location?.latitude.toString().toRequestBody("text/plain".toMediaType())
                lon =
                    location?.longitude.toString().toRequestBody("text/plain".toMediaType())
            }

            lifecycleScope.launch {
                viewModel.postStory(token,imageMultipart, description,lat, lon).collect{ response->
                    response.onSuccess {
                        showLoading(false)
                        FancyToast.makeText(this@AddStoryActivity,"Berhasil memposting story",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show()
                        onBackPressed()
                    }

                    response.onFailure {
                        showLoading(false)
                        FancyToast.makeText(this@AddStoryActivity,"Error : ${it.message}",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show()
                    }
                }
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