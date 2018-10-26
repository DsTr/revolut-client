package com.example.dmitrykostin.revolut_client.Revolut.response

import com.squareup.moshi.Json

data class Address(
    @Json(name = "city") val city: String?,
    @Json(name = "country") val country: String?,
    @Json(name = "postcode") val postcode: String?,
    @Json(name = "region") val region: String?,
    @Json(name = "streetLine1") val streetLine1: String?
)