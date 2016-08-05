package su.jfdev.gradle.service.describe

import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.internal.artifacts.*
import org.gradle.api.tasks.*
import su.jfdev.gradle.service.util.*

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
        pack[scope].dependencies += Out(scope)
    }

    fun depend(pack: Pack, scope: Scope): Pack = apply {
        pack.extend(this, scope)
    }

    private operator fun get(scope: Scope): Configuration {
        val name = scope.nameExtractor(sourceSet)
        return project.configurations.getByName(name)
    }

    private inner class Out(val scope: Scope): Dependency, ResolvableDependency {
        private val pack: Pack get() = this@Pack

        override fun getName(): String = project.name
        override fun getGroup() = project.group.toString()
        override fun getVersion() = project.version.toString()

        override fun copy() = Out(scope)

        override fun contentEquals(other: Dependency) = other is Out && other.pack == pack && scope == other.scope

        override fun resolve(ctx: DependencyResolveContext) = ctx.run {
            val configuration = get(scope)
            add(configuration)
            add(sourceSet.output)
        }
    }
}