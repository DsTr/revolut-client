package com.example.dmitrykostin.revolut_client.mvp.activity

import com.example.dmitrykostin.revolut_client.mvp.presenter.TransactionListPresenter
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction

interface TransactionsListView {
    fun displayNewDataset(newTransactionsList: List<Transaction>)
    fun openAuthorizationDialog(reasonToRequest: TransactionListPresenter.ReasonToLoginUser)
    fun displayNetworkFailure()
    fun doSwitchLoaderState(loadingState: Boolean)
}