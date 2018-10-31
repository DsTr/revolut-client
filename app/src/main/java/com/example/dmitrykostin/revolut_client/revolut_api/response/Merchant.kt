package com.example.dmitrykostin.revolut_client.revolut_api.response

import com.squareup.moshi.Json

data class Merchant(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String?,
    @Json(name = "country") val country: String?,
    @Json(name = "city") val city: String?,
    @Json(name = "mcc") val mcc: String?
)