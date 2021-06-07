package com.projek.sampahku1.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiInstance {
    companion object {
        val BaseURL="https://sampahku-server.herokuapp.com/api/v1/member/"
        val httpLogingInterceptor = HttpLoggingInterceptor()
        val okHttpClient: OkHttpClient =OkHttpClient.Builder().addInterceptor(httpLogingInterceptor).build()
        var retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BaseURL)
            .client(okHttpClient)
            .build()
    }

}


