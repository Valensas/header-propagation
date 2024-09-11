package com.valensas.headerpropagation.config

import com.valensas.headerpropagation.interceptor.FeignHeaderPropagationInterceptor
import com.valensas.headerpropagation.properties.HeaderPropagationProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnClass(feign.RequestInterceptor::class)
@EnableConfigurationProperties(HeaderPropagationProperties::class)
@ConditionalOnProperty(
    prefix = "header-propagation.feign",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class FeignConfiguration {
    @Bean
    fun feignHeaderPropagationInterceptor(): FeignHeaderPropagationInterceptor {
        return FeignHeaderPropagationInterceptor()
    }
}
