package com.example.dmitrykostin.revolut_client.mvp.activity

import com.example.dmitrykostin.revolut_client.mvp.presenter.LoginPresenter

interface LoginView {
    fun switchViewStateCb(loginState: LoginPresenter.LoginState)
    fun showWrongCredentials()
    fun showWrongConfirmationNumber()
    // User sucessfully confirmed SMS code
    fun submitUserCredentials(userId: String, accessToken: String)
}