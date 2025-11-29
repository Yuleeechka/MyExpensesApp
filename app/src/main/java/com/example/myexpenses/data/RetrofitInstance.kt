package com.example.myexpenses.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    val api: NbrbApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.nbrb.by/api/")  // базовый URL НБ РБ
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NbrbApi::class.java)
    }
}
