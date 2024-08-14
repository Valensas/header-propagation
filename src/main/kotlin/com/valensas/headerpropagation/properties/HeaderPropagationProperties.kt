package com.valensas.headerpropagation.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.kafka.propagation")
data class HeaderPropagationProperties(
    val headers: List<String> = emptyList()
)
