package com.example.myexpenses.data

data class RateResponse(
    val Cur_ID: Int,
    val Date: String,
    val Cur_Abbreviation: String,
    val Cur_Scale: Int,
    val Cur_Name: String,
    val Cur_OfficialRate: Double
)
