package su.jfdev.gradle.service.util

import org.gradle.api.*
import su.jfdev.gradle.service.dependency.*
import su.jfdev.gradle.service.describe.*

infix fun PackDependency.extend(pack: PackDependency) = pack depend this

infix fun PackDependency.depend(pack: PackDependency) {
    configuration.dependencies += pack
}

operator fun Project.get(name: String) = Pack(this, name)