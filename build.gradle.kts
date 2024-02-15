import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES
import org.gradle.plugins.ide.idea.model.IdeaLanguageLevel

plugins {
    idea
    id("io.spring.dependency-management")
    id("org.springframework.boot") apply false
    id("name.remal.sonarlint") apply false
    id("com.diffplug.spotless") apply false
}

idea {
    project {
        languageLevel = IdeaLanguageLevel(21)
    }
    module {
        isDownloadJavadoc = true
        isDownloadSources = true
    }
}


allprojects {
    group = "ru.dgp"

    repositories {
        mavenLocal()
        mavenCentral()
    }

    val testcontainersBom: String by project
    val protobufBom: String by project
    val guava: String by project
    val jmh: String by project
    val asm: String by project
    val glassfishJson: String by project
    val ehcache: String by project

    val mongodb: String by project
    val mongodbReactive: String by project
    val cassandra: String by project
    val neo4j: String by project
    val jedis: String by project

    val jetty: String by project
    val jettyServlet: String by project
    val freemarker: String by project

    apply(plugin = "io.spring.dependency-management")
    dependencyManagement {
        dependencies {
            imports {
                mavenBom(BOM_COORDINATES)
                mavenBom("org.testcontainers:testcontainers-bom:$testcontainersBom")
                mavenBom("com.google.protobuf:protobuf-bom:$protobufBom")
            }
            dependency("com.google.guava:guava:$guava")
            dependency("org.openjdk.jmh:jmh-core:$jmh")
            dependency("org.openjdk.jmh:jmh-generator-annprocess:$jmh")
            dependency("org.ow2.asm:asm-commons:$asm")
            dependency("org.glassfish:jakarta.json:$glassfishJson")
            dependency("org.ehcache:ehcache:$ehcache")

            dependency("com.datastax.oss:java-driver-core:$cassandra")
            dependency("org.mongodb:mongodb-driver-core:$mongodb")
            dependency("org.mongodb:mongodb-driver-sync:$mongodb")
            dependency("org.mongodb:bson:$mongodb")
            dependency("org.mongodb:mongodb-driver-reactivestreams:${mongodbReactive}")
            dependency("org.neo4j.driver:neo4j-java-driver:$neo4j")
            dependency("redis.clients:jedis:$jedis")

            dependency("org.eclipse.jetty.ee10:jetty-ee10-servlet:$jetty")
            dependency("org.eclipse.jetty:jetty-server:$jetty")
            dependency("org.eclipse.jetty.ee10:jetty-ee10-webapp:$jetty")
            dependency("org.eclipse.jetty:jetty-security:$jetty")
            dependency("org.eclipse.jetty:jetty-http:$jetty")
            dependency("org.eclipse.jetty:jetty-io:$jetty")
            dependency("org.eclipse.jetty:jetty-util:$jetty")
            dependency("org.freemarker:freemarker:$freemarker")
        }
    }
}

subprojects {
    plugins.apply(JavaPlugin::class.java)
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.addAll(listOf("-Xlint:all,-serial,-processing"))
    }

    apply<name.remal.gradle_plugins.sonarlint.SonarLintPlugin>()
    apply<com.diffplug.gradle.spotless.SpotlessPlugin>()
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        java {
            palantirJavaFormat("2.38.0")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
        testLogging.showExceptions = true
        reports {
            junitXml.required.set(true)
            html.required.set(true)
        }
    }
}

tasks {
    val managedVersions by registering {
        doLast {
            project.extensions.getByType<DependencyManagementExtension>()
                .managedVersions
                .toSortedMap()
                .map { "${it.key}:${it.value}" }
                .forEach(::println)
        }
    }
}