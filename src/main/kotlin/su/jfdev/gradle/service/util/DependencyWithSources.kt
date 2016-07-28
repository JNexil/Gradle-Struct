package su.jfdev.gradle.service.util

import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.internal.artifacts.*
import org.gradle.api.tasks.*

data class DependencyWithSources(val project: Project,
                                 val sources: String,
                                 val configuration: String): ResolvableDependency, Dependency {
    val sourceOut: SourceSetOutput = project.sourceSets.getByName(sources).output
    val dependency: Dependency = project.run {
        val conf = makeConfigName(sources, configuration)
        val configuration = mapOf("path" to path, "configuration" to conf)
        dependencies.project(configuration)
    }

    override fun getGroup() = project.group.toString()
    override fun getName(): String = project.name
    override fun getVersion(): String = project.version.toString()

    override fun copy() = DependencyWithSources(project, sources, configuration)

    override fun contentEquals(other: Dependency) = other is DependencyWithSources
                                                    && other.project == project
                                                    && other.sources == sources

    override fun resolve(context: DependencyResolveContext) {
        if (dependency is ResolvableDependency) dependency.resolve(context)
        else project.logger.warn("Dependency $dependency is not resolvable")
        context.add(sourceOut)
    }
}