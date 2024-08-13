import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.jmailen.kotlinter") version "4.3.0"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    //id("net.thebugmc.gradle.sonatype-central-portal-publisher") version "1.2.3"
    id("maven-publish")
}

group = "com.valensas"
version = "1.0.0-alpha27"
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

    compileOnly("com.valensas:kafka:0.2.2-alpha")
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
