package su.jfdev.gradle.service

import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.tasks.*

private val nameExtractors: Sequence<SourceSet.() -> String>
        = sequenceOf<(SourceSet) -> String>({ it.compileConfigurationName },
                                        { it.compileClasspathConfigurationName },
                                        { it.compileOnlyConfigurationName },
                                        { it.runtimeConfigurationName })

val Project.extractors: Sequence<SourceSet?.() -> Configuration?> get() = nameExtractors.map { extractName ->
    val extractor: (SourceSet?) -> Configuration? = {
        if (it == null) null
        else configurations.findByName(it.extractName())
    }
    extractor
}