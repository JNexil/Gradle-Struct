package su.jfdev.gradle.struct.describe

import org.gradle.api.file.*
import org.gradle.api.tasks.*

enum class Scope(val getConfiguration: SourceSet.() -> String,
                 val setClasspath: SourceSet.(FileCollection) -> Unit,
                 val getClasspath: SourceSet.() -> FileCollection) {

    RUNTIME(getConfiguration = SourceSet::getRuntimeConfigurationName,
            setClasspath = SourceSet::setRuntimeClasspath,
            getClasspath = SourceSet::getRuntimeClasspath),

    COMPILE(getConfiguration = SourceSet::getCompileConfigurationName,
            setClasspath = SourceSet::setCompileClasspath,
            getClasspath = SourceSet::getCompileClasspath),
}