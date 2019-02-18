package com.example.dmitrykostin.revolut_client.mvp.presenter

import kotlinx.coroutines.Job

interface LoginPresenter : BasePresenter {
    enum class LoginState {
        LOGIN,
        LOADER,
        CONFIRMATION,
    }

    fun userCancelClicked()
    fun userLogInClicked(phone: String, user_password: String): Job
    fun userConfirmSmsClicked(phone: String, code: String): Job
}