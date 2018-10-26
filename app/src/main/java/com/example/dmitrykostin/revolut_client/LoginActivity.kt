package com.example.dmitrykostin.revolut_client

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import com.example.dmitrykostin.revolut_client.Revolut.Api

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*

class LoginActivity : BaseActivityWithCoroutineScope() {
    private enum class LoginActivityState {
        LOGIN,
        LOADER,
        CONFIRMATION,
    }

    private val api by lazy {
        Api()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        password.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                true
            } else {
                false
            }
        }

        phone.text.insert(0,"+4407446744008");
        email_sign_in_button.setOnClickListener { attemptLogin() }
        confirm_sms_button.setOnClickListener { attemptConfirm() }
        cancel_confirm_sms_button.setOnClickListener { switchFormState(LoginActivityState.LOGIN) }
    }

    private fun attemptLogin() {
        // Reset errors.
        phone.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val phoneStr = phone.text.toString()
        val passwordStr = password.text.toString()

        var cancel = false

        // Check for a valid email address.
        if (TextUtils.isEmpty(phoneStr)) {
            phone.error = getString(R.string.error_field_required)
            phone.requestFocus()
            cancel = true
        }

        if (!cancel) {
            launchLogin(phoneStr, passwordStr)
        }
    }

    private fun launchLogin(phone: String, user_password: String) = launch {
        switchFormState(LoginActivityState.LOADER)

        val (_, err) = async(Dispatchers.Default) {
            api.signIn(phone, user_password)
        }.await()

        if (err == null) {
            switchFormState(LoginActivityState.CONFIRMATION)
        } else {
            password.error = getString(R.string.error_invalid_password)
            password.requestFocus()
            switchFormState(LoginActivityState.LOGIN)
        }
    }

    private fun attemptConfirm() {
        sms_code_input.error = null

        // Store values at the time of the login attempt.
        val smsCodeText = sms_code_input.text.toString()
        val phoneStr = phone.text.toString()

        var cancel = false

        // Check for a valid email address.
        if (TextUtils.isEmpty(smsCodeText)) {
            sms_code_input.error = getString(R.string.error_field_required)
            sms_code_input.requestFocus()
            cancel = true
        }

        if (!cancel) {
            processConfirmRequest(phoneStr, smsCodeText)
        }
    }

    private fun processConfirmRequest(phone: String, code: String) = launch {
        switchFormState(LoginActivityState.LOADER)

        val (confirmResponse, err) = async(Dispatchers.Default) {
            api.confirm(phone, code)
        }.await()
        Log.d("DEBUGGG", phone)
        if (err == null && confirmResponse != null) {
            val result = Intent()
            result.putExtra(INTENT_KEY_USER_ID, confirmResponse.user.id)
            result.putExtra(INTENT_KEY_TOKEN, confirmResponse.accessToken)
            setResult(Activity.RESULT_OK, result)
            finish()
        } else {
            sms_code_input.error = "Wrong code";
            switchFormState(LoginActivityState.CONFIRMATION)
        }
    }

    /**
     * Changes the current visible form
     */
    private fun switchFormState(activityState: LoginActivityState) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        val stateToFormMap = arrayListOf<Pair<View, LoginActivityState>>(
            Pair(login_form, LoginActivityState.LOGIN),
            Pair(progress, LoginActivityState.LOADER),
            Pair(confirm_form, LoginActivityState.CONFIRMATION)
        )

        for ( (view, mappedState) in stateToFormMap) {
            view.visibility = if (activityState == mappedState) View.VISIBLE else View.GONE
            view.animate()
                .setDuration(shortAnimTime)
                .alpha((if (activityState == mappedState) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = if (activityState == mappedState) View.VISIBLE else View.GONE
                    }
                })
        }
//        login_form.visibility = if (activityState == LoginActivityState.LOGIN) View.VISIBLE else View.GONE
//        login_form.animate()
//            .setDuration(shortAnimTime)
//            .alpha((if (show) 0 else 1).toFloat())
//            .setListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationEnd(animation: Animator) {
//                    login_form.visibility = if (show) View.GONE else View.VISIBLE
//                }
//            })
//
//        progress.visibility = if (show) View.VISIBLE else View.GONE
//        progress.animate()
//            .setDuration(shortAnimTime)
//            .alpha((if (show) 1 else 0).toFloat())
//            .setListener(object : AnimatorListenerAdapter() {
//                override fun onAnimationEnd(animation: Animator) {
//                    progress.visibility = if (show) View.VISIBLE else View.GONE
//                }
//            })
    }

    companion object {
        val TOKEN_REPLY = "com.example.dmitrykostin.revolut_client";
        val INTENT_KEY_TOKEN = "access_token";
        val INTENT_KEY_USER_ID = "user_id";
    }
}
