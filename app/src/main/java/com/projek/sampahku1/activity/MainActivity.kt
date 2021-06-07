package com.projek.sampahku1.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.projek.sampahku1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnLogin.setOnClickListener{
            var intent= Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)

        }
        binding.btnRegister.setOnClickListener{
            var intent1=Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent1)
        }
    }
}