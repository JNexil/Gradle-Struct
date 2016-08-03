package su.jfdev.gradle.service.describe

import org.gradle.api.tasks.*
import su.jfdev.gradle.service.dependency.*

class Pack(val module: Module, val name: String, real: Boolean = false) {
    init {
        if (real) module.sources.maybeCreate(name)
    }

    val sourceSet: SourceSet? get() = module.sources.findByName(name)
    val configurations: Map<Scope, PackDependency> = Scope.values().associate {
        val configuration = module.configurations.maybeCreate(it[name])
        it to PackDependency(this, configuration)
    }

    operator fun get(scope: Scope): PackDependency = configurations[scope]!!

    val isDummy: Boolean get() = sourceSet == null
}