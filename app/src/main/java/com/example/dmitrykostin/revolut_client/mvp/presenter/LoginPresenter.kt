package com.example.dmitrykostin.revolut_client.mvp.presenter

import kotlinx.coroutines.Job

interface LoginPresenter: BasePresenter {
    enum class LoginActivityState {
        LOGIN,
        LOADER,
        CONFIRMATION,
    }
    fun userCanceledConfirmation()
    fun tryToLogin(phone: String, user_password: String): Job
    fun processConfirmRequest(phone: String, code: String): Job
}