package su.jfdev.gradle.struct.describe

import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.file.*
import org.gradle.api.tasks.*
import su.jfdev.gradle.struct.util.*

data class Pack(val project: Project, val name: String) {

    var archive: String = joinPath()
    var includeResources: Boolean = false
    val sourceSet: SourceSet = project.sourceSets.maybeCreate(name)

    infix fun extend(pack: Pack): Pack = eachScope {
        extend(pack, it)
    }

    infix fun depend(pack: Pack): Pack = eachScope {
        depend(pack, it)
    }

    private inline fun Pack.eachScope(block: (Scope) -> Unit) = apply {
        for (scope in Scope.values()) block(scope)
    }

    fun extend(pack: Pack, scope: Scope): Pack = apply {
        pack.depend(this, scope)
    }

    fun depend(pack: Pack, scope: Scope): Pack = apply {
        scope.configuration.dependencies += PackDependency(scope, pack)
        scope.classpath += pack.sourceSet.output
    }

    operator fun get(scope: Scope) = scope.configuration

    private val Scope.configuration: Configuration
        get() = project.configurations.getByName(sourceSet.getConfiguration())

    private var Scope.classpath: FileCollection
        get() = sourceSet.getClasspath()
        set(value) = sourceSet.setClasspath(value)
}