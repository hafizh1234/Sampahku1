package com.projek.sampahku1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.projek.sampahku1.databinding.ActivityMainPageBinding

class MainPageActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar=binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title="Pengambilan hasil"

        binding.cardviewTransaksi.setOnClickListener{
            val intent=Intent(this@MainPageActivity,PerhitunganActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_logout->{

            }
        }
        return super.onOptionsItemSelected(item)
    }
}