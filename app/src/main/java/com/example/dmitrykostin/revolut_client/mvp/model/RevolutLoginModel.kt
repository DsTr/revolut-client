package com.example.dmitrykostin.revolut_client.mvp.model

import com.example.dmitrykostin.revolut_client.revolut_api.Api

class RevolutLoginModel : LoginModel {
    private val api by lazy {
        Api()
    }

//    override fun loadMoreClick(credentialsHolder: Credentials) : Result<Collection<Transaction>, TransactionsListModel.LoadError> {
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