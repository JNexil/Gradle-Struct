package su.jfdev.gradle.service.util

import org.gradle.api.artifacts.*
import su.jfdev.gradle.service.describe.*
import su.jfdev.gradle.service.describe.Module
import su.jfdev.gradle.service.describe.Service.*

fun Pack.depend(pack: Pack, scope: Scope) = this[scope].configuration depend pack[scope]
infix fun Pack.depend(packs: Packs) = depend(packs.dummy)
infix fun Pack.depend(pack: Pack) {
    for ((_1, configuration) in configurations.values) configuration.depend(pack)
}

infix fun Configuration.depend(pack: Pack) {
    for (dependency in pack.configurations.values) this depend dependency
}

infix fun Configuration.depend(dependency: Dependency){
    dependencies += dependency
}

operator fun Module.get(name: String) = fake(name)
private fun Module.fake(name: String) = Pack(this, name)

fun Module.target(name: String) = service.packs[name].orNull ?: fakeSet(name)
private fun Module.fakeSet(name: String) = setOf(fake(name))

val <T: Collection<*>>  T?.orNull: T? get() = when {
    this == null -> null
    isEmpty()    -> null
    else         -> this
}
