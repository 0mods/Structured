import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.text.SimpleDateFormat
import java.util.*

val minecraftVersion: String by project
val modName: String by project
val modAuthor: String by project
val kotlin_version: String by project

plugins {
    java
    idea
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    id("org.jetbrains.dokka") version "1.8.20"
    id("com.github.johnrengelman.shadow") version "8.+"
}

allprojects {
    repositories {
        mavenCentral()
        maven("https://repo.spongepowered.org/repository/maven-public/") {
            name = "Sponge / Mixin"
        }
        maven("https://maven.blamejared.com") {
            name = "BlameJared Maven (CrT / Bookshelf)"
        }
    }

    tasks.withType<GenerateModuleMetadata> {
        enabled = false
    }
}

subprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "org.jetbrains.dokka")
    apply(plugin = "com.github.johnrengelman.shadow")

    configurations {
        implementation.get().extendsFrom(this["shadow"])
    }

    extensions.configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))
        withJavadocJar()
        withSourcesJar()
    }

    dependencies {
        val coroutines_version: String by project
        val serialization_version: String by project
        val shadow = configurations["shadow"]
        shadow("org.jetbrains.kotlin:kotlin-reflect:${kotlin.coreLibrariesVersion}")
        shadow("org.jetbrains.kotlin:kotlin-stdlib:${kotlin.coreLibrariesVersion}")
        shadow("org.jetbrains.kotlin:kotlin-stdlib-common:${kotlin.coreLibrariesVersion}")
        shadow("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutines_version}")
        shadow("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${coroutines_version}")
        shadow("org.jetbrains.kotlinx:kotlinx-serialization-core:${serialization_version}")
        shadow("org.jetbrains.kotlinx:kotlinx-serialization-json:${serialization_version}")
    }

    tasks {
        getByName("build").dependsOn("shadowJar")
        getByName<ShadowJar>("shadowJar") {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE

            configurations = listOf(project.configurations.getByName("shadow"))
        }
        jar {
            manifest {
                attributes(
                        "Specification-Title" to modName,
                        "Specification-Vendor" to modAuthor,
                        "Specification-Version" to archiveVersion,
                        "Implementation-Title" to project.name,
                        "Implementation-Version" to archiveVersion,
                        "Implementation-Vendor" to modAuthor,
                        "Implementation-Timestamp" to SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(Date()),
                        "Timestamp" to System.currentTimeMillis(),
                        "Built-On-Java" to "${System.getProperty("java.vm.version")} (${System.getProperty("java.vm.vendor")})",
                        "Build-On-Minecraft" to minecraftVersion
                )
            }
        }
    }

    kotlin {
        jvmToolchain(17)
    }
}