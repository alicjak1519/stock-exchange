import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    idea
    kotlin("jvm") version ("1.4.10")
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
    }
}

subprojects {
    group = "pl.agh"

    apply {
        plugin("java")
    }

    dependencies {
        val junitVersion = "5.7.0"
        implementation("org.jetbrains:annotations:19.0.0")
        implementation("com.google.guava:guava:23.1-jre")
        testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
        testImplementation("org.assertj:assertj-core:3.18.1")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_15.toString()
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

