package su.jfdev.gradle.struct.describe

import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.publish.*
import org.gradle.api.publish.maven.*
import org.gradle.api.tasks.*
import org.gradle.jvm.tasks.*
import su.jfdev.gradle.struct.util.*

data class Pack private constructor(val project: Project, val sourceSet: SourceSet) {

    constructor(project: Project, name: String, create: Boolean = true): this(project,
                                                                              project.sourceSets[name, create])

    val name: String get() = sourceSet.name

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

    infix fun resourcesTo(pack: Pack): Pack = apply {
        pack resourcesFrom this
    }

    infix fun resourcesFrom(pack: Pack): Pack = apply {
        val receiver = sourceSet.resources
        val target = pack.sourceSet.resources
        receiver.add(target)
    }

    fun archive(name: String = this.name): Pack = apply {
        val task = project.tasks.maybeCreate(this.name + "Jar", Jar::class.java).apply {
            classifier = name
            from(sourceSet.output)
        }
        project.onlyWith<PublishingExtension> {
            publications.maybeCreate("general", MavenPublication::class.java).apply {
                artifact(task)
            }
        }
        project.artifacts.add("archives", task)
    }

    private companion object {
        private operator fun SourceSetContainer.get(name: String, create: Boolean) = when {
            create        -> maybeCreate(name)
            name in names -> getByName(name)
            else          -> error("Source set $name !in $this")
        }
    }
}