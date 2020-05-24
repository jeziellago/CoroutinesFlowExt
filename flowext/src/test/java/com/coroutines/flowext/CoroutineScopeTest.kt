package com.coroutines.flowext

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CoroutineScopeTest {

    private val coroutineScopeTest = TestCoroutineScope()

    @Test
    fun `observeState should receive values from stateFlow`() = runBlockingTest {
        val stateFlow = MutableStateFlow(0)

        val expected = listOf(0, 1, 2, 3, 4, 5)

        val emitted = mutableListOf<Int>()

        coroutineScopeTest.observeState(stateFlow) { state -> emitted.add(state) }

        repeat(5) { stateFlow.value++ }

        assertEquals(expected, emitted)
    }
}