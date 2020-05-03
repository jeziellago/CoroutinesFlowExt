/*
 * Copyright 2020 Jeziel Lago
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.coroutines.flowext

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalCoroutinesApi::class)
class FlowSubscriber<T : Any>(capacity: Int = Channel.BUFFERED) : FlowSubscription {

    init {
        require(capacity != Channel.RENDEZVOUS) { "Channel.RENDEZVOUS not supported to FlowSubscriber." }
    }

    private val internalChannel = Channel<Any>(capacity)

    private val flowSubscriber: Flow<T> = flow {
        internalChannel.consumeEach { value ->
            if (value is Throwable) {
                internalChannel.close()
                throw value
            } else {
                @Suppress("UNCHECKED_CAST")
                emit(value as T)
            }
        }
    }

    fun asFlow(): Flow<T> = flowSubscriber

    override suspend fun <T : Any> notify(value: T) {
        internalChannel.send(value)
    }

    override suspend fun notifyError(error: Throwable) {
        internalChannel.send(error)
    }

    override suspend fun close() {
        internalChannel.close()
    }
}