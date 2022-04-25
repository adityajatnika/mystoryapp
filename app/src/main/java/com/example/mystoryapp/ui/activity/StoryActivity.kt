package com.example.mystoryapp.ui.activity

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.content.pm.PackageManager
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.mystoryapp.R
import com.example.mystoryapp.data.local.SessionManager
import com.example.mystoryapp.data.remote.response.PostStoryResponse
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import com.example.mystoryapp.databinding.ActivityStoryBinding
import com.example.mystoryapp.rotateBitmap
import com.example.mystoryapp.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


class StoryActivity : AppCompatActivity() {
    private lateinit var sessionManager: SessionManager
    private lateinit var binding: ActivityStoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.add_your_story)

        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        binding.progressBar.visibility = View.INVISIBLE

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        binding.cameraButton.setOnClickListener {
            startCameraX() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.uploadButton.setOnClickListener { uploadImage() }
    }

    private fun uploadImage() {
        binding.progressBar.visibility = View.VISIBLE
        if (getFile != null && binding.edtDesc.text.toString() != "") {
            val file = reduceFileImage(getFile as File)
            val text = binding.edtDesc.text.toString()
            val desc = text.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            val service = ApiConfig.getApiService().postStory(token = StringBuilder("Bearer ").append(sessionManager.fetchAuthToken()).toString(), imageMultipart, desc)
            service.enqueue(object : Callback<PostStoryResponse> {
                override fun onResponse(
                    call: Call<PostStoryResponse>,
                    response: Response<PostStoryResponse>
                ) {
                    binding.progressBar.visibility = View.INVISIBLE
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            Toast.makeText(this@StoryActivity, responseBody.message, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@StoryActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        Toast.makeText(this@StoryActivity, response.message(), Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<PostStoryResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(this@StoryActivity, getString(R.string.retrofit_instance_failed), Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            binding.progressBar.visibility = View.INVISIBLE
            if(getFile == null) Toast.makeText(this@StoryActivity, getString(R.string.picture_not_found), Toast.LENGTH_SHORT).show()
            if(binding.edtDesc.text.toString() == "") Toast.makeText(this@StoryActivity, getString(R.string.caption_is_null), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_picture))
        launcherIntentGallery.launch(chooser)
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private var getFile: File? = null

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@StoryActivity)
            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
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

            //convert bitmap to File!
            val f = File(this.cacheDir, myFile.name)
            f.createNewFile()

            val bos = ByteArrayOutputStream()
            result.compress(CompressFormat.PNG, 0, bos)
            val bitmapData = bos.toByteArray()

            val fos = FileOutputStream(f)
            fos.write(bitmapData)
            fos.flush()
            fos.close()

            getFile = f
            binding.previewImageView.setImageBitmap(result)

        }
    }

    private fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    //camera x special function
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
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
