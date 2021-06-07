package com.projek.sampahku1.api

import android.telecom.Call
import com.projek.sampahku1.model.RequestLogin
import com.projek.sampahku1.model.ResponseLogin
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("login")
    fun login(@Body loginRequest:RequestLogin):Call<ResponseLogin>
}