package com.example.dmitrykostin.revolut_client.Revolut.response

import com.squareup.moshi.Json

data class Wallet(
    @Json(name = "id") val id: String?,
    @Json(name = "ref") val ref: String?,
    @Json(name = "state") val state: String?,
    @Json(name = "baseCurrency") val baseCurrency: String?,
    @Json(name = "topupLimit") val topupLimit: Int?,
    @Json(name = "totalTopup") val totalTopup: Int?,
    @Json(name = "pockets") val pockets: List<Pocket?>?
)