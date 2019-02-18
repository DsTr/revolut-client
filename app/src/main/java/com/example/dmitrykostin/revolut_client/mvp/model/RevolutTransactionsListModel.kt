package com.example.dmitrykostin.revolut_client.mvp.model

import com.example.dmitrykostin.revolut_client.revolut_api.Api
import com.example.dmitrykostin.revolut_client.revolut_api.Credentials
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.result.Result

class RevolutTransactionsListModel : TransactionsListModel {
    private val api by lazy {
        Api()
    }

    override fun loadMore(credentials: Credentials): Result<Collection<Transaction>, TransactionsListModel.LoadError> {
        val transactionsApiResult = api.getTransactions(credentials)

        transactionsApiResult.fold({
            return Result.Success(it)
        }, {
            val loadErrorType = getErrorTypeByResponse(it.response)
            val error = TransactionsListModel.LoadError(Exception(), loadErrorType)
            return Result.Failure(error)
        })
    }

    private fun getErrorTypeByResponse(response: Response): TransactionsListModel.LoadErrorType {
        return if (response.statusCode == 401) TransactionsListModel.LoadErrorType.WRONG_CREDENTIALS else TransactionsListModel.LoadErrorType.NETWORK_ERROR
    }
}