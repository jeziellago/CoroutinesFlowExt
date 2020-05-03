package com.coroutines.flowext

interface FlowPublisherChannel {

    fun subscribe(subscription: FlowSubscription)

    fun unsubscribe(subscription: FlowSubscription)
}