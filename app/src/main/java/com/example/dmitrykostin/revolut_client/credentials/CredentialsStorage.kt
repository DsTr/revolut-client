package com.example.dmitrykostin.revolut_client.credentials

import com.example.dmitrykostin.revolut_client.revolut_api.Credentials

interface CredentialsStorage {
    fun getCredentialsHolder() : Credentials?
    fun saveCredentialsHolder(credentials: Credentials)
    fun clearCredentials() : Boolean
}