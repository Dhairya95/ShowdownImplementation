plugins {
    id("java")
    kotlin("jvm") version "1.9.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.graalvm.js:js:22.3.0") // Replace with the GraalVM version you have installed
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(8)
}