package com.example.dmitrykostin.revolut_client.mvp.representer

import com.example.dmitrykostin.revolut_client.CredentialsHolder
import com.example.dmitrykostin.revolut_client.mvp.activity.TransactionsListActivityInterface
import com.example.dmitrykostin.revolut_client.mvp.model.TransactionsListModel
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction
import com.example.dmitrykostin.revolut_client.util.CredentialsKeeper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RevolutTransactionsListRepresenter(val credentialsKeeper: CredentialsKeeper, val transactionsListModel: TransactionsListModel, val transactionsListActivity: TransactionsListActivityInterface) : BaseRepresenter(), TransactionListRepresenterInterface {

    override fun load() {
        loadTransactions()
    }

    override fun logOutUser() {
        credentialsKeeper.clearCredentials()
        transactionsListActivity.doNewUserCredentialsRequest(TransactionListRepresenterInterface.ReasonToLoginUser.SIGNOUT)
    }

    override fun newCredentialsRetrievedFromUser(userId: String, token: String) {
        val credentialsHolder = CredentialsHolder(userId, token)
        credentialsKeeper.saveCredentialsHolder(credentialsHolder)
        loadTransactions()
    }

    private fun loadTransactions() = launch {
        val credentialsHolder = credentialsKeeper.getCredentialsHolder()
        if (credentialsHolder != null) {
            transactionsListActivity.doSwitchLoaderState(true)
            val (transactionList, err) = async(Dispatchers.IO) {
                transactionsListModel.loadMore(credentialsHolder)
            }.await()

            if (transactionList != null) {
                transactionsListActivity.gotNewTransactionsToDisplay(transactionList)
            } else {
                if (err?.loadErrorType == TransactionsListModel.LoadErrorType.WRONG_CREDENTIALS) {
                    transactionsListActivity.doNewUserCredentialsRequest(TransactionListRepresenterInterface.ReasonToLoginUser.EXPIRED_TOKEN)
                } else {
                    transactionsListActivity.gotNetworkFailure()
                }
            }
        } else {
            // Did not have token before
            transactionsListActivity.doNewUserCredentialsRequest(TransactionListRepresenterInterface.ReasonToLoginUser.FIRST_LAUNCH)
        }
        transactionsListActivity.doSwitchLoaderState(false)
    }
}