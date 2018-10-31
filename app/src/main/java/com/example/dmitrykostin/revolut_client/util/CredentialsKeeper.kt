package com.example.dmitrykostin.revolut_client.util

import com.example.dmitrykostin.revolut_client.CredentialsHolder

interface CredentialsKeeper {
    fun getCredentialsHolder() : CredentialsHolder?
    fun saveCredentialsHolder(credentialsHolder: CredentialsHolder)
}