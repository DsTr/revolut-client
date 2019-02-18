package com.example.dmitrykostin.revolut_client.credentials

import android.content.SharedPreferences
import com.example.dmitrykostin.revolut_client.revolut_api.Credentials

class SharedPreferencesCredentialsStorage(private val sharedPreferences: SharedPreferences) : CredentialsStorage {
    companion object {
        const val user_id_key = "user_id"
        const val token_key = "access_token"
    }

    override fun getCredentialsHolder(): Credentials? {
        val userId = sharedPreferences.getString(user_id_key, null)
        val accessToken = sharedPreferences.getString(token_key, null)

        if (userId != null && accessToken != null) {
            return Credentials(userId, accessToken)
        }
        return null
    }

    override fun saveCredentialsHolder(credentials: Credentials) {
        with(sharedPreferences.edit()) {
            putString(user_id_key, credentials.userId)
            putString(token_key, credentials.accessToken)
            apply()
        }
    }

    override fun clearCredentials(): Boolean {
        with(sharedPreferences.edit()) {
            remove(user_id_key)
            remove(token_key)
            apply()
        }
        return true
    }
}