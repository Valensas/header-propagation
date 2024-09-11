# Header Propagation

This is a library that covers your various header propagation needs in Spring Boot applications that utilize any of Kafka, Web MVC or Feign Client.

## Dependency

Only add the dependency for the module you need.

```kotlin
implementation("com.valensas:kafka")

implementation("org.springframework.boot:spring-boot-starter-web")

implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
```

## Method

When any of the propagation methods are enabled, the headers are stored in the ThreadLocalHeaderStore.headers (A ThreadLocalVariable) when they are received.
When a kafka message or http request is sent, the headers are read from the ThreadLocalHeaderStore.headers and added to the message.

## Usage

### Headers

You can configure the headers that you want to propagate in your properties file.

```yaml
spring:
  header-propagation:
    headers: 
      - header1
      - header2
```

### Kafka

Enable the kafka propagation in your properties file.

```yaml
spring:
  header-propagation:
    kafka.enabled: true
```

When you send a message to kafka using the `KafkaTemplate`, the headers that exist in the thread local store will be added to the message headers automatically.

When a consumer function annotated with @KafkaListener receives a message, the headers are stored in the ThreadLocalHeaderStore.headers.

### Web MVC

Enable the rest propagation in your properties file.

```yaml
spring:
  header-propagation:
    rest.enabled: true
```

When you send a request using the `RestTemplate`, the headers that exist in the thread local store will be added to the request headers automatically.

When a controller method receives a request, the headers are stored in the ThreadLocalHeaderStore.headers.

### Feign Client

Enable the feign propagation in your properties file.

```yaml
spring:
  header-propagation:
    feign.enabled: true
```

When you send a request using the `FeignClient`, the headers that exist in the thread local store will be added to the request headers automatically.

### Accessing Headers when inbound

You can access the headers in the ThreadLocalHeaderStore.headers from anywhere in the same thread that received the headers.

## Warning

You need to capture the headers before spawning a new thread, because the ThreadLocalHeaderStore.headers **may not** exist in the new thread.

