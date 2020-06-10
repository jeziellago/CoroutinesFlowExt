package com.coroutines.flowext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun CoroutineScope.addSubscribers(publisher: FlowPublisherChannel) {
    launch {
        val subscriberA = flowSubscriberOf<Int>(publisher)
        subscriberA.asFlow()
            .collect {
                println("[collect]:SUBSCRIBER-A: $it")
                if (it == 5) publisher.unsubscribe(subscriberA)
            }
    }

    launch {
        val subscriberB = flowSubscriberOf<Int>(publisher)
        subscriberB.asFlow()
            .collect {
                println("[collect]:SUBSCRIBER-B: $it")
                if (it == 10) publisher.unsubscribe(subscriberB)
            }
    }

    launch {
        val subscriberC = flowSubscriberOf<Int>(publisher)
        subscriberC.asFlow()
            .collect {
                println("[collect]:SUBSCRIBER-C: $it")
                if (it == 15) publisher.unsubscribe(subscriberC)
            }
    }
}


fun main() = runBlocking {

    val emitter = FlowEmitter<Int>()
    val flowPublisher = emitter.asPublisher()

    launch {
        repeat(21) {
            emitter.emit(it)
            println("[PUBLISH] $it")
            delay(800)
        }
    }
    addSubscribers(flowPublisher)

    launch {
        flowPublisher.open()
    }

    println("START!")
}