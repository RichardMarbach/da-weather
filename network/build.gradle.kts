import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "com.ds"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.microutils:kotlin-logging:1.6.25")
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-coroutines-core", version = "1.1.1")
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-io", version = "0.1.7")
    implementation(group = "io.ktor", name = "ktor-server-core", version = "1.1.3")
    implementation(group = "org.slf4j", name = "slf4j-api", version = "1.7.26")
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.7.26")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}