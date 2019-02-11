package com.example.dmitrykostin.revolut_client.revolut_api

import com.example.dmitrykostin.revolut_client.revolut_api.response.ConfirmResponse
import com.example.dmitrykostin.revolut_client.revolut_api.response.Transaction
import com.github.kittinunf.fuel.core.*
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.github.kittinunf.result.Result
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import org.json.JSONObject


class Api {
    private val moshi by lazy {
        Moshi.Builder()
            .add(DateMoshiAdapter())
            .build()
    }

    private val fuelManager by lazy {
        val fuelManager = FuelManager()
        val headers = mapOf(
            "X-Client-Version" to "5.19.2",
            "User-Agent" to "Revolut/5.19.2 501902425 (MI MAX 2; Android 7.1.1)",
            "X-Api-Version" to "1",
            "Content-Type" to "application/json",
            "X-Device-Id" to "8fff9782",
            "X-Device-Model" to "Xiaomi MI MAX 2",
            "Accept-Language" to "en-GB"
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

    fun getTransactions(credentials: Credentials) : Result<List<Transaction>, FuelError>
    {
        val transactionsList = Types.newParameterizedType(List::class.java, Transaction::class.java)
        val adapter: JsonAdapter<List<Transaction>> = moshi.adapter(transactionsList)

        Thread.sleep(2000)
        val res = moshiDeserializerOf(adapter).deserialize("[{\"id\":\"7db3a498-5b68-4c0c-afaf-d729795d80fc\",\"legId\":\"1800a298-5e97-44dc-b0b9-ce3192cca5e1\",\"type\":\"CARD_PAYMENT\",\"state\":\"COMPLETED\",\"startedDate\":1499272505810,\"updatedDate\":1499347063244,\"completedDate\":1499347063244,\"currency\":\"GBP\",\"amount\":-1800,\"fee\":0,\"balance\":10294,\"description\":\"The Clarks Shop\",\"rate\":1,\"merchant\":{\"id\":\"2101025487\",\"name\":\"The Clarks Shop\",\"country\":\"GB\",\"city\":\"Richmond Upon\",\"mcc\":\"5661\"},\"counterpart\":{\"amount\":-1800,\"currency\":\"GBP\"}},{\"id\":\"f5fe2744-21e3-4e0c-b845-aa9dd875deb2\",\"legId\":\"86649f50-ced5-4d14-8ba1-1e3112f166d8\",\"type\":\"TOPUP\",\"state\":\"COMPLETED\",\"startedDate\":1499172283348,\"updatedDate\":1499172283348,\"completedDate\":1499172283348,\"currency\":\"GBP\",\"amount\":12000,\"fee\":0,\"balance\":12094,\"description\":\"Payment from D Kostin\"},{\"id\":\"1ef7ed44-ac2c-450f-be38-1f90b2c5f56f\",\"legId\":\"2fa77809-245a-4066-812f-4b165877b73d\",\"type\":\"CARD_PAYMENT\",\"state\":\"DECLINED\",\"startedDate\":1499166655603,\"updatedDate\":1499166655684,\"currency\":\"GBP\",\"amount\":-1110,\"fee\":0,\"description\":\"Sainsburys 0548\",\"reason\":\"insufficient_balance\",\"rate\":1,\"merchant\":{\"id\":\"43325063\",\"name\":\"Sainsburys 0548\",\"country\":\"GB\",\"city\":\"Richmond\",\"mcc\":\"5411\"},\"counterpart\":{\"amount\":-1110,\"currency\":\"GBP\"}},{\"id\":\"3a7131fe-54e0-44f9-9a9b-c54eb0f78b27\",\"legId\":\"0db1024a-0e32-4921-a259-3d662bca7d33\",\"type\":\"TRANSFER\",\"state\":\"COMPLETED\",\"startedDate\":1496743205805,\"updatedDate\":1496743205805,\"completedDate\":1496743205805,\"currency\":\"GBP\",\"amount\":-1000,\"fee\":0,\"balance\":94,\"description\":\"To Roman Simonov\",\"comment\":\"gleb\",\"rate\":1,\"recipient\":{\"id\":\"00711c5c-ec48-47e0-9df3-c673af815f14\",\"phone\":\"+447731517902\",\"firstName\":\"Roman\",\"lastName\":\"Simonov\"}},{\"id\":\"25cd2022-3705-4c9a-a0f7-9efcbbc553cb\",\"legId\":\"84dd3f16-d427-4438-9a6f-b9dcc04fb187\",\"type\":\"TOPUP\",\"state\":\"COMPLETED\",\"startedDate\":1496743166525,\"updatedDate\":1496743167728,\"completedDate\":1496743167722,\"currency\":\"GBP\",\"amount\":1000,\"fee\":0,\"balance\":1094,\"description\":\"Top-Up by *6285\",\"logo\":\"hsbc\"},{\"id\":\"4001e637-e19a-4290-ad06-c3cfec9fd977\",\"legId\":\"dd61416d-04ff-48b2-9079-988e6aee076b\",\"type\":\"CARD_PAYMENT\",\"state\":\"COMPLETED\",\"startedDate\":1495823866197,\"updatedDate\":1496248728552,\"completedDate\":1496248728552,\"currency\":\"GBP\",\"amount\":-2687,\"fee\":0,\"balance\":3094,\"description\":\"Sainsburys 0548\",\"rate\":1,\"merchant\":{\"id\":\"43325063\",\"name\":\"Sainsburys 0548\",\"country\":\"GB\",\"city\":\"Richmond\",\"mcc\":\"5411\"},\"counterpart\":{\"amount\":-2687,\"currency\":\"GBP\"}},{\"id\":\"03c1f837-bcb7-4247-ad12-56e42401154f\",\"legId\":\"6804d6f7-21f1-4e17-80de-7669687868e1\",\"type\":\"CARD_PAYMENT\",\"state\":\"COMPLETED\",\"startedDate\":1495797489473,\"updatedDate\":1496249501734,\"completedDate\":1496249501734,\"currency\":\"GBP\",\"amount\":-3000,\"fee\":0,\"balance\":94,\"description\":\"Clarks Mcw\",\"rate\":1,\"merchant\":{\"id\":\"2101024457\",\"name\":\"Clarks Mcw\",\"country\":\"GB\",\"city\":\"Kew, Richmond\",\"mcc\":\"5661\"},\"counterpart\":{\"amount\":-3000,\"currency\":\"GBP\"}}]");
        return Result.Success<List<Transaction>, FuelError>(res!!)

        //https://api.revolut.com/user/current/transactions?from=0
        ///user/current/transactions/last?to=TS
        //, "to" to (System.currentTimeMillis()/1000)
        val params = listOf("from" to 0, "count" to 10 )
        val (_, _, result) = fuelManager.request(
            Method.GET,
            "/user/current/transactions",
            params
        )
            .authenticate(credentials.userId, credentials.accessToken)
            .responseObject(moshiDeserializerOf(adapter))

        return result;
    }

    companion object {
        // Чтоб никто не догадался
        val revolut_app_id = "S3DO2C1GFD3F"
    }
}