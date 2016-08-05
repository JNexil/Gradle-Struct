package su.jfdev.gradle.service.describe

import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.tasks.*
import su.jfdev.gradle.service.util.*

enum class Scope(private val extractor: SourceSet.() -> String) {
    RUNTIME({ runtimeConfigurationName }),
    COMPILE({ compileConfigurationName });

    @JvmName("getAt")
    operator fun get(project: Project, name: String): Configuration {
        val configuration = project.sourceSets.getByName(name).extractor()
        return project.configurations.getByName(configuration)
    }
}