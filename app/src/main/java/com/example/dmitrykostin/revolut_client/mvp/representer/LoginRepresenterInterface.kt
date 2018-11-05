package com.example.dmitrykostin.revolut_client.mvp.representer

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface LoginRepresenterInterface: BaseRepresenterInferface {
    enum class LoginActivityState {
        LOGIN,
        LOADER,
        CONFIRMATION,
    }
    fun userCanceledConfirmation()
    fun tryToLogin(phone: String, user_password: String): Job
    fun processConfirmRequest(phone: String, code: String): Job
}