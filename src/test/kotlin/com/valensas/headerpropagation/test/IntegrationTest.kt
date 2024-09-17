package com.valensas.headerpropagation.test

import com.valensas.headerpropagation.client.TestFeignClient
import com.valensas.headerpropagation.config.ThreadLocalHeaderStore
import com.valensas.headerpropagation.util.Fake
import com.valensas.headerpropagation.util.TestMessage
import org.apache.kafka.clients.producer.ProducerRecord
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = ["server.port=13333"])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@EmbeddedKafka(
    partitions = 1,
    topics = [
        "kafka-to-kafka-input", "kafka-to-kafka-output", "rest-to-kafka-output", "kafka-to-rest-input",
        "kafka-to-rest-output", "feign-to-kafka-output", "kafka-to-feign-input", "kafka-to-feign-output"
    ]
)
class IntegrationTest(
    @Autowired private val restTemplate: TestRestTemplate,
    @Autowired private val testFeignClient: TestFeignClient
) {
    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, TestMessage>

    @Autowired
    private lateinit var embeddedKafkaBroker: EmbeddedKafkaBroker

    @Test
    fun `should propagate headers from Kafka to Kafka`() {
        val consumer = Fake.consumer(embeddedKafkaBroker)
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "kafka-to-kafka-output")

        ThreadLocalHeaderStore.headers = mapOf(Fake.taskKey to Fake.taskValue)

        kafkaTemplate.send(ProducerRecord("kafka-to-kafka-input", Fake.testMessage()))
        val record = KafkaTestUtils.getSingleRecord(consumer, "kafka-to-kafka-output")

        val headerValue = record.headers().headers(Fake.taskKey).iterator().next().value()
        assertEquals(Fake.taskValue, String(headerValue))
    }

    @Test
    fun `should propagate headers from Rest to Kafka`() {
        val consumer = Fake.consumer(embeddedKafkaBroker)
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "rest-to-kafka-output")

        val requestEntity = HttpEntity(null, HttpHeaders().also { it.set(Fake.taskKey, Fake.taskValue) })

        restTemplate.postForEntity("/test/rest-to-kafka", requestEntity, String::class.java)
        val record = KafkaTestUtils.getSingleRecord(consumer, "rest-to-kafka-output")

        val headerValue = record.headers().headers(Fake.taskKey).iterator().next().value()
        assertEquals(Fake.taskValue, String(headerValue))
    }

    @Test
    fun `should propagate headers from Kafka to Rest`() {
        val consumer = Fake.consumer(embeddedKafkaBroker)
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "kafka-to-rest-output")

        ThreadLocalHeaderStore.headers = mapOf(Fake.taskKey to Fake.taskValue)

        kafkaTemplate.send(ProducerRecord("kafka-to-rest-input", Fake.testMessage()))
        val record = KafkaTestUtils.getSingleRecord(consumer, "kafka-to-rest-output")

        val headerValue = record.headers().headers(Fake.taskKey).iterator().next().value()
        assertEquals(Fake.taskValue, String(headerValue))
    }

    @Test
    fun `should propagate headers from Feign to Kafka`() {
        val consumer = Fake.consumer(embeddedKafkaBroker)
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "feign-to-kafka-output")

        testFeignClient.postFeignToKafka(Fake.testMessage(), mapOf(Fake.taskKey to Fake.taskValue))
        val record = KafkaTestUtils.getSingleRecord(consumer, "feign-to-kafka-output")

        val headerValue = record.headers().headers(Fake.taskKey).iterator().next().value()
        assertEquals(Fake.taskValue, String(headerValue))
    }

    @Test
    fun `should propagate headers from Kafka to Feign`() {
        val consumer = Fake.consumer(embeddedKafkaBroker)
        embeddedKafkaBroker.consumeFromAnEmbeddedTopic(consumer, "kafka-to-feign-output")

        ThreadLocalHeaderStore.headers = mapOf(Fake.taskKey to Fake.taskValue)

        kafkaTemplate.send(ProducerRecord("kafka-to-feign-input", Fake.testMessage()))
        val record = KafkaTestUtils.getSingleRecord(consumer, "kafka-to-feign-output")

        val headerValue = record.headers().headers(Fake.taskKey).iterator().next().value()
        assertEquals(Fake.taskValue, String(headerValue))
    }
}
