import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("com.github.ben-manes.versions") version "0.51.0"
    id("org.jmailen.kotlinter") version "4.3.0"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.2.3"
    id("maven-publish")
}

group = "com.valensas"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    mavenLocal()
}

tasks.getByName<Jar>("jar") {
    archiveClassifier.set("")
}

dependencies {
    compileOnly("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.1")
    compileOnly("org.springframework.boot:spring-boot-starter-web")

    compileOnly("com.valensas:kafka:0.3.0")

    testImplementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.github.springwolf:springwolf-kafka:1.4.0")
    testImplementation("com.valensas:kafka:0.3.0")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.4.0")
    testImplementation("org.springframework.cloud:spring-cloud-starter-openfeign:4.1.1")


    // WireMock
    testImplementation("org.wiremock:wiremock:3.9.1")

    // Awaitility for asynchronous tests
    testImplementation("org.awaitility:awaitility-kotlin:4.2.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
}

signing {
    val keyId = System.getenv("SIGNING_KEYID")
    val secretKey = System.getenv("SIGNING_SECRETKEY")
    val passphrase = System.getenv("SIGNING_PASSPHRASE")

    useInMemoryPgpKeys(keyId, secretKey, passphrase)
}

centralPortal {
    username = System.getenv("SONATYPE_USERNAME")
    password = System.getenv("SONATYPE_PASSWORD")

    pom {
        name = "Valensas Header Propagation"
        description = "This library contains the configurable header propagation implementation for kafka and spring web mvc also feign client"
        url = "https://valensas.com/"
        scm {
            url = "https://github.com/Valensas/header-propagation"
        }

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
            }
        }

        developers {
            developer {
                id.set("0")
                name.set("Valensas")
                email.set("info@valensas.com")
            }
        }
    }
}
