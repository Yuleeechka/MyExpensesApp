package com.example.myexpenses.data

import retrofit2.http.GET
import retrofit2.http.Path

interface NbrbApi {
    @GET("exrates/rates/{id}")
    suspend fun getRate(@Path("id") id: Int): RateResponse
}

