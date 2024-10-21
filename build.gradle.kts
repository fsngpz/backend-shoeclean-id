import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    kotlin("plugin.jpa") version "1.9.25"
}

group = "id.shoeclean"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

val jsonWebTokenVersion = "0.11.5"
val springDocVersion = "2.5.0"
val apacheCommonsCsvVersion = "1.11.0"
val apacheCommonsIoVersion = "2.16.1"
val mockitoKotlinVersion = "5.3.1"
val flywayPostgresVersion = "10.20.0"

dependencies {
    // -- spring --
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")

    // -- kotlin --
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    // -- spring boot: data --
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.flywaydb:flyway-core")

    // -- jwt --
    implementation("io.jsonwebtoken:jjwt-api:$jsonWebTokenVersion")
    implementation("io.jsonwebtoken:jjwt-impl:$jsonWebTokenVersion")
    implementation("io.jsonwebtoken:jjwt-jackson:$jsonWebTokenVersion")

    // -- apache commons --
    implementation("commons-io:commons-io:$apacheCommonsIoVersion")

    // -- spring doc --
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springDocVersion")

    // -- postgres --
    runtimeOnly("org.postgresql:postgresql")
    // -- flyway --
    runtimeOnly("org.flywaydb:flyway-database-postgresql:$flywayPostgresVersion")


    // -- test --
    testImplementation("org.mockito.kotlin:mockito-kotlin:$mockitoKotlinVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
        jvmTarget = JvmTarget.JVM_21
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.getByName<Jar>("jar") {
    enabled = false
}
