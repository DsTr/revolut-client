package com.example.dmitrykostin.revolut_client.mvp.activity

import com.example.dmitrykostin.revolut_client.mvp.representer.TransactionListRepresenterInterface
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction

interface TransactionsListActivityInterface {
    fun gotNewDatasetToDisplay(newTransactionsList: List<Transaction>)
    fun doNewUserCredentialsRequest(reasonToRequest: TransactionListRepresenterInterface.ReasonToLoginUser)
    fun gotNetworkFailure()
    fun doSwitchLoaderState(loadingState: Boolean)
}