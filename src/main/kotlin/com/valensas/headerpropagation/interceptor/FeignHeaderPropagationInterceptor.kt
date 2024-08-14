package com.valensas.headerpropagation.interceptor

import com.valensas.headerpropagation.config.ThreadLocalHeaderStore
import feign.RequestInterceptor
import feign.RequestTemplate

class FeignHeaderPropagationInterceptor : RequestInterceptor {
    override fun apply(template: RequestTemplate) {
        ThreadLocalHeaderStore.headers.forEach { (key, value) ->
            template.header(key, value)
        }
    }
}
