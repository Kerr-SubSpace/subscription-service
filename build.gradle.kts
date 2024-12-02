import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Versions {
    const val KOTLIN = "2.1.0"
    const val GRADLE = "8.7"
    const val STRIKT = "0.34.1"
    const val ARROW = "1.2.4"
    const val MOCKITO = "5.3.1"
    const val JACKSON = "2.17.1"
    const val KOTEST = "5.9.1"
    const val KOTEST_SPRING = "1.3.0"
    const val KOTEST_ARROW = "1.4.0"
    const val DATAFAKER = "2.4.2"
    const val SPRING_CLOUD_CONTRACT = "4.1.5"
}

semver {
    initialVersion("0.1.0")
    versionModifier { nextMinor() }
    tagPrefix("")
}

version = semver.version
group = "app.kerrlab.subspace"

plugins {
    kotlin("jvm")
    kotlin("plugin.noarg") version "2.1.0"
    kotlin("plugin.allopen") version "2.1.0"
    kotlin("plugin.spring") version "2.1.0"
    kotlin("plugin.jpa") version "2.1.0"
    id("com.google.cloud.tools.jib") version "3.4.4"
    id("com.figure.gradle.semver-plugin") version "1.10.0"
    id("org.graalvm.buildtools.native") version "0.10.3"
    id("org.hibernate.orm") version "6.6.3.Final"

    id("org.springframework.boot") version "3.3.6"
    id("io.spring.dependency-management") version "1.1.6"
    id("org.springframework.cloud.contract") version "4.1.5"
}

contracts {
    contractsDslDir = File(project.rootDir, "src/contractTest/resources/contracts")
}

object DockerProperties {
    val dockeruser = System.getenv("DOCKER_USERNAME")
    val dockerpass = System.getenv("DOCKER_PASSWORD")
    val dockerRegistryUrl = System.getenv("DOCKER_REGISTRY_URL")
    val dockerImage = System.getenv("DOCKER_IMAGE") ?: "${dockeruser}/project.name"
    const val BASE_IMAGE = "azul/zulu-openjdk-alpine:21-latest"
}

repositories {
    mavenLocal()
    mavenCentral()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-contract-dependencies:${Versions.SPRING_CLOUD_CONTRACT}")
    }
}

dependencies {
    // Spring
//    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Serialization
    implementation("com.fasterxml.jackson.core:jackson-core:${Versions.JACKSON}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${Versions.JACKSON}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${Versions.JACKSON}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
//    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // Other
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}")
    implementation("io.arrow-kt:arrow-core:${Versions.ARROW}")
    // DB Drivers
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.strikt:strikt-core:${Versions.STRIKT}")
    testImplementation("org.mockito.kotlin:mockito-kotlin:${Versions.MOCKITO}")
    testImplementation("io.kotest:kotest-runner-junit5:${Versions.KOTEST}")
    testImplementation("io.kotest:kotest-assertions-core:${Versions.KOTEST}")
    testImplementation("io.kotest.extensions:kotest-assertions-arrow:${Versions.KOTEST_ARROW}")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:${Versions.KOTEST_SPRING}")
    testImplementation("net.datafaker:datafaker:${Versions.DATAFAKER}")
    testImplementation("org.springframework.cloud:spring-cloud-starter-contract-verifier")
    testImplementation("org.jetbrains.kotlin:kotlin-scripting-compiler-embeddable:${Versions.KOTLIN}")
    testImplementation("org.springframework.cloud:spring-cloud-contract-spec-kotlin")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

buildscript {
    dependencies {
        classpath("org.springframework.cloud:spring-cloud-contract-gradle-plugin:4.1.5")
    }
}

tasks {
    build {
//        TODO: jib image
//        dependsOn(jibDockerBuild)
    }

//    dockerBuildNative {
//    TODO: native image
//        images.set(listOf("${dockerImage}-native:${project.version}"))
//    }

    wrapper {
        gradleVersion = Versions.GRADLE
        distributionType = Wrapper.DistributionType.ALL
    }
}

allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.inject.Singleton")
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JVM_21)
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}



jib {
    from {
        image = DockerProperties.BASE_IMAGE
    }
    to {
        image = "${DockerProperties.dockerImage}:${project.version}"
        auth {
            username = DockerProperties.dockeruser
            password = DockerProperties.dockerpass
        }
        tags = setOf("latest")
    }
    container.creationTime = "USE_CURRENT_TIMESTAMP"
}
kotlin {
    jvmToolchain(21)
}

hibernate {
    enhancement {
        enableAssociationManagement.set(true)
    }
}