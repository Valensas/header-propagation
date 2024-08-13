package com.valensas.headerpropagation.config

object ThreadLocalHeaderStore {
    private val threadLocalHeaders: ThreadLocal<Map<String, String>> = ThreadLocal.withInitial { mapOf() }

    var headers: Map<String, String>
        get() = threadLocalHeaders.get()
        set(headers) {
            threadLocalHeaders.set(headers)
        }

    fun clear() {
        threadLocalHeaders.remove()
    }
}
