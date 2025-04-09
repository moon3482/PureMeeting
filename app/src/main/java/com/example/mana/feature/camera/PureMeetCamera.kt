package com.example.mana.feature.camera

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.mana.R
import com.example.mana.databinding.ActivityPureMeetCameraBinding
import com.example.mana.feature.common.Constants
import com.example.mana.logging.Tag
import com.example.mana.util.PermissionHelper
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class PureMeetCamera : AppCompatActivity() {
    private val binding by lazy { ActivityPureMeetCameraBinding.inflate(layoutInflater) }
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (PermissionHelper.allPermissionGranted(this)) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                PermissionHelper.getRequirePermissions(),
                PermissionHelper.CAMERA_PERMISSION_CODE
            )
        }
        cameraExecutor = Executors.newSingleThreadExecutor()
        binding.shutter.setOnClickListener { takePhoto() }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val name = SimpleDateFormat(
            "yyyy-MM-dd_HH-mm-ss",
            Locale.getDefault()
        ).format(System.currentTimeMillis())
        val photoFile = File(getOutputDirectory(), "$name.jpg")
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(photoFile)
            .build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(e: ImageCaptureException) {
                    Timber.tag(Tag.PURE_MEET_CAMERA).e(e, "takePicture")
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Timber.tag(Tag.PURE_MEET_CAMERA).d(msg);
                    val saveUri = FileProvider.getUriForFile(
                        this@PureMeetCamera,
                        "${applicationContext.packageName}.fileprovider",
                        photoFile
                    )

                    val resultIntent = Intent().apply {
                        putExtra(Constants.BundleKey.MANA_PHOTO_URI, saveUri.toString())
                        putExtra(Constants.BundleKey.MANA_PHOTO_PATH, photoFile.absolutePath)
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            }
        )
    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let { externalDir ->
            File(externalDir, getString(R.string.app_name)).apply {
                if (!exists() && !mkdirs()) {
                    Timber.tag(Tag.PURE_MEET_CAMERA).d("Failure Make Directory")
                    return@let null
                }
            }
        }
        return mediaDir ?: filesDir
    }

    private fun startCamera() {
        val cameraFuture = ProcessCameraProvider.getInstance(baseContext)
        cameraFuture.addListener({
            val cameraProvider = cameraFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.surfaceProvider = binding.viewFinder.surfaceProvider
                }
            imageCapture = ImageCapture.Builder().build()
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
            } catch (e: Exception) {
                Timber.tag(Tag.PURE_MEET_CAMERA).e(e)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionHelper.CAMERA_PERMISSION_CODE) {
            if (PermissionHelper.allPermissionGranted(this)) {
                startCamera()
            } else {
                Toast.makeText(this, "Require Camera Permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }
}