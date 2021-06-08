package com.projek.sampahku1.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.projek.sampahku1.api.ApiInstance
import com.projek.sampahku1.databinding.ActivityLoginBinding
import com.projek.sampahku1.model.RequestLogin

import com.projek.sampahku1.model.ResponseLogin
import com.projek.sampahku1.session.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val etUsername=binding.etUsername
        val etPassword=binding.etPassword
        binding.btnLogin.setOnClickListener {
            var textAdaYangEmpty: Boolean = false

            if (etUsername.text.isEmpty()) {
                textAdaYangEmpty = true
                etUsername.error = "Username tidak boleh kosong"
            }
            if (etPassword.text.isEmpty()) {
                textAdaYangEmpty = true
                etPassword.error = "Password tidak boleh kosong"
            }
            if(textAdaYangEmpty==false) {
                val loginRequest: RequestLogin = RequestLogin(

                    etUsername.text.toString(),
                    etPassword.text.toString()
                )
                loginUser(loginRequest)
            }
            else{textAdaYangEmpty=false}

        }
    }

    private fun loginUser(loginRequest: RequestLogin) {
        val responseLogin: Call<ResponseLogin> =ApiInstance.userService.login(loginRequest)
        responseLogin.enqueue(object :Callback<ResponseLogin>{
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if(response.isSuccessful) {
                    val loginResponse=response.body()
                    val sessionManager: SessionManager=SessionManager(this@LoginActivity)
                    if (loginResponse != null) {
                        sessionManager.createLoginSession(loginResponse.username.toString(),loginResponse.password.toString(),loginResponse.fullname.toString(),loginResponse.email.toString())
                    }
                    startActivity(Intent(this@LoginActivity,MainPageActivity::class.java))
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
               Toast.makeText(this@LoginActivity,"gagal masuk",Toast.LENGTH_LONG).show()
            }

        })
    }

}

