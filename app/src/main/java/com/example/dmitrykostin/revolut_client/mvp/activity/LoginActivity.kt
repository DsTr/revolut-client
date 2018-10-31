package com.example.dmitrykostin.revolut_client.mvp.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.EditorInfo
import com.example.dmitrykostin.revolut_client.R
import com.example.dmitrykostin.revolut_client.mvp.representer.LoginRepresenter
import com.example.dmitrykostin.revolut_client.revolut_api.Api

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*

class LoginActivity : BaseActivity() {

    lateinit var loginRepresenter: LoginRepresenter;

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

        loginRepresenter = LoginRepresenter()
        loginRepresenter.switchViewStateCb = {switchFormState(it)}
        loginRepresenter.wrongCredentialsCb = {
            password.error = getString(R.string.error_invalid_password)
            password.requestFocus()
        }
        loginRepresenter.wrongConfirmationNumberCb = {
            sms_code_input.error = "Wrong code";
        }
        loginRepresenter.credentialsReceivedCb = { userId, accessToken ->
            val result = Intent()
            result.putExtra(LoginActivity.INTENT_KEY_USER_ID, userId)
            result.putExtra(LoginActivity.INTENT_KEY_TOKEN, accessToken)
            setResult(Activity.RESULT_OK, result)
            finish()
        }

        cancel_confirm_sms_button.setOnClickListener { loginRepresenter.cancelConfirmation() }
    }

    private fun attemptLogin() {
        // Reset errors.
        phone.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val phoneStr = phone.text.toString()
        val passwordStr = password.text.toString()

        loginRepresenter.tryToLogin(phoneStr, passwordStr)
    }

    private fun attemptConfirm() {
        sms_code_input.error = null

        // Store values at the time of the login attempt.
        val phoneStr = phone.text.toString()
        val smsCodeText = sms_code_input.text.toString()

        loginRepresenter.processConfirmRequest(phoneStr, smsCodeText)
    }

    /**
     * Changes the current visible form
     */
    private fun switchFormState(activityState: LoginRepresenter.LoginActivityState) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        val stateToFormMap = arrayListOf<Pair<View, LoginRepresenter.LoginActivityState>>(
            Pair(login_form,
                LoginRepresenter.LoginActivityState.LOGIN
            ),
            Pair(progress, LoginRepresenter.LoginActivityState.LOADER),
            Pair(confirm_form,
                LoginRepresenter.LoginActivityState.CONFIRMATION
            )
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
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        val phoneStr = phone.text.toString()
        outState?.putString(LAST_ENTERED_PHONE_ID, phoneStr)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val phoneStr = phone.text.toString()
        val savedPhone = savedInstanceState?.getString(LAST_ENTERED_PHONE_ID)
        if (null != savedPhone) {
            // TODO: Можно ли нормально вытавить текст без 2 методов?
            with(phone.text) {
                clear()
                insert(0, savedPhone)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loginRepresenter.destroy()
    }

    companion object {
        val INTENT_KEY_TOKEN = "access_token";
        val INTENT_KEY_USER_ID = "user_id";
        val LAST_ENTERED_PHONE_ID = "android:enteredPhone"
    }
}
