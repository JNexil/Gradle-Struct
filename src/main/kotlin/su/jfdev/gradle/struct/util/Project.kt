package su.jfdev.gradle.struct.util

import org.gradle.api.*
import su.jfdev.gradle.struct.describe.*

operator fun Project.get(name: String, create: Boolean = true) = Pack(this, name, create)

inline fun <reified T: Any> Project.onlyWith(block: T.() -> Unit) {
    project.extensions.findByType(T::class.java)?.apply(block)
}