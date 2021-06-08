package com.projek.sampahku1.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.projek.sampahku1.databinding.ActivityPerhitunganBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PerhitunganActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerhitunganBinding
    private var imageUri: Uri? = null
    private lateinit var currentPhotoPath: String
    private lateinit var database:StorageReference

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
        database=FirebaseStorage.getInstance().reference
        askPermissionStorage()
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

    private fun askPermissionStorage() {
        val stringOfPerm = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
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
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(Date())

        val storageDir:File? = if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        } else {
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        }

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
                val imageFile = File(currentPhotoPath)
                MediaScannerConnection.scanFile(this,arrayOf(imageFile.toString()),arrayOf(imageFile.name),null)

                binding.ivHasilGambar.setImageURI(Uri.fromFile(imageFile))
                binding.ivHasilGambar.visibility = View.VISIBLE
                /*upload to firebase*/
                uploadImageToFirebase(imageFile.name,Uri.fromFile(imageFile),"fromTakePicture")
            }
        }
    private fun getFileExt(contentUri: Uri): String? {
        val k:ContentResolver=contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(k.getType(contentUri))
    }
    private fun uploadImageToFirebase(imageFilename: String, uri: Uri,location:String) {
        lateinit var image:StorageReference
        if(location=="fromTakePicture") {
            image = database.child(currentPhotoPath + imageFilename)
        }
        else if(location=="fromPicture"){
           image=database.child("pictures/$imageFilename")
        }
        image.putFile(uri).addOnSuccessListener {
            OnSuccessListener<UploadTask.TaskSnapshot> {
                image.downloadUrl.addOnSuccessListener { OnSuccessListener<Uri> { Toast.makeText(this@PerhitunganActivity,"uploaded image is:$uri",Toast.LENGTH_LONG).show() } }
            }
        }.addOnFailureListener { p0 ->
            Toast.makeText(
                this@PerhitunganActivity,
                "${p0.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val pickImages =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { uri ->
                binding.ivHasilGambar.setImageURI(uri)
                binding.ivHasilGambar.visibility = View.VISIBLE
                val timeStamp=SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val imageFilename="JPEG_"+timeStamp+"."+getFileExt(uri)
                uploadImageToFirebase(imageFilename,uri,"fromPicture")
            }
        }
}
