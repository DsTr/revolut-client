package com.example.dmitrykostin.revolut_client.mvp.activity

import com.example.dmitrykostin.revolut_client.mvp.presenter.LoginPresenter

interface LoginActivityInterface {
    fun switchViewStateCb(loginActivityState: LoginPresenter.LoginActivityState)
    fun gotWrongCredentials()
    fun gotWrongConfirmationNumber()
    // User sucessfully confirmed SMS code
    fun gotUserCredentials(userId: String, accessToken: String)
}