package com.valensas.headerpropagation.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spring.header-propagation")
data class HeaderPropagationProperties(
    val headers: List<String> = emptyList()
)
