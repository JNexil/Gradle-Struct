package su.jfdev.gradle.service.describe

import org.gradle.api.tasks.*
import su.jfdev.gradle.service.dependency.*

class Pack(val module: Module, val name: String) {
    val sourceSet: SourceSet = module.sources.maybeCreate(name)
    val configurations: Map<Scope, PackDependency> = Scope.values().associate {
        it to PackDependency(this, it)
    }

    operator fun get(scope: Scope) = configurations[scope]!!
}