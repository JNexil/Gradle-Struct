package su.jfdev.gradle.service.util

import org.gradle.api.*
import su.jfdev.gradle.service.dependency.*
import su.jfdev.gradle.service.describe.*

infix fun Pack.depend(pack: Pack) {
    for (scope in Scope.values()) depend(pack, scope)
}

fun Pack.depend(pack: Pack, scope: Scope) = this[scope] depend pack[scope]

infix fun PackDependency.depend(pack: PackDependency) {
    configuration.dependencies += pack
}

operator fun Project.get(name: String) = Pack(this, name)