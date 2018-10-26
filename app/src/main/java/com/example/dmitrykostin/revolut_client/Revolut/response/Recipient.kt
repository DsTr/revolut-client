package com.example.dmitrykostin.revolut_client.Revolut.response

import com.squareup.moshi.Json

data class Recipient(
    @Json(name = "id") val id: String?,
    @Json(name = "phone") val phone: String?,
    @Json(name = "firstName") val firstName: String?,
    @Json(name = "lastName") val lastName: String?
)