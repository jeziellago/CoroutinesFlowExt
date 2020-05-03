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

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertArrayEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class FlowPublisherTest {

    @Test
    fun `publisher should emit values for all when has subscribers listening from source`() = runBlocking {
        // Given
        val flowPublisher = FlowPublisher(sourceFlow(emitTimes = 4))
        val subscriberA = FlowSubscriber<Int>()
        val subscriberB = FlowSubscriber<Int>()
        val subscriberC = FlowSubscriber<Int>()

        flowPublisher.subscribe(subscriberA)
        flowPublisher.subscribe(subscriberB)
        flowPublisher.subscribe(subscriberC)

        val emittedFromSource = mutableListOf<Int>()
        val receivedFromSubscriberA = mutableListOf<Int>()
        val receivedFromSubscriberB = mutableListOf<Int>()
        val receivedFromSubscriberC = mutableListOf<Int>()

        // When
        flowPublisher.asFlow().collect {
            emittedFromSource.add(it)
        }
        subscriberA.asFlow().collect { receivedFromSubscriberA.add(it) }
        subscriberB.asFlow().collect { receivedFromSubscriberB.add(it) }
        subscriberC.asFlow().collect { receivedFromSubscriberC.add(it) }

        // Then
        val expectedValues = arrayOf(0, 1, 2, 3)
        assertArrayEquals("Publisher", expectedValues, emittedFromSource.toTypedArray())
        assertArrayEquals("Subscriber-A", expectedValues, receivedFromSubscriberA.toTypedArray())
        assertArrayEquals("Subscriber-B", expectedValues, receivedFromSubscriberB.toTypedArray())
        assertArrayEquals("Subscriber-C", expectedValues, receivedFromSubscriberC.toTypedArray())
    }

    @Test
    fun `publisher without explicit source should emit values for all when has subscribers listening`() = runBlocking {
        // Given
        val flowPublisher = flowPublisher {
            emit(0)
            emit(1)
            emit(2)
            emit(3)
        }

        val subscriberA = FlowSubscriber<Int>()
        val subscriberB = FlowSubscriber<Int>()
        val subscriberC = FlowSubscriber<Int>()

        flowPublisher.subscribe(subscriberA)
        flowPublisher.subscribe(subscriberB)
        flowPublisher.subscribe(subscriberC)

        val receivedFromSubscriberA = mutableListOf<Int>()
        val receivedFromSubscriberB = mutableListOf<Int>()
        val receivedFromSubscriberC = mutableListOf<Int>()

        // When
        flowPublisher.open()

        subscriberA.asFlow().onEach { receivedFromSubscriberA.add(it) }.collect { }
        subscriberB.asFlow().collect { receivedFromSubscriberB.add(it) }
        subscriberC.asFlow().collect { receivedFromSubscriberC.add(it) }

        // Then
        val expectedValues = arrayOf(0, 1, 2, 3)
        assertArrayEquals("Subscriber-A", expectedValues, receivedFromSubscriberA.toTypedArray())
        assertArrayEquals("Subscriber-B", expectedValues, receivedFromSubscriberB.toTypedArray())
        assertArrayEquals("Subscriber-C", expectedValues, receivedFromSubscriberC.toTypedArray())
    }

    private fun sourceFlow(emitTimes: Int) = flow {
        repeat(emitTimes) { emit(it) }
    }

}