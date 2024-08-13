package com.valensas.headerpropagation.interceptor

import com.valensas.headerpropagation.config.ThreadLocalHeaderStore
import com.valensas.headerpropagation.properties.HeaderPropagationProperties
import org.apache.kafka.clients.consumer.ConsumerInterceptor
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.header.Header
import org.springframework.context.ApplicationContext
import java.util.function.Consumer

class KafkaHeaderPropagationConsumerInterceptor<K, V>() : ConsumerInterceptor<K, V> {
    private lateinit var headerPropagationProperties: HeaderPropagationProperties
    private lateinit var applicationContext: ApplicationContext

    override fun onConsume(records: ConsumerRecords<K, V>): ConsumerRecords<K, V> {
        println("I WORK HERE")
        records.forEach(
            Consumer { record: ConsumerRecord<K, V> ->
                val headers = record.headers()
                val headerMap: MutableMap<String, String> =
                    ThreadLocalHeaderStore.headers.toMutableMap()
                headers.forEach(
                    Consumer { header: Header ->
                        if (headerPropagationProperties.headers.contains(header.key())) {
                            headerMap[header.key()] = header.value().toString(Charsets.UTF_8)
                        }
                    }
                )
                ThreadLocalHeaderStore.headers = headerMap
            }
        )
        return records
    }

    override fun close() {
    }

    override fun onCommit(p0: MutableMap<TopicPartition, OffsetAndMetadata>?) {
    }

    override fun configure(configs: Map<String, *>) {
        applicationContext = configs["applicationContext"] as ApplicationContext
        headerPropagationProperties = applicationContext.getBean(HeaderPropagationProperties::class.java)
    }
}
