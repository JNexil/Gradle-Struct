package su.jfdev.gradle.service.util

import su.jfdev.gradle.service.dependency.*
import su.jfdev.gradle.service.describe.*

infix fun Pack.depend(pack: Pack) {
    for (scope in Scope.values()) depend(pack, scope)
}

fun Pack.depend(pack: Pack, scope: Scope) = this[scope] depend pack[scope]

infix fun PackDependency.depend(pack: PackDependency) {
    configuration.dependencies += pack
}

operator fun Module.get(name: String) = fake(name)
private fun Module.fake(name: String) = Pack(this, name)

val <T: Collection<*>>  T?.orNull: T? get() = when {
    this == null -> null
    isEmpty()    -> null
    else         -> this
}
