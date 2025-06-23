package com.kafasan.store.domain.network

/**
 * A utility time duration, since in unit test environment quite hard to simulate debounce
 * or delay, we can use interface for workaround to set the timer to 0
 */
interface TimerUtility {
    fun debounceTime(): Long
}

class TimerUtilityImpl : TimerUtility {
    override fun debounceTime(): Long {
        return 500
    }
}
