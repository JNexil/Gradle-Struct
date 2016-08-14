package su.jfdev.gradle.struct.util

import org.gradle.api.*
import org.gradle.api.plugins.*
import su.jfdev.gradle.struct.describe.*

operator fun Project.get(name: String) = Pack(this, name)

inline operator fun <reified T> ExtensionContainer.get(name: String) = findByName(name) as? T

inline fun <reified T: Any> Project.onlyWith(name: String, action: T.() -> Unit) {
    val value: T = extensions[name] ?: return
    value.action()
}