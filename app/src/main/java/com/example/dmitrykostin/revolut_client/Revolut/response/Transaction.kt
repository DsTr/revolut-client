package com.example.dmitrykostin.revolut_client.Revolut.response

import com.squareup.moshi.Json

typealias TransactionList = List<Transaction> ;

data class Transaction(
    @Json(name = "id") val id: String?,
    @Json(name = "legId") val legId: String?,
    @Json(name = "type") val type: String?,
    @Json(name = "state") val state: String?,
    @Json(name = "startedDate") val startedDate: Long?,
    @Json(name = "updatedDate") val updatedDate: Long?,
    @Json(name = "completedDate") val completedDate: Long?,
    @Json(name = "currency") val currency: String?,
    @Json(name = "amount") val amount: Int?,
    @Json(name = "fee") val fee: Int?,
    @Json(name = "balance") val balance: Int?,
    @Json(name = "description") val description: String?,
    @Json(name = "rate") val rate: Int?,
    @Json(name = "merchant") val merchant: Merchant?,
    @Json(name = "counterpart") val counterpart: Counterpart?,
    @Json(name = "reason") val reason: String?,
    @Json(name = "comment") val comment: String?,
    @Json(name = "recipient") val recipient: Recipient?,
    @Json(name = "logo") val logo: String?
)