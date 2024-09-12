package com.valensas.headerpropagation.config

import com.valensas.headerpropagation.interceptor.KafkaHeaderPropagationConsumerInterceptor
import com.valensas.headerpropagation.interceptor.KafkaHeaderPropagationProducerInterceptor
import com.valensas.headerpropagation.properties.HeaderPropagationProperties
import com.valensas.kafka.config.ConsumerInterceptorClassHolder
import com.valensas.kafka.config.ProducerInterceptorClassHolder
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
    prefix = "header-propagation.kafka",
    name = ["enabled"],
    havingValue = "true",
    matchIfMissing = true
)
class KafkaInterceptorConfiguration {
    @Bean
    fun kafkaHeaderPropagationConsumerInterceptorClassHolder(): ConsumerInterceptorClassHolder {
        return ConsumerInterceptorClassHolder(
            KafkaHeaderPropagationConsumerInterceptor::class.java
        )
    }

    @Bean
    fun kafkaHeaderPropagationProducerInterceptorClassHolder(): ProducerInterceptorClassHolder {
        return ProducerInterceptorClassHolder(
            KafkaHeaderPropagationProducerInterceptor::class.java
        )
    }
}
