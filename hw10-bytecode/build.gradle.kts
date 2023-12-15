plugins {
    id("java")
}

group = "ru.dgp"
version = "unspecified"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("ch.qos.logback:logback-classic")
}

tasks.test {
    useJUnitPlatform()
}