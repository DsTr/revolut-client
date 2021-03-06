package com.example.dmitrykostin.revolut_client.revolut_api.response

import com.squareup.moshi.Json

data class Counterpart(
    @Json(name = "amount") val amount: Int?,
    @Json(name = "currency") val currency: String?
)