package com.example.dmitrykostin.revolut_client.mvp.presenter

import com.example.dmitrykostin.revolut_client.mvp.activity.LoginView
import com.example.dmitrykostin.revolut_client.revolut_api.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RevolutLoginPresenter(val loginView: LoginView) : CoroutinePresenter(), LoginPresenter {
    private val api by lazy {
        Api()
    }

    override fun userCancelClicked() {
        loginView.switchViewStateCb(LoginPresenter.LoginState.LOGIN)
    }

    override fun userLogInClicked(phone: String, user_password: String) = launch {
        if (phone.isEmpty() || user_password.isEmpty()) {
            loginView.showWrongCredentials()
        } else {
            loginView.switchViewStateCb(LoginPresenter.LoginState.LOADER)

            val (_, err) = async(Dispatchers.Default) {
                api.signIn(phone, user_password)
            }.await()

            if (err == null) {
                loginView.switchViewStateCb(LoginPresenter.LoginState.CONFIRMATION)
            } else {
                loginView.switchViewStateCb(LoginPresenter.LoginState.LOGIN)
                loginView.showWrongCredentials()
            }
        }
    }

    override fun userConfirmSmsClicked(phone: String, code: String) = launch {
        if (phone.isEmpty()) {
            loginView.switchViewStateCb(LoginPresenter.LoginState.LOGIN)
        } else if (code.isEmpty()) {
            loginView.showWrongConfirmationNumber()
        } else {
            loginView.switchViewStateCb(LoginPresenter.LoginState.LOADER)
            val (confirmResponse, err) = async(Dispatchers.Default) {
                api.confirm(phone, code)
            }.await()
            if (err == null && confirmResponse != null) {
                loginView.submitUserCredentials(confirmResponse.user.id, confirmResponse.accessToken)
            } else {
                loginView.showWrongConfirmationNumber()
                loginView.switchViewStateCb(LoginPresenter.LoginState.CONFIRMATION)
            }
        }
    }
}