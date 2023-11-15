import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    kotlin("jvm") version "1.8.22" apply false
}

allprojects {
    defaultTasks = listOf("clean", "build", "jacocoTestReport")
    group = "org.suggs.fsm"
}

subprojects {
    repositories {
        mavenCentral()
    }

    apply {
        plugin("org.jetbrains.kotlin.jvm")
        plugin("org.gradle.jacoco")
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = JavaVersion.VERSION_17.majorVersion
        targetCompatibility = JavaVersion.VERSION_17.majorVersion
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = JavaVersion.VERSION_17.majorVersion
            incremental = false
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    dependencies {
        val implementation by configurations
        implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.22")
        implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.22")
        implementation("org.slf4j:slf4j-api:2.0.7")

        val testImplementation by configurations
        testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
        testImplementation("io.kotest:kotest-assertions-core:5.6.2")
        testImplementation("org.mockito:mockito-core:5.4.0")

        val testRuntimeOnly by configurations
        testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
        testRuntimeOnly("ch.qos.logback:logback-classic:1.4.8")
    }
}
