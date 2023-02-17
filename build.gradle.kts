plugins {
    kotlin("jvm") version "1.8.10"
    id("io.gitlab.arturbosch.detekt") version "1.22.0"
    id("maven-publish")
}

group = "aam"
version = "0.1"

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
            version = "0.1"

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}
