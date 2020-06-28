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
@file:Suppress("unused")

package com.coroutines.flowext

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@OptIn(ExperimentalCoroutinesApi::class)
class FlowPublisher<T : Any>(source: Flow<T>) : FlowPublisherChannel {

    private val subscribers = mutableSetOf<FlowSubscription>()

    private val emitter = source
        .onEach { value -> emitBroadcast { it.notify(value) } }
        .catch { error ->
            emitBroadcast { it.notifyError(error) }
        }.onCompletion {
            emitBroadcast { it.close() }
        }

    fun asFlow(): Flow<T> = emitter

    suspend fun open(onEachCollect: ((T) -> Unit)? = null) {
        emitter.collect { onEachCollect?.invoke(it) }
    }

    override fun subscribe(subscription: FlowSubscription) {
        subscribers.add(subscription)
    }

    override fun unsubscribe(subscription: FlowSubscription) {
        subscribers.remove(subscription)
    }

    fun clearSubscriptions() = subscribers.clear()

    private suspend fun emitBroadcast(action: suspend (FlowSubscription) -> Unit) {
        subscribers.forEach { action(it) }
    }
}