package com.valensas.headerpropagation.interceptor

import com.valensas.headerpropagation.config.ThreadLocalHeaderStore
import org.apache.kafka.clients.producer.ProducerInterceptor
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import java.lang.Exception

class KafkaHeaderPropagationProducerInterceptor<K, V> : ProducerInterceptor<K, V> {
    override fun onSend(record: ProducerRecord<K, V>): ProducerRecord<K, V> {
        ThreadLocalHeaderStore.headers.forEach { (key: String, value: String) ->
            record.headers().add(key, value.toByteArray(Charsets.UTF_8))
        }
        return record
    }

    override fun configure(p0: MutableMap<String, *>?) {
    }

    override fun close() {
    }

    override fun onAcknowledgement(
        p0: RecordMetadata?,
        p1: Exception?
    ) {
    }
}
