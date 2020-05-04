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

import kotlinx.coroutines.channels.Channel
import kotlin.experimental.ExperimentalTypeInference


@OptIn(ExperimentalTypeInference::class)
fun <T : Any> flowSubscriberOf(
    publisherChannel: FlowPublisherChannel,
    bufferSize: Int = Channel.BUFFERED
) = FlowSubscriber<T>(bufferSize).also {
    publisherChannel.subscribe(it)
}

