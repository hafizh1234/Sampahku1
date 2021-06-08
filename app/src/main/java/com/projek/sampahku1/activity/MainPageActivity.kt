package com.projek.sampahku1.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.projek.sampahku1.R
import com.projek.sampahku1.databinding.ActivityMainPageBinding
import com.projek.sampahku1.model.RegistrationResponse
import com.projek.sampahku1.model.ResponseLogin
import com.projek.sampahku1.session.SessionManager

class MainPageActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar=binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title="Pengambilan hasil"
        val sessionManager:SessionManager=SessionManager(this)
        val userDetail=sessionManager.getUsersDetail()
        binding.namaToolbar.text="Hello, ${userDetail.get(SessionManager.KEY_USERNAME)}"

        binding.cardviewTransaksi.setOnClickListener{
            val intent=Intent(this@MainPageActivity, PerhitunganActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_logout ->{

            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val DATA_USER_LOGIN = "data_user_login"
        const val DATA_USER_REGISTER="data user register"
    }

}