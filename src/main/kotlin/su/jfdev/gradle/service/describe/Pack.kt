package su.jfdev.gradle.service.describe

import org.gradle.api.*
import org.gradle.api.tasks.*
import su.jfdev.gradle.service.dependency.*
import su.jfdev.gradle.service.util.*

data class Pack(val project: Project, val name: String) {

    val sourceSet: SourceSet? get() = project.sourceSets.findByName(name)

    val configurations: Map<Scope, PackDependency> = Scope.values().associate {
        it to PackDependency(this, it)
    }

    @JvmName("getAt")
    operator fun get(scope: Scope) = configurations[scope]!!

    companion object {
        operator fun get(project: Project, name: String) = Pack(project, name).apply {
            project.sourceSets.maybeCreate(name)
        }
    }
}