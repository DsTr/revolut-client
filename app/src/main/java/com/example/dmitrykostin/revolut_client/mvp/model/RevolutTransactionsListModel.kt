package com.example.dmitrykostin.revolut_client.mvp.model

import com.example.dmitrykostin.revolut_client.CredentialsHolder
import com.example.dmitrykostin.revolut_client.revolut_api.Api
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction
import com.github.kittinunf.result.Result
import com.github.kittinunf.result.failure

class RevolutTransactionsListModel : TransactionsListModel {
    private val api by lazy {
        Api()
    }

    override fun loadMore(credentialsHolder: CredentialsHolder) : Result<Collection<Transaction>, TransactionsListModel.LoadError> {
        val transactionsApiResult = api.getTransactions(credentialsHolder)

        transactionsApiResult.fold({
            return Result.Success(it)
        }, {
            val loadErrorType = if (it.response.statusCode == 401) TransactionsListModel.LoadErrorType.WRONG_CREDENTIALS else TransactionsListModel.LoadErrorType.NETWORK_ERROR
            val error = TransactionsListModel.LoadError(Exception(), loadErrorType)
            return Result.Failure(error)
        })
    }
}