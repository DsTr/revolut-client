package com.example.dmitrykostin.revolut_client.mvp.representer

import com.example.dmitrykostin.revolut_client.CredentialsHolder
import com.example.dmitrykostin.revolut_client.mvp.model.TransactionsListModel
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction
import com.example.dmitrykostin.revolut_client.util.CredentialsKeeper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class TransactionsListRepresenter(val credentialsKeeper: CredentialsKeeper, val transactionsListModel: TransactionsListModel) : BaseRepresenter() {
    enum class ReasonToLoginUser {
        FIRST_LAUNCH,
        EXPIRED_TOKEN,
        SIGNOUT
    }

    var newTransactionsToDisplayCb : (Collection<Transaction>) -> Unit = {}
    var needNewUserCredentialsCb : (ReasonToLoginUser) -> Unit = {}
    var networkFailureCb : () -> Unit = {}
    var switchLoaderStateCb : (Boolean) -> Unit = {}

    fun load() {
        loadTransactions()
    }

    fun logOutUser() {
        credentialsKeeper.clearCredentials()
        needNewUserCredentialsCb(ReasonToLoginUser.SIGNOUT)
    }

    fun newCredentialsRetrievedFromUser(userId: String, token: String) {
        val credentialsHolder = CredentialsHolder(userId, token)
        credentialsKeeper.saveCredentialsHolder(credentialsHolder)
        loadTransactions()
    }

    private fun loadTransactions() = launch {
        val credentialsHolder = credentialsKeeper.getCredentialsHolder()
        if (credentialsHolder != null) {
            switchLoaderStateCb(true)
            val (transactionList, err) = async(Dispatchers.IO) {
                transactionsListModel.loadMore(credentialsHolder)
            }.await()

            if (transactionList != null) {
                newTransactionsToDisplayCb(transactionList)
            } else {
                if (err?.loadErrorType == TransactionsListModel.LoadErrorType.WRONG_CREDENTIALS) {
                    needNewUserCredentialsCb(ReasonToLoginUser.EXPIRED_TOKEN)
                } else {
                    networkFailureCb()
                }
            }
        } else {
            // Did not have token before
            needNewUserCredentialsCb(ReasonToLoginUser.FIRST_LAUNCH)
        }
        switchLoaderStateCb(false)
    }
}