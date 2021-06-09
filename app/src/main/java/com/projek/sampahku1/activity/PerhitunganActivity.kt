package com.projek.sampahku1.activity

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
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
import com.projek.sampahku1.api.ApiInstance
import com.projek.sampahku1.databinding.ActivityPerhitunganBinding
import com.projek.sampahku1.helper.getPathFromUriHelper
import com.projek.sampahku1.model.UserMendaurUlang
import com.projek.sampahku1.session.SessionManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PerhitunganActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPerhitunganBinding
    private var imageUri: Uri? = null
    private lateinit var currentPhotoPath: String
    private lateinit var database: StorageReference
    private lateinit var sessionManager: SessionManager
    lateinit var userDetail: MutableMap<String, Any?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerhitunganBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)
        //camera
        val btnCamera = binding.btnAmbilGambar
        val btnGallery = binding.pilihGaleri
        binding.layout.visibility = View.GONE
        binding.textView6.visibility = View.GONE
        binding.ivHasilGambar.visibility = View.GONE

        askPermissionCamera()
        askPermissionStorage()
        database = FirebaseStorage.getInstance().reference
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
        binding.btnSend.setOnClickListener{
            saveImageUpload()
        }
    }

    private fun askPermissionStorage() {
        val stringOfPerm = arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, stringOfPerm, 102)
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
        if (requestCode == 101 || requestCode == 102) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.layout.visibility = View.VISIBLE
            } else {
                Toast.makeText(
                    this,
                    "Untuk menggunakan camera, izinkan aplikasi memakai kamera dan mengakses file anda",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }


    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

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
                val imageFile = File(currentPhotoPath)
                MediaScannerConnection.scanFile(this,
                    arrayOf(imageFile.toString()),
                    arrayOf(imageFile.name),
                    null)

                binding.ivHasilGambar.setImageURI(Uri.fromFile(imageFile))
                binding.ivHasilGambar.visibility = View.VISIBLE
                /*upload to invoice transaction*/

            }
        }

    private fun uploadImageToInvoice() {

    }

    private fun getFileExt(contentUri: Uri): String? {
        val k: ContentResolver = contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(k.getType(contentUri))
    }

    private fun uploadImageToFirebase(imageFilename: String, uri: Uri, location: String) {
        lateinit var image: StorageReference
        if (location == "fromTakePicture") {
            image = database.child(currentPhotoPath + imageFilename)
        } else if (location == "fromPicture") {
            image = database.child("pictures/$imageFilename")
        }
        image.putFile(uri).addOnSuccessListener {
            OnSuccessListener<UploadTask.TaskSnapshot> {
                image.downloadUrl.addOnSuccessListener {
                    OnSuccessListener<Uri> {
                        Toast.makeText(this@PerhitunganActivity,
                            "uploaded image is:$uri",
                            Toast.LENGTH_LONG).show()
                    }
                }
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
                val timeStamp =
                    SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
                val imageFilename = "JPEG_" + timeStamp + "." + getFileExt(uri)
                val uriPathHelper = getPathFromUriHelper()
                currentPhotoPath = uriPathHelper.getPath(this, uri).toString()

            }
        }

    private fun saveImageUpload() {
        var date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        var imageFile = File(currentPhotoPath)
        val mimeType = getMimeType(imageFile);
        val timeStamp =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFilename = "JPEG_" + timeStamp + "." + getFileExt(Uri.fromFile(imageFile))
        var requestBody: RequestBody =
            MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uploaded_file",
                    imageFilename,
                    imageFile.asRequestBody(mimeType?.toMediaTypeOrNull()))
                .build()

        var partImage = MultipartBody.Part.createFormData("image", imageFile.name, requestBody)
        var uploading: Call<UserMendaurUlang> = ApiInstance.userService.uploadTransaksi(
            partImage,
            userDetail[SessionManager.ID].toString().toRequestBody("text/plain".toMediaTypeOrNull()),
            "Sampah Plastik ex".toRequestBody("text/plain".toMediaTypeOrNull()) ,
            "Tps Ex".toRequestBody("text/plain".toMediaTypeOrNull()),
            "1".toRequestBody("text/plain".toMediaTypeOrNull()),
            date.toRequestBody("text/plain".toMediaTypeOrNull())
        )
        uploading.enqueue(object : Callback<UserMendaurUlang>{
            override fun onResponse(
                call: Call<UserMendaurUlang>,
                response: Response<UserMendaurUlang>
            ) {
                startActivity(Intent(this@PerhitunganActivity,MainPageActivity::class.java))
            }

            override fun onFailure(call: Call<UserMendaurUlang>, t: Throwable) {
                Toast.makeText(this@PerhitunganActivity,"${t.message}",Toast.LENGTH_LONG).show()
            }
        })
    }

    // url = file path or whatever suitable URL you want.
    fun getMimeType(file: File): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(file.path)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }
}
