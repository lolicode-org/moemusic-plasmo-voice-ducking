import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.4.0"
    kotlin("plugin.serialization") version "2.4.0"
}

group = "org.lolicode.moemusic.pvducking"
version = "1.2.0"

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://codeberg.org/api/packages/lolicode/maven") {
        content { includeGroupByRegex("org\\.lolicode.*") }
    }
    maven("https://repo.plasmoverse.com/releases") {
        content {
            includeGroupByRegex("su\\.plo\\..*")
        }
    }
}

dependencies {
    compileOnly("org.lolicode.moemusic:api:2.1.1")
    compileOnly("su.plo.voice.api:client:2.1.9")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.11.0")
    compileOnly("org.slf4j:slf4j-api:2.0.18")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:6.1.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.1.0")
    testImplementation("org.lolicode.moemusic:api:2.1.1")
}

tasks.test {
    useJUnitPlatform()
}
