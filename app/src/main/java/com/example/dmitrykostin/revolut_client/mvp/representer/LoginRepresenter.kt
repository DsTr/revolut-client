package com.example.dmitrykostin.revolut_client.mvp.representer

import com.example.dmitrykostin.revolut_client.revolut_api.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginRepresenter : BaseRepresenter() {
    private val api by lazy {
        Api()
    }

    enum class LoginActivityState {
        LOGIN,
        LOADER,
        CONFIRMATION,
    }

    var switchViewStateCb : (LoginActivityState) -> Unit = {}
    var wrongCredentialsCb : () -> Unit = {}
    var wrongConfirmationNumberCb : () -> Unit = {}
    // User sucessfully confirmed SMS code
    var credentialsReceivedCb : ((login: String, token: String) -> Unit) = {_,_ -> }

    fun cancelConfirmation() {
        switchViewStateCb(LoginActivityState.LOGIN)
    }

    fun tryToLogin(phone: String, user_password: String) = launch {
        if (phone.isEmpty() || user_password.isEmpty()) {
            wrongCredentialsCb()
        } else {
            switchViewStateCb(LoginActivityState.LOADER)

            val (_, err) = async(Dispatchers.Default) {
                api.signIn(phone, user_password)
            }.await()

            if (err == null) {
                switchViewStateCb(LoginActivityState.CONFIRMATION)
            } else {
                switchViewStateCb(LoginActivityState.LOGIN)
                wrongCredentialsCb()
            }
        }
    }

    fun processConfirmRequest(phone: String, code: String) = launch {
        if (phone.isEmpty()) {
            switchViewStateCb(LoginActivityState.LOGIN)
        } else if (code.isEmpty()) {
            wrongConfirmationNumberCb()
        } else {
            switchViewStateCb(LoginActivityState.LOADER)
            val (confirmResponse, err) = async(Dispatchers.Default) {
                api.confirm(phone, code)
            }.await()
            if (err == null && confirmResponse != null) {
                credentialsReceivedCb(confirmResponse.user.id, confirmResponse.accessToken)
            } else {
                wrongConfirmationNumberCb()
                switchViewStateCb(LoginActivityState.CONFIRMATION)
            }
        }
    }
}