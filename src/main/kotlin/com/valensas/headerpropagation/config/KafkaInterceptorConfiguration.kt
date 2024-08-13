package com.valensas.headerpropagation.config

import com.valensas.headerpropagation.interceptor.KafkaHeaderPropagationConsumerInterceptor
import com.valensas.headerpropagation.interceptor.KafkaHeaderPropagationProducerInterceptor
import com.valensas.headerpropagation.properties.HeaderPropagationProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate

@Configuration
@ConditionalOnClass(KafkaTemplate::class)
@EnableConfigurationProperties(HeaderPropagationProperties::class)
@ConditionalOnProperty(
    prefix = "spring.kafka.propagation",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class KafkaInterceptorConfiguration {
    @Bean
    fun kafkaHeaderPropagationConsumerInterceptor(
        headerPropagationProperties: HeaderPropagationProperties
    ): KafkaHeaderPropagationConsumerInterceptor<Any, Any> {
        return KafkaHeaderPropagationConsumerInterceptor()
    }

    @Bean
    fun kafkaHeaderPropagationProducerInterceptor(): KafkaHeaderPropagationProducerInterceptor<Any, Any> {
        return KafkaHeaderPropagationProducerInterceptor()
    }
}
