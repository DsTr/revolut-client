package com.example.dmitrykostin.revolut_client.mvp.representer

import com.example.dmitrykostin.revolut_client.CredentialsHolder
import com.example.dmitrykostin.revolut_client.mvp.activity.TransactionsListActivityInterface
import com.example.dmitrykostin.revolut_client.mvp.model.TransactionsListModel
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction
import com.example.dmitrykostin.revolut_client.util.CredentialsKeeper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class RevolutTransactionsListRepresenter(val credentialsKeeper: CredentialsKeeper, val transactionsListModel: TransactionsListModel) : BaseRepresenter(), TransactionListRepresenterInterface {

    var transactionsListActivity: TransactionsListActivityInterface? = null
    private val transactionsToDisplay: ArrayList<Transaction> = ArrayList(0)
    private var loadingInProgress: AtomicBoolean = AtomicBoolean(false)

    override fun attachView(view: TransactionsListActivityInterface) {
        transactionsListActivity = view
    }

    override fun detachView() {
        transactionsListActivity = null
    }

    override fun firstLoad() {
        if (!transactionsToDisplay.isEmpty()) {
            transactionsListActivity?.gotNewDatasetToDisplay(transactionsToDisplay)
        } else if(loadingInProgress.get()) {
            transactionsListActivity?.doSwitchLoaderState(true)
        } else {
            loadTransactionsFromApi()
        }
    }

    override fun logOutUser() {
        credentialsKeeper.clearCredentials()
        transactionsListActivity?.doNewUserCredentialsRequest(TransactionListRepresenterInterface.ReasonToLoginUser.SIGNOUT)
    }

    override fun newCredentialsRetrievedFromUser(userId: String, token: String) {
        val credentialsHolder = CredentialsHolder(userId, token)
        credentialsKeeper.saveCredentialsHolder(credentialsHolder)
        loadTransactionsFromApi()
    }

    private fun loadTransactionsFromApi() = launch {
        val credentialsHolder = credentialsKeeper.getCredentialsHolder()
        if (credentialsHolder != null) {
            // WIP
            if (loadingInProgress.getAndSet(true)) {
                return@launch
            }

            transactionsListActivity?.doSwitchLoaderState(true)
            val (newTransactionList, err) = async(Dispatchers.IO) {
                transactionsListModel.loadMore(credentialsHolder)
            }.await()

            loadingInProgress.set(false)

            if (newTransactionList != null) {
                transactionsToDisplay.addAll(newTransactionList)
                transactionsListActivity?.gotNewDatasetToDisplay(transactionsToDisplay)
            } else {
                if (err?.loadErrorType == TransactionsListModel.LoadErrorType.WRONG_CREDENTIALS) {
                    transactionsListActivity?.doNewUserCredentialsRequest(TransactionListRepresenterInterface.ReasonToLoginUser.EXPIRED_TOKEN)
                } else {
                    transactionsListActivity?.gotNetworkFailure()
                }
            }
        } else {
            // Did not have token before
            transactionsListActivity?.doNewUserCredentialsRequest(TransactionListRepresenterInterface.ReasonToLoginUser.FIRST_LAUNCH)
        }
        transactionsListActivity?.doSwitchLoaderState(false)
    }
}