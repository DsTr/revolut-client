package com.example.dmitrykostin.revolut_client.revolut_api

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.*

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