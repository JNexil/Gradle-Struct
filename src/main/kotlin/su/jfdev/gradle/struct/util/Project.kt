package su.jfdev.gradle.struct.util

import org.gradle.api.*
import su.jfdev.gradle.struct.describe.*

operator fun Project.get(name: String) = Pack(this, name)

inline fun <reified T: Any> Project.onlyWith(name: String, block: T.() -> Unit) {
    val extension = extensions.findByName(name) ?: return
    if (extension is T) block(extension)
}