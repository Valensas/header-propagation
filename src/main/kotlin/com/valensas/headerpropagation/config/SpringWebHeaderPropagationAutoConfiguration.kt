package com.valensas.headerpropagation.config

import com.valensas.headerpropagation.interceptor.WebHeaderExtractorFilter
import com.valensas.headerpropagation.properties.HeaderPropagationProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.client.RestTemplateCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpRequest
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.web.client.RestTemplate
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@ConditionalOnClass(RestTemplate::class)
@EnableConfigurationProperties(HeaderPropagationProperties::class)
@ConditionalOnProperty(
    prefix = "spring.header-propagation.rest",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class SpringWebHeaderPropagationAutoConfiguration : WebMvcConfigurer {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun headerPropagationFilter(headerPropagationProperties: HeaderPropagationProperties): OncePerRequestFilter {
        return WebHeaderExtractorFilter(headerPropagationProperties)
    }

    @Bean
    fun restTemplateHeaderPropagationCustomizer(): RestTemplateCustomizer {
        return RestTemplateCustomizer { restTemplate: RestTemplate ->
            restTemplate.interceptors.add(
                ClientHttpRequestInterceptor { request: HttpRequest, body: ByteArray, execution: ClientHttpRequestExecution ->
                    ThreadLocalHeaderStore.headers.forEach {
                        request.headers[it.key] = listOf(it.value)
                    }
                    ThreadLocalHeaderStore.clear()
                    execution.execute(request, body)
                }
            )
        }
    }
}
