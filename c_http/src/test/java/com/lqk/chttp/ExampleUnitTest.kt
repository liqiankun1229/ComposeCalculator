package com.lqk.chttp

import android.util.Log
import kotlinx.coroutines.delay
import org.junit.Test

import org.junit.Assert.*
import kotlin.concurrent.thread
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.createCoroutine
import kotlin.coroutines.resume

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        println("main:${Thread.currentThread()}")

        val continuation = suspend {
            println("created: 协程")
            5
        }.createCoroutine(object : Continuation<Int> {
            override val context: CoroutineContext
                get() = EmptyCoroutineContext

            override fun resumeWith(result: Result<Int>) {
                println("resumeWith: 协程结果: ${result.getOrNull()}")
            }
        })
        continuation.resume(func())
    }

    private fun func() {
        println("func:${Thread.currentThread()}")
    }
}