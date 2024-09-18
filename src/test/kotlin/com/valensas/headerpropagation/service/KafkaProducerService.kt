package com.valensas.headerpropagation.service

import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class KafkaProducerService(
    private val kafkaTemplate: KafkaTemplate<String, Any>
) {
    fun publishMessage(
        topic: String,
        message: Any
    ) {
        val producerRecord = ProducerRecord<String, Any>(topic, message)
        kafkaTemplate.send(producerRecord)
    }
}
