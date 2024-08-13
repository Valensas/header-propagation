package com.valensas.headerpropagation.config

import com.valensas.headerpropagation.interceptor.FeignHeaderPropagationInterceptor
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnClass(feign.RequestInterceptor::class)
@ConditionalOnProperty(
    prefix = "spring.web.propagation",
    name = ["enabled"],
    havingValue = "true"
)
class FeignConfiguration {
    @Bean
    fun feignHeaderPropagationInterceptor(): FeignHeaderPropagationInterceptor {
        return FeignHeaderPropagationInterceptor()
    }
}
