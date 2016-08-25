package su.jfdev.gradle.struct.describe

import org.gradle.api.artifacts.*
import org.gradle.api.internal.artifacts.*
import su.jfdev.gradle.struct.util.*

data class PackDependency(val scope: Scope, val pack: Pack): Dependency, ResolvableDependency {

    private val name = pack.path.joinToString(separator = "-")

    override fun getName() = name
    override fun getGroup() = pack.project.group.toString()
    override fun getVersion() = pack.project.version.toString()

    override fun copy() = PackDependency(scope, pack)

    override fun contentEquals(other: Dependency) = equals(other)

    override fun resolve(ctx: DependencyResolveContext) = pack.sourceSet.run {
        if (ctx.isTransitive) ctx.add(pack[scope])
        if (pack.includeResources) ctx.add(resources)
        ctx.add(output)
    }
}