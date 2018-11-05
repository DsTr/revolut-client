package com.example.dmitrykostin.revolut_client.mvp.activity

import com.example.dmitrykostin.revolut_client.mvp.representer.TransactionListRepresenterInterface
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction

interface TransactionsListActivityInterface {
    fun gotNewTransactionsToDisplay(newTransactionsList: Collection<Transaction>)
    fun doNewUserCredentialsRequest(reasonToRequest: TransactionListRepresenterInterface.ReasonToLoginUser)
    fun gotNetworkFailure()
    fun doSwitchLoaderState(loadingState: Boolean)
}