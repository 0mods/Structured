import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    idea
    `maven-publish`
    id("fabric-loom") version "1.3.8"
}

val minecraftVersion: String by project
val fabricLoaderVersion: String by project
val modName: String by project
val modId: String by project
val coroutines_version: String by project
val serialization_version: String by project

val baseArchiveName = "${modName}-fabric-${minecraftVersion}"

base {
    archivesName.set(baseArchiveName)
}

sourceSets {
    main {
        kotlin.srcDirs("src/main/java")
    }
}

repositories {
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/releases/")
}

tasks.withType<KotlinCompile> {
    source(project(":Common").sourceSets.main.get().allSource)
}

dependencies {
    minecraft("com.mojang:minecraft:${minecraftVersion}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${fabricLoaderVersion}")
    implementation(project(":Common"))
//    shadow("org.jetbrains.kotlin:kotlin-reflect:${kotlin.coreLibrariesVersion}")
//    shadow("org.jetbrains.kotlin:kotlin-stdlib:${kotlin.coreLibrariesVersion}")
//    shadow("org.jetbrains.kotlin:kotlin-stdlib-common:${kotlin.coreLibrariesVersion}")
//    shadow("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutines_version}")
//    shadow("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:${coroutines_version}")
//    shadow("org.jetbrains.kotlinx:kotlinx-serialization-core:${serialization_version}")
//    shadow("org.jetbrains.kotlinx:kotlinx-serialization-json:${serialization_version}")

}

loom {
    accessWidenerPath.set(file("src/main/resources/structured.accesswidener"))
    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir("run")
        }
        named("server") {
            server()
            configName = "Fabric Server"
            ideConfigGenerated(true)
            runDir("run")
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

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${modName}" }
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    configurations.getByName("shadow").onEach {
        from(project.zipTree(it)) {
            exclude("META-INF", "META-INF/**")
        }
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

