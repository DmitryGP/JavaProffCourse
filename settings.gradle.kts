rootProject.name = "javaProffCourse"

include("hw01-gradle")

pluginManagement {
    val jgitver: String by settings
    val dependencyManagement: String by settings
    val springframeworkBoot: String by settings
    val johnrengelmanShadow: String by settings
    val jib: String by settings
    val protobufVer: String by settings
    val sonarlint: String by settings
    val spotless: String by settings

    plugins {
        id("fr.brouillard.oss.gradle.jgitver") version jgitver
        id("io.spring.dependency-management") version dependencyManagement
        id("org.springframework.boot") version springframeworkBoot
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("com.google.cloud.tools.jib") version jib
        id("com.google.protobuf") version protobufVer
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }
}
include("hw04-generics")
include("hw06-annotations")
include("hw08-gc")
include("hw08-gc:homework")
findProject(":hw08-gc:homework")?.name = "homework"
include("hw10-bytecode")
include("hw12-solid")
include("hw15-patterns")
include("hw16-io")
include("hw18-jdbc")
include("hw21-cache")
include("hw25-dependancy-injection")
include("hw20-hibernate")
include("hw24-webserver")
include("hw28-spring-jdbc")
include("hw32-concurrent-containers")
include("hw31-threads")
include("hw33-grpc")
include("hw37-web-flux")
include("hw37-web-flux:datastore-service")
findProject(":hw37-web-flux:datastore-service")?.name = "datastore-service"
include("hw37-web-flux:client-service")
findProject(":hw37-web-flux:client-service")?.name = "client-service"
