package com.projek.sampahku1.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiInstance {


    companion object{
        var userService=getRetrofit().create(UserService::class.java)
        fun getRetrofit(): Retrofit {
            val BaseURL = "https://sampahku-server.herokuapp.com/api/v1/member/"
            val httpLogingInterceptor = HttpLoggingInterceptor()
            httpLogingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient: OkHttpClient =
                OkHttpClient.Builder().addInterceptor(httpLogingInterceptor).build()
            val retrofit: Retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BaseURL)
                .client(okHttpClient)
                .build()
            return retrofit
        }

    }
}


