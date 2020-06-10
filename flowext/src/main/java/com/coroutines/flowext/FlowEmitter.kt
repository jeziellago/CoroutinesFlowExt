package com.coroutines.flowext

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

class FlowEmitter<T : Any> {

    private val emitter = Channel<T>()

    @OptIn(ExperimentalCoroutinesApi::class)
    fun asPublisher() = FlowPublisher(emitter.receiveAsFlow())

    suspend fun emit(value: T) {
        emitter.send(value)
    }
}