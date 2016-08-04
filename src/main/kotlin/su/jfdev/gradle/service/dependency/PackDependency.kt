package su.jfdev.gradle.service.dependency

import org.gradle.api.artifacts.*
import org.gradle.api.internal.artifacts.*
import su.jfdev.gradle.service.describe.*
import su.jfdev.gradle.service.describe.Module

data class PackDependency(val pack: Pack, val scope: Scope): Dependency, ResolvableDependency {

    val target: Module get() = pack.module

    val configuration: Configuration get() = target.configurations.getByName(scope[pack.name])

    override fun getGroup() = target.group.toString()
    override fun getName(): String = target.name
    override fun getVersion() = target.version.toString()

    override fun contentEquals(other: Dependency) = equals(other)

    override fun copy() = copy(pack, scope)

    override fun resolve(ctx: DependencyResolveContext) {
        val transit = target.dependencies.project(mapOf("path" to target.path, "configuration" to configuration))
        ctx.add(transit)

        ctx.add(pack.sourceSet.output)
    }
}