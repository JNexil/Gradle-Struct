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

operator fun Module.get(name: String): Set<Pack> = service.packs[name] ?: setOf(Pack(this, name))
fun Module.getMain(name: String): Pack = get(name).single { it.name == name }