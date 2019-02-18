package com.example.dmitrykostin.revolut_client.mvp.model

import com.example.dmitrykostin.revolut_client.revolut_api.Credentials
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction
import com.github.kittinunf.result.Result

interface TransactionsListModel {
    enum class LoadErrorType {
        NETWORK_ERROR,
        WRONG_CREDENTIALS
    }

    class LoadError(private val exception: Exception, val loadErrorType: LoadErrorType) : Exception(exception) {
        override fun toString(): String = "${exception.javaClass.canonicalName}: ${exception.message ?: "<no message>"}"
    }

    fun loadMore(credentials: Credentials): Result<Collection<Transaction>, LoadError>
}