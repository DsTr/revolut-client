package com.example.dmitrykostin.revolut_client.mvp.activity

import com.example.dmitrykostin.revolut_client.mvp.representer.LoginRepresenterInterface

interface LoginActivityInterface {
    fun switchViewStateCb(loginActivityState: LoginRepresenterInterface.LoginActivityState)
    fun gotWrongCredentials()
    fun gotWrongConfirmationNumber()
    // User sucessfully confirmed SMS code
    fun gotUserCredentials(userId: String, accessToken: String)
}