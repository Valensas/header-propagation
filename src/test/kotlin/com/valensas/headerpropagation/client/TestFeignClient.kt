package com.valensas.headerpropagation.client

import com.valensas.headerpropagation.util.TestMessage
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(name = "testFeignClient", url = "\${test.feign.client.url}")
interface TestFeignClient {
    @PostMapping("/test/feign-to-kafka")
    fun postFeignToKafka(
        @RequestBody testMessage: TestMessage,
        @RequestHeader headers: Map<String, String>
    ): ResponseEntity<Unit>

    @PostMapping("/test/feign-to-kafka-publish")
    fun postFeignToKafkaAndPublish(): ResponseEntity<Unit>

    @PostMapping("/test/kafka-to-feign")
    fun postKafkaToFeign(): ResponseEntity<Unit>
}
