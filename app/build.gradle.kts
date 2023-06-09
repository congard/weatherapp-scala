/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Scala application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/8.1.1/userguide/building_java_projects.html
 * This project uses @Incubating APIs which are subject to change.
 */

plugins {
    // Apply the scala Plugin to add support for Scala.
    scala

    // Apply the application plugin to add support for building a CLI application in Java.
    application

    id("org.openjfx.javafxplugin") version("0.0.10")
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use Scala 3.2.2 in our library project
    // https://mvnrepository.com/artifact/org.scala-lang/scala3-library_3/3.2.2
    implementation("org.scala-lang:scala3-library_3:3.2.2")

    // This dependency is used by the application.
    implementation("com.google.guava:guava:31.1-jre")

    // https://mvnrepository.com/artifact/com.lihaoyi/requests
    implementation("com.lihaoyi:requests_3:0.8.0")

    // https://mvnrepository.com/artifact/com.lihaoyi/upickle
    implementation("com.lihaoyi:upickle_3:3.1.0")
}

testing {
    suites {
        // Configure the built-in test suite
        val test by getting(JvmTestSuite::class) {
            // Use JUnit4 test framework
            useJUnit("4.13.2")

            dependencies {
                // Use Scalatest for testing our library
                implementation("org.scalatest:scalatest_3:3.2.16")
                implementation("org.scalatestplus:junit-4-13_2.13:3.2.2.0")

                // Need scala-xml at test runtime
                runtimeOnly("org.scala-lang.modules:scala-xml_2.13:1.2.0")
            }
        }
    }
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    // Define the main class for the application.
    mainClass.set("pl.edu.agh.congard.weatherapp.Main")
}

javafx {
    version = "17"
    modules("javafx.controls")
}
