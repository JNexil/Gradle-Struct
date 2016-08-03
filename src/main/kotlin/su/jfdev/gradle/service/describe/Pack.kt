package su.jfdev.gradle.service.describe

import org.gradle.api.artifacts.*
import org.gradle.api.tasks.*

class Pack(val module: Module, val name: String, isDummy: Boolean = false) {
    init {
        if(!isDummy) createSource()
    }

    internal fun createSource() = apply {
        module.sources.maybeCreate(name)
    }

    val sourceSet: SourceSet? get() = module.sources.findByName(name)
    val configurations: Map<Scope, Configuration> = Scope.values().associate {
        it to module.configurations.maybeCreate(it[name])
    }

    operator fun get(scope: Scope): Configuration = configurations[scope]!!
}