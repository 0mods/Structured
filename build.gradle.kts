import com.github.jengelman.gradle.plugins.shadow.relocation.*
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import java.util.regex.Pattern
import org.codehaus.plexus.util.SelectorUtils
import org.gradle.api.tasks.Input
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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

            relocate(KRelocator("native", "nonnative", ArrayList(), ArrayList(), true))
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

/**
 * @author HollowHorizon
 * @reason JDK isn't load packets with the name "native"
 */
class KRelocator: Relocator {
    @get:Input
    val patternIn: String

    @get:Input
    val pathPatternIn: String

    @get:Input
    val shadedPatternIn: String

    @get:Input
    val shadedPathPatternIn: String

    @get:Input
    val includesIn: HashSet<String>

    @get:Input
    val excludesIn: HashSet<String>

    @get:Input
    val rawStringIn: Boolean

    constructor(patt: String, shadedPattern: String, includes: List<String>, excludes: List<String>) : this(
        patt,
        shadedPattern,
        includes,
        excludes,
        false
    )

    constructor(
        patt: String,
        shadedPattern: String,
        includes: List<String>,
        excludes: List<String>,
        rawString: Boolean
    ) {
        this.rawStringIn = rawString

        if (rawString) {
            this.pathPatternIn = patt
            this.shadedPathPatternIn = shadedPattern

            this.patternIn = patt
            this.shadedPatternIn = shadedPattern
        } else {
            this.patternIn = patt.replace('/', '.')
            this.pathPatternIn = patt.replace('.', '/')

            this.shadedPatternIn = shadedPattern.replace('/', '.')
            this.shadedPathPatternIn = shadedPattern.replace('.', '/')
        }

        this.includesIn = normalizePatterns(*includes.toTypedArray())
        this.excludesIn = normalizePatterns(*excludes.toTypedArray())
    }

    fun include(pattern: String): KRelocator {
        this.includesIn.addAll(normalizePatterns(pattern))
        return this
    }

    fun exclude(pattern: String): KRelocator {
        this.excludesIn.addAll(normalizePatterns(pattern))
        return this
    }

    private fun normalizePatterns(vararg patterns: String): HashSet<String> {
        val normalized = LinkedHashSet<String>()

        if (patterns.isNotEmpty()) {

            for (pattern in patterns) {
                // Regex patterns don't need to be normalized and stay as is
                if (pattern.startsWith(SelectorUtils.REGEX_HANDLER_PREFIX)) {
                    normalized.add(pattern)
                    continue
                }

                val classPattern = pattern.replace('.', '/')

                normalized.add(classPattern)

                if (classPattern.endsWith("/*")) {
                    val packagePattern = classPattern.substring(0, classPattern.lastIndexOf('/'))
                    normalized.add(packagePattern)
                }
            }
        }

        return normalized
    }

    private fun isIncluded(path: String): Boolean {
        if (includesIn.isNotEmpty()) {
            for (include in includesIn) {
                if (SelectorUtils.matchPath(include, path, "/", true)) {
                    return true
                }
            }
            return false
        }
        return true
    }

    private fun isExcluded(path: String): Boolean {
        if (excludesIn.isNotEmpty()) {
            for (exclude in excludesIn) {
                if (SelectorUtils.matchPath(exclude, path, "/", true)) {
                    return true
                }
            }
        }
        return false
    }

    override fun canRelocatePath(path: String): Boolean {
        var pathMutable = path
        if (rawStringIn) {
            return Pattern.compile(pathPatternIn).matcher(pathMutable).find()
        }

        if (pathMutable.length < pathPatternIn.length) {
            return false
        }

        if (pathMutable.endsWith(".class")) {
            if (pathMutable.length == 6) {
                return false
            }
            pathMutable = pathMutable.substring(0, pathMutable.length - 6)
        }

        val pathStartsWithPattern =
            if (pathMutable.first() == '/') path.startsWith(pathPatternIn, 1) else path.startsWith(pathPatternIn)
        if (pathStartsWithPattern) {
            return isIncluded(pathMutable) && !isExcluded(pathMutable)
        }
        return false
    }

    override fun canRelocateClass(className: String): Boolean {
        return !rawStringIn &&
                className.indexOf('/') < 0 &&
                canRelocatePath(className.replace('.', '/'))
    }

    override fun relocatePath(context: RelocatePathContext): String {
        val path = context.path
        context.stats.relocate(pathPatternIn, shadedPathPatternIn)
        return if (rawStringIn) {
            path.replace(pathPatternIn, shadedPathPatternIn)
        } else {
            path.replaceFirst(pathPatternIn, shadedPathPatternIn)
        }
    }

    override fun relocateClass(context: RelocateClassContext): String {
        val clazz = context.className
        context.stats.relocate(pathPatternIn, shadedPathPatternIn)
        return clazz.replaceFirst(patternIn, shadedPatternIn)
    }

    override fun applyToSourceContent(sourceContent: String): String {
        return if (rawStringIn) {
            sourceContent
        } else {
            sourceContent.replace("\\b$patternIn", shadedPatternIn)
        }
    }
}