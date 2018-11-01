package com.example.dmitrykostin.revolut_client.mvp.model

import com.example.dmitrykostin.revolut_client.CredentialsHolder
import com.example.dmitrykostin.revolut_client.revolut_api.Api
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction
import com.github.kittinunf.result.Result

class RevolutLoginModel : LoginModel {
    private val api by lazy {
        Api()
    }

//    override fun loadMore(credentialsHolder: CredentialsHolder) : Result<Collection<Transaction>, TransactionsListModel.LoadError> {
//        val transactionsApiResult = api.signIn(credentialsHolder)
//
//        transactionsApiResult.fold({
//            return Result.Success(it)
//        }, {
//            val error = TransactionsListModel.LoadError(Exception(), TransactionsListModel.LoadErrorType.NETWORK_ERROR)
//            return Result.Failure(error)
//        })
//    }
}