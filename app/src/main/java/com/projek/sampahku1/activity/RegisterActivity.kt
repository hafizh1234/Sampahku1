package com.projek.sampahku1.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.projek.sampahku1.api.ApiInstance
import com.projek.sampahku1.databinding.ActivityRegisterBinding
import com.projek.sampahku1.model.RegistrationPureResponse
import com.projek.sampahku1.model.RequestRegistration
import com.projek.sampahku1.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val etFullname = binding.etNamaLengkap
        val etEmail = binding.etEmail
        val etPassword = binding.etPassword
        val etUsername = binding.etUsername
        binding.btnRegister.setOnClickListener {
            var textAdaYangEmpty = false
            if (etEmail.text.isEmpty()) {
                textAdaYangEmpty = true
                etEmail.error = "Email tidak boleh kosong"
            }
            if (etUsername.text.isEmpty()) {
                textAdaYangEmpty = true
                etUsername.error = "Username tidak boleh kosong"
            }
            if (etPassword.text.isEmpty()) {
                textAdaYangEmpty = true
                etPassword.error = "Password tidak boleh kosong"
            }
            if (etFullname.text.isEmpty()) {
                textAdaYangEmpty = true
                etFullname.error = "Fullname tidak boleh kosong"
            }
            if(!textAdaYangEmpty) {
                val registerRequest = RequestRegistration(
                    etUsername.text.toString(),
                    etFullname.text.toString(),
                    etEmail.text.toString(),
                    etPassword.text.toString()

                )
                registerUser(registerRequest)
            }

        }
    }

    fun registerUser(registerRequest: RequestRegistration) {
        var registerResponseCall: Call<RegistrationPureResponse> =
            ApiInstance.userService.regis(registerRequest)
        registerResponseCall.enqueue(object : Callback<RegistrationPureResponse> {
            override fun onResponse(
                call: Call<RegistrationPureResponse>,
                response: Response<RegistrationPureResponse>
            ) {
                if (response.isSuccessful) {
                    val responseRegis=response.body()
                    val data=responseRegis?.register
                    val message = "Successful register"
                    Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_LONG).show()
                    val sessionManager:SessionManager=SessionManager(this@RegisterActivity)
                    sessionManager.createLoginSession(data?.username.toString(),data?.password.toString(),data?.fullname.toString(),data?.email.toString())

                    startActivity(Intent(this@RegisterActivity, MainPageActivity::class.java))
                }
                    else{
                        Toast.makeText(this@RegisterActivity,
                            "Tidak bisa mendaftar sekarang, silahkan coba lagi beberapa saat",
                            Toast.LENGTH_LONG).show()
                    }
                }


            override fun onFailure(call: Call<RegistrationPureResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_LONG).show()
            }

        })
    }
}