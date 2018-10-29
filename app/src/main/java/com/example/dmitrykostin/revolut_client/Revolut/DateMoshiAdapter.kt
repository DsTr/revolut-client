package com.example.dmitrykostin.revolut_client.Revolut

import com.squareup.moshi.JsonAdapter
import java.util.*
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class DateMoshiAdapter {
    @ToJson
    internal fun toJson(date: Date): String {
        return date.time.toString()
    }

    @FromJson
    internal fun fromJson(date: String): Date {
        return Date(date.toLong())
    }
}