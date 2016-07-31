package su.jfdev.gradle.service.describe

import org.gradle.api.artifacts.*
import org.gradle.api.internal.artifacts.*

data class ConfigurationDependency(val target: Module,
                                   val source: String,
                                   val configuration: Configuration): Dependency, ResolvableDependency {

    override fun getGroup() = target.group.toString()
    override fun getName(): String = target.name
    override fun getVersion() = target.version.toString()

    override fun copy() = copy(target, source, configuration)

    override fun contentEquals(other: Dependency) = equals(other)

    override fun resolve(ctx: DependencyResolveContext) {
        val transit = target.dependencies.project(mapOf("path" to target.path, "configuration" to configuration))
        ctx.add(transit)

        val sourceSet = target.sources.findByName(source) ?: return
        ctx.add(sourceSet.output)
    }
}

fun Pack.dependency(configuration: Configuration) = ConfigurationDependency(module, name, configuration)