import java.net.URI

plugins {
    id("java")
    id("application")
}

group = "com.ancozockt"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenCentral()
    maven {
        url = URI.create("https://repo.enonic.com/public/")
    }
    maven {
        url = URI.create("https://maven.abstractolotl.de/snapshots/")
    }
}

dependencies {
    implementation("de.ancozockt:aoclib:2.2.2-SNAPSHOT")
    testImplementation("de.ancozockt:aoclib:2.2.2-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}