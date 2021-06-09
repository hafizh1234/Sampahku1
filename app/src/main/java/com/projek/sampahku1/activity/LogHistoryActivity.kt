package com.projek.sampahku1.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.projek.sampahku1.databinding.ActivityLogHistoryBinding
import com.projek.sampahku1.session.SessionManager

class LogHistoryActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLogHistoryBinding
    private lateinit var sessionManager: SessionManager
    lateinit var userDetail:MutableMap<String,Any?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLogHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager= SessionManager(this)
        userDetail=sessionManager.getUsersDetail()


    }
}