import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.21"
}

group = "com.ds"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.2.0-alpha")
    implementation(group = "org.jetbrains.kotlinx", name = "kotlinx-io", version = "0.1.7")
    implementation(group = "io.ktor", name = "ktor-network", version = "1.1.3")
    implementation(group = "io.ktor", name = "ktor-server-core", version = "1.1.3")
    implementation(group = "io.ktor", name = "ktor-server-netty", version = "1.1.3")
    implementation(group = "io.github.microutils", name = "kotlin-logging", version = "1.6.24")
    implementation(group = "org.slf4j", name = "slf4j-api", version = "1.7.26")
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.7.26")
    implementation(group = "com.google.code.gson", name = "gson", version = "2.8.5")
    implementation("org.jetbrains.exposed:exposed:0.13.2")
}

compileKotlin {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = ['-Xuse-experimental=kotlin.Experimental']
}
compileTestKotlin {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs = ['-Xuse-experimental=kotlin.Experimental']
}
