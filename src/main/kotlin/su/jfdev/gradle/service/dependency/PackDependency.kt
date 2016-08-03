package su.jfdev.gradle.service.dependency

import org.gradle.api.artifacts.*
import org.gradle.api.internal.artifacts.*
import su.jfdev.gradle.service.describe.*
import su.jfdev.gradle.service.describe.Module

data class PackDependency(val pack: Pack, val configuration: Configuration): Dependency, ResolvableDependency {

    val target: Module get() = pack.module

    override fun getGroup() = target.group.toString()
    override fun getName(): String = target.name
    override fun getVersion() = target.version.toString()

    override fun contentEquals(other: Dependency) = equals(other)

    override fun copy() = copy(pack, configuration)

    override fun resolve(ctx: DependencyResolveContext) {
        val transit = target.dependencies.project(mapOf("path" to target.path, "configuration" to configuration))
        ctx.add(transit)

        val sourceSet = pack.sourceSet ?: return
        ctx.add(sourceSet.output)
    }
}