package com.example.dmitrykostin.revolut_client.revolut_api.response

import com.squareup.moshi.Json

data class Pocket(
    @Json(name = "id") val id: String?,
    @Json(name = "state") val state: String?,
    @Json(name = "currency") val currency: String?,
    @Json(name = "balance") val balance: Int?,
    @Json(name = "blockedAmount") val blockedAmount: Int?
)