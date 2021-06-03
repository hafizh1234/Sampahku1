package com.projek.sampahku1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.projek.sampahku1.databinding.ActivityPerhitunganBinding

class PerhitunganActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPerhitunganBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityPerhitunganBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //camera
    }
}