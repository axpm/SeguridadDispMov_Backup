package com.uc3m.searchyourrecipe.api

import com.uc3m.searchyourrecipe.util.Constants.Companion.EDAMAM_URL
import com.uc3m.searchyourrecipe.util.Constants.Companion.EDAMAM_URL_SHORT
import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitInstance {

    private val retrofit by lazy{

       var certificatePinner = CertificatePinner.Builder()
               .add(EDAMAM_URL_SHORT , "sha256/UfOJLsqvGfMx0qopRv8ecI/MKkpY/M8BjKD7XRAVCsU=").build()

       val okHttpClient = OkHttpClient.Builder().certificatePinner(certificatePinner).build()

       Retrofit.Builder()
       .baseUrl(EDAMAM_URL)
       .addConverterFactory(GsonConverterFactory.create())
       .client(okHttpClient)
       .build()
   }

    val edamamAPI: EdamamAPI by lazy{
        retrofit.create(EdamamAPI::class.java)
    }
}