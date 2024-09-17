package com.valensas.headerpropagation.controller

import com.valensas.headerpropagation.client.TestFeignClient
import com.valensas.headerpropagation.service.KafkaProducerService
import com.valensas.headerpropagation.util.Fake
import com.valensas.headerpropagation.util.TestMessage
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/test")
class TestController(
    private val kafkaProducerService: KafkaProducerService,
    private val testFeignClient: TestFeignClient
) {
    @PostMapping("/rest-to-kafka")
    fun postRestToKafka(): ResponseEntity<Unit> {
        kafkaProducerService.publishMessage("rest-to-kafka-output", Fake.testMessage())
        return ResponseEntity.ok().build()
    }

    @PostMapping("/kafka-to-rest")
    fun postKafkaToRest(): ResponseEntity<Unit> {
        kafkaProducerService.publishMessage("kafka-to-rest-output", Fake.testMessage())
        return ResponseEntity.ok().build()
    }

    @PostMapping("/feign-to-kafka")
    fun postFeignToKafkaAndPost(
        @RequestBody testMessage: TestMessage,
        @RequestHeader headers: Map<String, String>
    ): ResponseEntity<Unit> {
        testFeignClient.postFeignToKafkaAndPublish()
        return ResponseEntity.ok().build()
    }

    @PostMapping("/feign-to-kafka-publish")
    fun postFeignToKafkaAndPublish(): ResponseEntity<Unit> {
        kafkaProducerService.publishMessage("feign-to-kafka-output", Fake.testMessage())
        return ResponseEntity.ok().build()
    }

    @PostMapping("/kafka-to-feign")
    fun postKafkaToFeign(): ResponseEntity<Unit> {
        kafkaProducerService.publishMessage("kafka-to-feign-output", Fake.testMessage())
        return ResponseEntity.ok().build()
    }
}
