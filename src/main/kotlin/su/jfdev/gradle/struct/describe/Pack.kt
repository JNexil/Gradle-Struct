package su.jfdev.gradle.struct.describe

import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.tasks.*
import org.gradle.jvm.tasks.*
import su.jfdev.gradle.struct.util.*

data class Pack(val project: Project, val name: String) {

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
        this[scope].dependencies += PackDependency(scope, pack)
    }

    operator fun get(scope: Scope): Configuration {
        val name = scope.nameExtractor(sourceSet)
        return project.configurations.getByName(name)
    }

    fun archive(name: String = this.name): Pack = apply {
        val task = project.tasks.maybeCreate(this.name + "Jar", Jar::class.java).apply {
            classifier = name
            from(sourceSet.output)
        }
        project.artifacts.add("archives", task)
    }
}