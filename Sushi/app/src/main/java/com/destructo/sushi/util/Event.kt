package com.destructo.sushi.util

import timber.log.Timber

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 */
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            Timber.e("Handled: $hasBeenHandled")
            null
        } else {
            hasBeenHandled = true
            Timber.e("Handled: $hasBeenHandled")
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}