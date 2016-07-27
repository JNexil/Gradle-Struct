package su.jfdev.gradle.service.util

import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.tasks.*

private val nameExtractors = sequenceOf <SourceSet.() -> String>({ compileConfigurationName },
                                                                 { compileClasspathConfigurationName },
                                                                 { compileOnlyConfigurationName },
                                                                 { runtimeConfigurationName })

val Project.extractors: Sequence<SourceSet?.() -> Configuration?> get() = nameExtractors.map { extractName ->
    val extractor: SourceSet?.() -> Configuration? = {
        if (this == null) null
        else configurations.findByName(extractName())
    }
    extractor
}