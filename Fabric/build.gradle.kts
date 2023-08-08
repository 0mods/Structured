plugins {
    idea
    `maven-publish`
    id("fabric-loom") version "1.3.8"
    kotlin("jvm")
}

val minecraftVersion: String by project
val fabricLoaderVersion: String by project
val modName: String by project
val modId: String by project
val coroutines_version: String by project
val serialization_version: String by project
val shadow: Configuration by configurations.creating

val baseArchiveName = "${modName}-fabric-${minecraftVersion}"

base {
    archivesName.set(baseArchiveName)
}

sourceSets {
    main {
        java.srcDirs("src/disabled")
        kotlin.srcDirs("src/main/java")
        resources.srcDirs("src/main/resources")
    }
}

repositories {
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:${minecraftVersion}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
    implementation(project(":Common"))
    shadow("org.jetbrains.kotlin:kotlin-reflect:${kotlin.coreLibrariesVersion}")
    shadow("org.jetbrains.kotlin:kotlin-stdlib:${kotlin.coreLibrariesVersion}")
    shadow("org.jetbrains.kotlin:kotlin-stdlib-common:${kotlin.coreLibrariesVersion}")
    shadow("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutines_version}")
    shadow("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${coroutines_version}")
    shadow("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:${coroutines_version}")
    shadow("org.jetbrains.kotlinx:kotlinx-serialization-core:${serialization_version}")
    shadow("org.jetbrains.kotlinx:kotlinx-serialization-json:${serialization_version}")

}

loom {
    accessWidenerPath.set(file("src/main/resources/structured.accesswidener"))
    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir("run")
            vmArg("-XX:+AllowEnhancedClassRedefinition")
        }
        named("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
            runDir("run")
            vmArg("-XX:+AllowEnhancedClassRedefinition")
        }
    }
}


tasks.processResources {
    from(project(":Common").sourceSets.main.get().resources)
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.withType<JavaCompile> {
    source(project(":Common").sourceSets.main.get().allSource)
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${modName}" }
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