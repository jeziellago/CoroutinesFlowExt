package com.coroutines.flowext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> CoroutineScope.observeState(
    stateFlow: StateFlow<T>,
    collector: (T) -> Unit
) {
    launch { stateFlow.collect { collector(it) } }
}
