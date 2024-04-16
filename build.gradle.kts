import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.23"
    id("org.jetbrains.kotlin.kapt") version "1.9.23"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.9.23"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.micronaut.application") version "4.3.6"
    id("io.micronaut.aot") version "4.3.6"
    id("org.jetbrains.kotlin.plugin.spring") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.23"
    kotlin("plugin.noarg") version "1.9.23"
    id("org.liquibase.gradle") version "2.2.1"
}

version = "0.1.0-SNAPSHOT"
group = "app.kerrlab.subspace"

val javaVersion = "21"
val dockerRegistryUrl = System.getenv("DOCKER_REGISTRY_URL")
val dockerRepositoryPath = System.getenv("DOCKER_REPOSITORY_PATH")
val dockerImage = System.getenv("DOCKER_IMAGE") ?: project.name
val baseImage = "azul/zulu-openjdk-alpine:21-latest"
val dockeruser = System.getenv("DOCKER_USERNAME")
val dockerpass = System.getenv("DOCKER_PASSWORD")

val kotlinVersion: String by project
repositories {
    mavenCentral()
}

val hibernateVersion = "6.4.4.Final"
dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    kapt("io.micronaut.openapi:micronaut-openapi")
    kapt("io.micronaut.serde:micronaut-serde-processor")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-retry")
    implementation("io.micronaut.discovery:micronaut-discovery-client")
    implementation("io.micronaut.kotlin:micronaut-kotlin-extension-functions")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.serde:micronaut-serde-jackson")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${kotlinVersion}")

    implementation("org.hibernate.orm:hibernate-core:$hibernateVersion")
    implementation("io.micronaut.sql:micronaut-hibernate-jpa")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.data:micronaut-data-tx-hibernate")
    implementation("io.micronaut.data:micronaut-data-processor")
    implementation("io.micronaut.data:micronaut-data-hibernate-jpa")
    implementation("io.micronaut.validation:micronaut-validation")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    annotationProcessor("io.micronaut.validation:micronaut-validation-processor")
    annotationProcessor("io.micronaut:micronaut-inject-java")
    compileOnly("io.micronaut.openapi:micronaut-openapi-annotations")
}


application {
    mainClass.set("app.kerrlab.subspace.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion(javaVersion)
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(javaVersion))
    }
}

tasks {
    dockerBuild {
        images.set(listOf("${dockerRepositoryPath}/${dockerImage}:${project.version}"))
    }

    dockerBuildNative {
        images.set(listOf("${dockerRepositoryPath}/${dockerImage}-native:${project.version}"))
    }
}
graalvmNative.toolchainDetection.set(false)
micronaut {
    runtime("netty")
    testRuntime("kotest5")
    processing {
        incremental(true)
        annotations("app.kerrlab.subspace.*")
    }
    aot {
        // Please review carefully the optimizations enabled below
        // Check https://micronaut-projects.github.io/micronaut-aot/latest/guide/ for more details
        optimizeServiceLoading.set(false)
        convertYamlToJava.set(false)
        precomputeOperations.set(true)
        cacheEnvironment.set(true)
        optimizeClassLoading.set(true)
        deduceEnvironment.set(true)
        optimizeNetty.set(true)
    }
}

tasks.named<io.micronaut.gradle.docker.MicronautDockerfile>("dockerfile") {
    baseImage(baseImage.get())
}

tasks.named<io.micronaut.gradle.docker.NativeImageDockerfile>("dockerfileNative") {
    jdkVersion.set(javaVersion)
}

tasks.named<com.bmuschko.gradle.docker.tasks.image.DockerPushImage>("dockerPush") {
    registryCredentials {
        username = dockeruser
        password = dockerpass
        url = dockerRegistryUrl
    }
}

tasks.named<com.bmuschko.gradle.docker.tasks.image.DockerPushImage>("dockerPushNative") {
    registryCredentials {
        username = dockeruser
        password = dockerpass
        url = dockerRegistryUrl
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
    }
}

allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.inject.Singleton")
    annotation("io.micronaut.http.annotation.Controller")
}