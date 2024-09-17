package com.valensas.headerpropagation.util

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.utils.KafkaTestUtils
import java.time.Instant
import kotlin.random.Random

object Fake {
    fun consumer(embeddedKafkaBroker: EmbeddedKafkaBroker): Consumer<String, TestMessage> {
        val consumerProps =
            KafkaTestUtils.consumerProps("testGroup-${Instant.now()}", "true", embeddedKafkaBroker).apply {
                this[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
                this[ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG] = StringDeserializer::class.java
                this[ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG] = JsonDeserializer::class.java
                this[JsonDeserializer.TRUSTED_PACKAGES] = "*"
            }

        val consumerFactory = DefaultKafkaConsumerFactory<String, TestMessage>(consumerProps)
        return consumerFactory.createConsumer()
    }

    fun testMessage(): TestMessage = TestMessage(Random.nextLong())

    val taskKey = "task-name"
    val taskValue = "test"
}

data class TestMessage(
    val id: Long
)
