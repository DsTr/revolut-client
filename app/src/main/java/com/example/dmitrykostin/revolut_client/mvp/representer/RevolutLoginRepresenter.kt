package com.example.dmitrykostin.revolut_client.mvp.representer

import com.example.dmitrykostin.revolut_client.mvp.activity.LoginActivityInterface
import com.example.dmitrykostin.revolut_client.revolut_api.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RevolutLoginRepresenter(val loginActivity: LoginActivityInterface) : BaseRepresenter(), LoginRepresenterInterface {
    private val api by lazy {
        Api()
    }

    override fun userCanceledConfirmation() {
        loginActivity.switchViewStateCb(LoginRepresenterInterface.LoginActivityState.LOGIN)
    }

    override fun tryToLogin(phone: String, user_password: String) = launch {
        if (phone.isEmpty() || user_password.isEmpty()) {
            loginActivity.gotWrongCredentials()
        } else {
            loginActivity.switchViewStateCb(LoginRepresenterInterface.LoginActivityState.LOADER)

            val (_, err) = async(Dispatchers.Default) {
                api.signIn(phone, user_password)
            }.await()

            if (err == null) {
                loginActivity.switchViewStateCb(LoginRepresenterInterface.LoginActivityState.CONFIRMATION)
            } else {
                loginActivity.switchViewStateCb(LoginRepresenterInterface.LoginActivityState.LOGIN)
                loginActivity.gotWrongCredentials()
            }
        }
    }

    override fun processConfirmRequest(phone: String, code: String) = launch {
        if (phone.isEmpty()) {
            loginActivity.switchViewStateCb(LoginRepresenterInterface.LoginActivityState.LOGIN)
        } else if (code.isEmpty()) {
            loginActivity.gotWrongConfirmationNumber()
        } else {
            loginActivity.switchViewStateCb(LoginRepresenterInterface.LoginActivityState.LOADER)
            val (confirmResponse, err) = async(Dispatchers.Default) {
                api.confirm(phone, code)
            }.await()
            if (err == null && confirmResponse != null) {
                loginActivity.gotUserCredentials(confirmResponse.user.id, confirmResponse.accessToken)
            } else {
                loginActivity.gotWrongConfirmationNumber()
                loginActivity.switchViewStateCb(LoginRepresenterInterface.LoginActivityState.CONFIRMATION)
            }
        }
    }
}