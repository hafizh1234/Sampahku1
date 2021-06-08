package com.projek.sampahku1.api

import com.projek.sampahku1.model.RegistrationPureResponse
import com.projek.sampahku1.model.RequestLogin
import com.projek.sampahku1.model.RequestRegistration
import com.projek.sampahku1.model.ResponseLogin
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("login")
    fun login(@Body loginRequest:RequestLogin): Call<ResponseLogin>

    @POST("register")
    fun regis(@Body registerRequest:RequestRegistration): Call<RegistrationPureResponse>
}