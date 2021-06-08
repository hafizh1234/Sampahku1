package com.projek.sampahku1.session

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    lateinit var context:Context
    var usersSession=context.getSharedPreferences("userloginsession",Context.MODE_PRIVATE)
    var editor:SharedPreferences.Editor=usersSession.edit()

    private var isLoggedIn:String="IsLoggedIn"

    companion object{
        var FULL_NAME="fullname"
        var KEY_USERNAME="username"
        var PASSWORD="password"
        var EMAIL="email"
    }
    fun createLoginSession(username:String,password:String,fullname:String,email:String){
        editor.putBoolean(isLoggedIn,true)
        editor.putString(FULL_NAME,fullname)
        editor.putString(EMAIL,email)
        editor.putString(KEY_USERNAME,username)
        editor.putString(PASSWORD,password)
        editor.commit()
    }
    fun getUsersDetail(): MutableMap<String, Any?> {
        val userData:MutableMap<String,Any?> =HashMap()
        userData[FULL_NAME]=usersSession.getString(FULL_NAME,null)
        userData[KEY_USERNAME]=usersSession.getString(KEY_USERNAME,null)
        userData[PASSWORD]=usersSession.getString(PASSWORD,null)
        userData[EMAIL]=usersSession.getString(EMAIL,null)
        return userData
    }
    fun checkLogin(): Boolean {
        return usersSession.getBoolean(isLoggedIn,true)
    }
    fun logoutUserFromSession(){
        editor.clear()
        editor.commit()
    }
}