package com.projek.sampahku1

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore

import android.view.View

import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.projek.sampahku1.databinding.ActivityPerhitunganBinding

import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import com.bumptech.glide.load.engine.Resource
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PerhitunganActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerhitunganBinding
    private var isGranted: Boolean = false
    var imageUri: Uri? = null
    lateinit var currentPhotoPath: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerhitunganBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //camera
        binding.layout.visibility = View.GONE
        val btnCamera = binding.btnAmbilGambar
        binding.textView6.visibility = View.GONE
        binding.ivHasilGambar.visibility = View.GONE
        val btnGallery = binding.pilihGaleri
        askPermissionCamera()
        btnCamera.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            var photoFile: File? = null
            try {
                photoFile = createImageFile()
            } catch (e: IOException) {
                Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
            }
            photoFile.also {
                imageUri = FileProvider.getUriForFile(
                    this,
                    "com.projek.sampahku1.fileprovider",
                    it as File
                )

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                try {

                    takePicture.launch(takePictureIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show()
                }


            }

        }
        btnGallery.setOnClickListener {
            pickImages.launch("image/*")
        }
    }

    private fun askPermissionCamera() {
        val stringOfPerm = arrayOf(android.Manifest.permission.CAMERA)
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, stringOfPerm, 101)
            Toast.makeText(
                this,
                "Untuk menggunakan camera, izinkan aplikasi memakai kamera anda",
                Toast.LENGTH_SHORT
            ).show()

        } else {
            binding.layout.visibility = View.VISIBLE
        }
    }

    /*
        private fun createPhotoFile():File {
            val photoFileName= UUID.randomUUID().toString()
            val path= applicationContext.filesDir
            println("Debug: requireContext().filesDir= $path")
            val imagePath=File(path,"images")
            imagePath.mkdirs()
            return File(imagePath,"$photoFileName.jpg")
        }

    */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 101) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.layout.visibility = View.VISIBLE
            } else {
                Toast.makeText(
                    this,
                    "Untuk menggunakan camera, izinkan aplikasi memakai kamera anda",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private val takePicture =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                /*val bundle=result.data?.extras
                val imageBitmap=bundle?.get("data") as Bitmap
                println("Debug: photoUri= ${imageUri}")
               */
                var imageFile = File(currentPhotoPath)

                binding.ivHasilGambar.setImageURI(Uri.fromFile(imageFile))
                binding.ivHasilGambar.visibility = View.VISIBLE
            }
        }

    private val pickImages =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { uri ->
                binding.ivHasilGambar.setImageURI(uri)
                binding.ivHasilGambar.visibility = View.VISIBLE

            }
        }
}
