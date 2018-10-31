package com.example.dmitrykostin.revolut_client.mvp.model

import com.example.dmitrykostin.revolut_client.CredentialsHolder
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result

interface TransactionsListModel {
    enum class LoadErrorType {
        NETWORK_ERROR,
        WRONG_CREDENTIALS
    }

    class LoadError(val exception: Exception, val loadErrorType: LoadErrorType)
        : Exception(exception) {
        override fun toString(): String = "${exception.javaClass.canonicalName}: ${exception.message ?: "<no message>"}"
    }

    fun loadMore(credentialsHolder: CredentialsHolder) : Result<Collection<Transaction>, LoadError>
}