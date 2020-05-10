package com.nshumskii.lab1.model

open class Event<out T>(private val content: T? = null) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? = if (hasBeenHandled) {
        null
    } else {
        hasBeenHandled = true
        content
    }

    fun peekContent(): T? = content
}