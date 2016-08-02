package su.jfdev.gradle.service.util

import org.gradle.api.artifacts.*
import su.jfdev.gradle.service.describe.*
import su.jfdev.gradle.service.describe.Module

fun Pack.depend(scope: Scope, pack: Pack, configuration: Configuration = this[scope])
        = pack.extend(scope, this, configuration)

fun Pack.extend(scope: Scope, pack: Pack, configuration: Configuration = this[scope]) {
    val packConfiguration = pack[scope]

    configuration.extendsFrom(packConfiguration)

    val configurationDependency = dependency(configuration)
    packConfiguration.dependencies.add(configurationDependency)
}

fun Pack.depend(pack: Pack) = pack.extend(this)

fun Pack.extend(pack: Pack) {
    for ((scope, configuration) in configurations) {
        extend(scope, pack, configuration)
    }
}

operator fun Module.get(name: String) = service.packs[name].orNull ?: fakeSet(name)
fun Module.target(name: String) = service.packs[name].orNull?.dummy ?: fake(name)

private fun Module.fakeSet(name: String) = setOf(fake(name))
private fun Module.fake(name: String) = Pack(this, name)

private val <T: Collection<*>>  T?.orNull: T? get() = when {
    this == null -> null
    isEmpty()    -> null
    else         -> this
}
