package com.projek.sampahku1.api

import com.projek.sampahku1.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UserService {
    @POST("login")
    fun login(@Body loginRequest:RequestLogin): Call<ResponseLogin>

    @POST("register")
    fun regis(@Body registerRequest:RequestRegistration): Call<RegistrationPureResponse>

    @Multipart
    @POST("transaksi")
    fun uploadTransaksi(@Part image:MultipartBody.Part,
    @Part("member")member:RequestBody,
    @Part("namaKategori")namaKategori:RequestBody,
    @Part("namaTps")namaTps:RequestBody,
    @Part("berat")berat:RequestBody,
    @Part("tglTransaksi")tglTransaksi:RequestBody):Call<UserMendaurUlang>
}