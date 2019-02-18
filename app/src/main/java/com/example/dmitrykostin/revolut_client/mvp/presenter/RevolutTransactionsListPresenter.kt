package com.example.dmitrykostin.revolut_client.mvp.presenter

import com.example.dmitrykostin.revolut_client.credentials.CredentialsStorage
import com.example.dmitrykostin.revolut_client.mvp.activity.TransactionsListView
import com.example.dmitrykostin.revolut_client.mvp.model.TransactionsListModel
import com.example.dmitrykostin.revolut_client.revolut_api.Credentials
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

class RevolutTransactionsListPresenter(
    private val credentialsKeeper: CredentialsStorage,
    private val transactionsListModel: TransactionsListModel
) : CoroutinePresenter(), TransactionListPresenter {

    private var transactionsListView: TransactionsListView? = null
    private val transactionsToDisplay: ArrayList<Transaction> = ArrayList(0)
    private var loadingInProgress: AtomicBoolean = AtomicBoolean(false)

    override fun attachView(view: TransactionsListView) {
        transactionsListView = view
    }

    override fun detachView() {
        transactionsListView = null
    }

    override fun viewLoaded() {
        if (!transactionsToDisplay.isEmpty()) {
            transactionsListView?.displayNewDataset(transactionsToDisplay)
        } else if (loadingInProgress.get()) {
            transactionsListView?.doSwitchLoaderState(true)
        } else {
            loadTransactionsFromApi()
        }
    }

    override fun loadMoreClick() {
        if (loadingInProgress.get()) {
            transactionsListView?.doSwitchLoaderState(true)
        } else {
            loadTransactionsFromApi()
        }
    }

    override fun logOutClick() {
        credentialsKeeper.clearCredentials()
        transactionsListView?.openAuthorizationDialog(TransactionListPresenter.ReasonToLoginUser.SIGNOUT)
    }

    override fun newCredentialsRetrievedFromUser(userId: String, token: String) {
        val credentialsHolder = Credentials(userId, token)
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

            transactionsListView?.doSwitchLoaderState(true)
            val (newTransactionList, err) = withContext(Dispatchers.IO) {
                transactionsListModel.loadMore(credentialsHolder)
            }

            loadingInProgress.set(false)

            if (newTransactionList != null) {
                transactionsToDisplay.addAll(newTransactionList)
                transactionsListView?.displayNewDataset(transactionsToDisplay)
            } else {
                if (err?.loadErrorType == TransactionsListModel.LoadErrorType.WRONG_CREDENTIALS) {
                    transactionsListView?.openAuthorizationDialog(TransactionListPresenter.ReasonToLoginUser.EXPIRED_TOKEN)
                } else {
                    transactionsListView?.displayNetworkFailure()
                }
            }
        } else {
            // Did not have token before
            transactionsListView?.openAuthorizationDialog(TransactionListPresenter.ReasonToLoginUser.FIRST_LAUNCH)
        }
        transactionsListView?.doSwitchLoaderState(false)
    }
}