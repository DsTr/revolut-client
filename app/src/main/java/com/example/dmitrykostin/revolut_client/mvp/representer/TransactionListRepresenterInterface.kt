package com.example.dmitrykostin.revolut_client.mvp.representer

import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction

interface TransactionListRepresenterInterface: BaseRepresenterInferface {
    enum class ReasonToLoginUser {
        FIRST_LAUNCH,
        EXPIRED_TOKEN,
        SIGNOUT
    }
    fun load()
    fun logOutUser()
    fun newCredentialsRetrievedFromUser(userId: String, token: String)
}