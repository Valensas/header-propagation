package com.valensas.headerpropagation.service

import com.valensas.headerpropagation.client.TestFeignClient
import com.valensas.headerpropagation.util.TestMessage
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty("spring.kafka.consumer.enabled", havingValue = "true")
class KafkaConsumerService(
    private val kafkaProducerService: KafkaProducerService,
    private val testRestTemplate: TestRestTemplate,
    private val testFeignClient: TestFeignClient
) {
    @KafkaListener(topics = ["kafka-to-kafka-input"])
    fun consumeKafkaToKafkaInput(message: TestMessage) {
        kafkaProducerService.publishMessage("kafka-to-kafka-output", message)
    }

    @KafkaListener(topics = ["kafka-to-feign-input"])
    fun consumeKafkaToFeignInput() {
        testFeignClient.postKafkaToFeign()
    }

    @KafkaListener(topics = ["kafka-to-rest-input"])
    fun consumeKafkaToRestInput(message: TestMessage) {
        val body = HttpEntity(message)
        testRestTemplate.postForEntity("/test/kafka-to-rest", body, String::class.java)
    }
}
