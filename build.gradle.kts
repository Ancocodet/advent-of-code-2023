import java.net.URI

plugins {
    id("java")
    id("application")
}

group = "com.ancozockt"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.ancozockt.advent.Main")
}

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
    implementation("org.projectlombok:lombok:1.18.22")
    testImplementation("org.projectlombok:lombok:1.18.22")

    annotationProcessor("org.projectlombok:lombok:1.18.22")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.22")

    implementation("com.google.common:google-collect:0.5")
    implementation("org.reflections:reflections:0.10.2")
    testImplementation("org.reflections:reflections:0.10.2")

    implementation( "org.apache.commons:commons-math3:3.6.1")
    implementation("org.apache.commons:commons-lang3:3.12.0")

    implementation("de.ancozockt:aoclib:3.0.0-SNAPSHOT")
    testImplementation("de.ancozockt:aoclib:3.0.0-SNAPSHOT")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.encoding = "utf-8"
}