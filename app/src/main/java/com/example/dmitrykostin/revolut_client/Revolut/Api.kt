package com.example.dmitrykostin.revolut_client.Revolut

import com.example.dmitrykostin.revolut_client.AuthorizationHolder
import com.example.dmitrykostin.revolut_client.Revolut.response.ConfirmResponse
import com.example.dmitrykostin.revolut_client.Revolut.response.Transaction
import com.example.dmitrykostin.revolut_client.Revolut.response.TransactionList
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.github.kittinunf.result.Result
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.json.JSONObject
import java.lang.reflect.Type


class Api {
    private val moshi by lazy {
        Moshi.Builder()
//            .add(KotlinJsonAdapterFactory())
            .build()
    }

    private val fuelManager by lazy {
        val fuelManager = FuelManager()
        val headers = mapOf(
            "X-Client-Version" to "4.5.1",
            "User-Agent" to "Revolut/com.revolut.revolut (iPhone5,3 2237; OS Version 10.2.1 (Build 14D27))",
            "X-Api-Version" to "1",
            "Content-Type" to "application/json",
            "X-Device-Id" to "6A881B1E-0002-1548-42A1-01D7346E40B1"
        )
        fuelManager.baseHeaders = headers
        fuelManager.basePath = "https://api.revolut.com"
        fuelManager
    }

    /**
     * Always returns empty response
     */
    fun signIn(phone: String, password: String): Result<String, FuelError> {
        val json = JSONObject()
        json.put("phone", phone)
        json.put("password", password)
        val (request, response, result) = fuelManager.request(
            Method.POST,
            "signin"
        )
            .authenticate("App", revolut_app_id)
            .jsonBody(json.toString())
            .responseString();
        return result
    }

    fun confirm(phone: String, code: String) : Result<ConfirmResponse, FuelError> {
        val json = JSONObject()
        json.put("phone", phone)
        json.put("code", code)
        val adapter = moshi.adapter(ConfirmResponse::class.java)
        val (_, _, result) = fuelManager.request(
            Method.POST,
            "signin/confirm"
        )
            .authenticate("App", revolut_app_id)
            .jsonBody(json.toString())
            .responseObject(moshiDeserializerOf(adapter))

        return result;
    }

    fun getTransactions(authorizationHolder: AuthorizationHolder) : Result<List<Transaction>, FuelError>
    {
        val transactionsList = Types.newParameterizedType(List::class.java, Transaction::class.java)
        val adapter: JsonAdapter<List<Transaction>> = moshi.adapter(transactionsList)

        val params = listOf("count" to "120", "to" to (System.currentTimeMillis()/1000) )
        val (_, _, result) = fuelManager.request(
            Method.GET,
            "/user/current/transactions/last",
            params
        )
            .authenticate(authorizationHolder.userId, authorizationHolder.accessToken)
            .responseObject(moshiDeserializerOf(adapter))

        return result;
    }

    companion object {
        // Чтоб никто не догадался
        val revolut_app_id = "S3DO2C1GFD3F"
    }
}