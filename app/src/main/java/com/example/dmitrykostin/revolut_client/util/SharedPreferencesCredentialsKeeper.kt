package com.example.dmitrykostin.revolut_client.util

import android.content.SharedPreferences
import com.example.dmitrykostin.revolut_client.CredentialsHolder

class SharedPreferencesCredentialsKeeper(val sharedPreferences: SharedPreferences) : CredentialsKeeper {
    companion object {
        val user_id_key = "user_id"
        val token_key = "access_token"
    }

    override fun getCredentialsHolder(): CredentialsHolder? {
        val userId = sharedPreferences.getString(user_id_key, null)
        val accessToken = sharedPreferences.getString(token_key, null)

        if (userId != null && accessToken != null) {
            return CredentialsHolder(userId, accessToken)
        }
        return null
    }

    override fun saveCredentialsHolder(credentialsHolder: CredentialsHolder) {
        with(sharedPreferences.edit()) {
            putString(user_id_key, credentialsHolder.userId)
            putString(token_key, credentialsHolder.accessToken)
            apply()
        }
    }
}