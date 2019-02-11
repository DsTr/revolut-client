package com.example.dmitrykostin.revolut_client.mvp.presenter

import com.example.dmitrykostin.revolut_client.mvp.activity.TransactionsListActivityInterface

interface TransactionListPresenterInterface: BasePresenter {
    enum class ReasonToLoginUser {
        FIRST_LAUNCH,
        EXPIRED_TOKEN,
        SIGNOUT
    }
    fun activityLoaded()
    fun loadMoreClick()
    fun logOutClick()
    fun newCredentialsRetrievedFromUser(userId: String, token: String)
    fun attachView(view: TransactionsListActivityInterface)
    fun detachView()

}