plugins {
    kotlin("jvm") version "1.9.23"
    id("io.gitlab.arturbosch.detekt") version "1.23.6"
    id("maven-publish")
}

group = "com.github.octa-one"
version = "0.2"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.octa-one"
            artifactId = "kmold"
            version = "0.2"

            afterEvaluate {
                from(components["java"])
            }
        }
    }
}
