package com.example.dmitrykostin.revolut_client.mvp.presenter

import com.example.dmitrykostin.revolut_client.mvp.activity.TransactionsListView

interface TransactionListPresenter : BasePresenter {
    enum class ReasonToLoginUser {
        FIRST_LAUNCH,
        EXPIRED_TOKEN,
        SIGNOUT
    }

    fun viewLoaded()
    fun loadMoreClick()
    fun logOutClick()
    fun newCredentialsRetrievedFromUser(userId: String, token: String)
    fun attachView(view: TransactionsListView)
    fun detachView()
}