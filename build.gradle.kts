import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.ByteArrayOutputStream
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Versions {
    const val JAVA = "21"
    const val KOTLIN = "2.0.0"
    const val GRADLE = "8.7"
    const val JIB = "3.4.2"
    const val SEMVER_PLUGIN = "1.10.0"
    const val STRIKT = "0.34.1"
    const val ARROW = "1.2.4"
    const val MOCKITO = "5.3.1"
    const val JACKSON = "2.17.1"
}

plugins {
    kotlin("jvm")
    kotlin("plugin.noarg") version "2.0.0"
    kotlin("plugin.allopen") version "2.0.0"
    kotlin("plugin.spring") version "2.0.0"
    kotlin("plugin.jpa") version "2.0.0"
    id("com.google.cloud.tools.jib") version "3.4.3"
    id("com.figure.gradle.semver-plugin") version "1.10.0"
    id("org.graalvm.buildtools.native") version "0.10.2"
    id("org.hibernate.orm") version "6.5.2.Final"

    id("org.springframework.boot") version "3.3.0"
    id("io.spring.dependency-management") version "1.1.5"
}

semver {
    initialVersion("0.1.0")
    versionModifier { nextMinor() }
    tagPrefix("")
}

version = semver.version
group = "app.kerrlab.subspace"

object DockerProperties {
    val dockeruser = System.getenv("DOCKER_USERNAME")
    val dockerpass = System.getenv("DOCKER_PASSWORD")
    val dockerRegistryUrl = System.getenv("DOCKER_REGISTRY_URL")
    val dockerImage = System.getenv("DOCKER_IMAGE") ?: "${dockeruser}/project.name"
    const val BASE_IMAGE = "azul/zulu-openjdk-alpine:21-latest"
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    // Spring
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // Serialization
    implementation("com.fasterxml.jackson.core:jackson-core:${Versions.JACKSON}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${Versions.JACKSON}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${Versions.JACKSON}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // Other
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${Versions.KOTLIN}")
    implementation("io.arrow-kt:arrow-core:${Versions.ARROW}")
    // DB Drivers
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:postgresql")
    testImplementation("io.rest-assured:rest-assured")
    testImplementation("io.strikt:strikt-core:${Versions.STRIKT}")
    testImplementation("org.mockito.kotlin:mockito-kotlin:${Versions.MOCKITO}")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
}

val gitHash: String by lazy {
    val stdout = ByteArrayOutputStream()
    rootProject.exec {
        commandLine("git", "rev-parse", "--verify", "--short", "HEAD")
        standardOutput = stdout
    }
    stdout.toString().trim()
}

val gitTimestamp: OffsetDateTime by lazy {
    val stdout = ByteArrayOutputStream()
    rootProject.exec {
        commandLine("git", "show", "-s", "--format=%ci")
        standardOutput = stdout
    }
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z")
    OffsetDateTime.parse(stdout.toString().trim(), formatter)
}

val imageTimestamp: String by lazy {
    gitTimestamp.atZoneSameInstant(ZoneId.of("UTC")).format(DateTimeFormatter.ISO_DATE_TIME)
}

tasks {
    build {
        dependsOn(jibDockerBuild)
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
    container {
        creationTime = imageTimestamp
    }
}
kotlin {
    jvmToolchain(21)
}

hibernate {
    enhancement {
        enableAssociationManagement.set(true)
    }
}