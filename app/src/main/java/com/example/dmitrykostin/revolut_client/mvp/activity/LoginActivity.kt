package com.example.dmitrykostin.revolut_client.mvp.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import com.example.dmitrykostin.revolut_client.R
import com.example.dmitrykostin.revolut_client.mvp.presenter.LoginPresenter
import com.example.dmitrykostin.revolut_client.mvp.presenter.RevolutLoginPresenter

import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), LoginView {

    private lateinit var loginPresenter: LoginPresenter

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

        //phone.text.insert(0,"+4407446744008");
        email_sign_in_button.setOnClickListener { attemptLogin() }
        confirm_sms_button.setOnClickListener { attemptConfirm() }

        loginPresenter = createConcreteLoginRepresenter()

        cancel_confirm_sms_button.setOnClickListener { loginPresenter.userCancelClicked() }
    }

    private fun createConcreteLoginRepresenter(): LoginPresenter {
        return RevolutLoginPresenter(this)
    }

    override fun showWrongCredentials() {
        password.error = getString(R.string.error_invalid_password)
        password.requestFocus()
    }

    override fun showWrongConfirmationNumber() {
        sms_code_input.error = "Wrong code"
    }

    override fun submitUserCredentials(userId: String, accessToken: String) {
        val result = Intent()
        result.putExtra(LoginActivity.INTENT_KEY_USER_ID, userId)
        result.putExtra(LoginActivity.INTENT_KEY_TOKEN, accessToken)
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    private fun attemptLogin() {
        // Reset errors.
        phone.error = null
        password.error = null

        // Store values at the time of the login attempt.
        val phoneStr = phone.text.toString()
        val passwordStr = password.text.toString()

        loginPresenter.userLogInClicked(phoneStr, passwordStr)
    }

    private fun attemptConfirm() {
        sms_code_input.error = null

        // Store values at the time of the login attempt.
        val phoneStr = phone.text.toString()
        val smsCodeText = sms_code_input.text.toString()

        loginPresenter.userConfirmSmsClicked(phoneStr, smsCodeText)
    }

    /**
     * Changes the current visible form
     */
    override fun switchViewStateCb(loginState: LoginPresenter.LoginState) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

        val stateToFormMap = arrayListOf<Pair<View, LoginPresenter.LoginState>>(
            Pair(
                login_form,
                LoginPresenter.LoginState.LOGIN
            ),
            Pair(progress, LoginPresenter.LoginState.LOADER),
            Pair(
                confirm_form,
                LoginPresenter.LoginState.CONFIRMATION
            )
        )

        for ((view, mappedState) in stateToFormMap) {
            view.visibility = if (loginState == mappedState) View.VISIBLE else View.GONE
            view.animate()
                .setDuration(shortAnimTime)
                .alpha((if (loginState == mappedState) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = if (loginState == mappedState) View.VISIBLE else View.GONE
                    }
                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        loginPresenter.destroy()
    }

    companion object {
        const val INTENT_KEY_TOKEN = "access_token"
        const val INTENT_KEY_USER_ID = "user_id"
    }
}
