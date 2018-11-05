package com.example.dmitrykostin.revolut_client.mvp.representer

import android.os.Bundle
import com.example.dmitrykostin.revolut_client.revolut_api.Api
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class BaseRepresenter : CoroutineScope, BaseRepresenterInferface {
    private val job by lazy {
        Job()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun destroy() {
        job.cancel()
    }
}