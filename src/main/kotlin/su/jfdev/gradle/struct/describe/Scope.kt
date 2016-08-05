package su.jfdev.gradle.struct.describe

import org.gradle.api.tasks.*

enum class Scope(val nameExtractor: SourceSet.() -> String) {

    RUNTIME(nameExtractor = { runtimeConfigurationName }),

    COMPILE(nameExtractor = { compileConfigurationName })
}