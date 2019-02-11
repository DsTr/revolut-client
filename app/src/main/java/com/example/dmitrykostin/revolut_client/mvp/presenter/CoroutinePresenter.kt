package com.example.dmitrykostin.revolut_client.mvp.presenter

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

abstract class CoroutinePresenter : CoroutineScope, BasePresenter {
    private val job by lazy {
        Job()
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun destroy() {
        job.cancel()
    }
}