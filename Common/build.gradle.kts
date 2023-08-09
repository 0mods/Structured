import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    `maven-publish`
    id("org.spongepowered.gradle.vanilla") version "0.2.1-SNAPSHOT"
}

val minecraftVersion: String by project
val commonRunsEnabled: String by project
val commonClientRunName: String? by project
val commonServerRunName: String? by project
val modName: String by project
val modId: String by project
val coroutines_version: String by project
val serialization_version: String by project

val baseArchiveName = "${modName}-common-${minecraftVersion}"

base {
    archivesName.set(baseArchiveName)
}

sourceSets {
    main {
        kotlin.srcDirs("src/main/java")
    }
}

minecraft {
    version(minecraftVersion)
    runs {
        if (commonRunsEnabled == "true") {
            client(commonClientRunName ?: "vanilla_client") {
                workingDirectory(project.file("run"))
            }
            server(commonServerRunName ?: "vanilla_server") {
                workingDirectory(project.file("run"))
            }
        }
    }
}

dependencies {
    compileOnly("org.spongepowered:mixin:0.8.5")
//    shadow("org.jetbrains.kotlin:kotlin-reflect:${kotlin.coreLibrariesVersion}")
//    shadow("org.jetbrains.kotlin:kotlin-stdlib:${kotlin.coreLibrariesVersion}")
//    shadow("org.jetbrains.kotlin:kotlin-stdlib-common:${kotlin.coreLibrariesVersion}")
//    shadow("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutines_version}")
//    shadow("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${coroutines_version}")
//    shadow("org.jetbrains.kotlinx:kotlinx-serialization-core:${serialization_version}")
//    shadow("org.jetbrains.kotlinx:kotlinx-serialization-json:${serialization_version}")
}

tasks.processResources {
    val buildProps = project.properties

    filesMatching("pack.mcmeta") {
        expand(buildProps)
    }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            artifactId = baseArchiveName
            from(components["java"])
        }
    }

    repositories {
        maven("file://${System.getenv("local_maven")}")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
    kotlinOptions.freeCompilerArgs = listOf("-Xjvm-default=all", "-Xopt-in=kotlin.RequiresOptIn")
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    configurations.getByName("shadow").onEach {
        from(project.zipTree(it)) {
            exclude("META-INF", "META-INF/**")
        }
    }
}