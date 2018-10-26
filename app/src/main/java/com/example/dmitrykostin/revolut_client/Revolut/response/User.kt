package com.example.dmitrykostin.revolut_client.Revolut.response

import com.squareup.moshi.Json

data class User(
    @Json(name = "id") val id: String?,
    @Json(name = "createdDate") val createdDate: Long?,
    @Json(name = "address") val address: Address?,
    @Json(name = "birthDate") val birthDate: List<Int?>?,
    @Json(name = "firstName") val firstName: String?,
    @Json(name = "lastName") val lastName: String?,
    @Json(name = "phone") val phone: String?,
    @Json(name = "email") val email: String?,
    @Json(name = "emailVerified") val emailVerified: Boolean?,
    @Json(name = "locked") val locked: Boolean?,
    @Json(name = "state") val state: String?,
    @Json(name = "referralCode") val referralCode: String?,
    @Json(name = "underReview") val underReview: Boolean?,
    @Json(name = "riskAssessed") val riskAssessed: Boolean?,
    @Json(name = "kyc") val kyc: String?
)