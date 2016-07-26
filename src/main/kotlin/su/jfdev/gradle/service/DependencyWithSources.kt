package su.jfdev.gradle.service

import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.internal.artifacts.*
import org.gradle.api.tasks.*

class DependencyWithSources(val project: Project,
                            val sources: String,
                            val configuration: String): ResolvableDependency, Dependency {
    val sourceOut: SourceSetOutput = project.extractSourceOut(sources)
    val dependency: Dependency = project.extractProjectOut(configuration)

    override fun getGroup() = project.group.toString()

    override fun getName(): String = project.name

    override fun getVersion(): String = project.version.toString()

    override fun copy() = DependencyWithSources(project, sources, configuration)

    override fun contentEquals(other: Dependency) = other is DependencyWithSources
                                                    && other.project == project
                                                    && other.sources == sources
                                                    && other.configuration == configuration

    override fun resolve(context: DependencyResolveContext) {
        if (dependency is ResolvableDependency) dependency.resolve(context)
        else project.logger.warn("Dependency $dependency is not resolvable")
        context.add(sourceOut)
    }

    companion object {
        private fun Project.extractProjectOut(conf: String)
                = dependencies.project(mapOf("path" to path, "configuration" to conf))

        private fun Project.extractSourceOut(source: String)
                = sourceSets.getByName(source).output.apply {
            println("$source = ${this@extractSourceOut}")
        }
    }
}