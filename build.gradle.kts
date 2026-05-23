import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("jvm") version "2.3.21"
    kotlin("plugin.serialization") version "2.3.21"
}

group = "org.lolicode.moemusic.pvducking"
version = "1.0.0"

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
    compileOnly("org.lolicode.moemusic:api:1.0.0")
    compileOnly("su.plo.voice.api:client:2.1.9")
    compileOnly("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.11.0")

    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter:6.0.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:6.0.3")
    testImplementation("org.lolicode.moemusic:api:1.0.0")
}

tasks.test {
    useJUnitPlatform()
}
