package su.jfdev.gradle.struct.util

import org.gradle.api.*
import org.gradle.api.plugins.*
import su.jfdev.gradle.struct.describe.*
import java.util.*

operator fun Project.get(name: String) = Pack(this, name)

inline fun <reified T: Any> Project.extOrWarn(name: String): T? = extensions.get<T>(name).apply {
    this ?: warnException(name)
}

fun Project.warnException(name: String) = logger.warn("Not found extension $name")

inline operator fun <reified T> ExtensionContainer.get(name: String) = findByName(name) as? T

inline fun <reified T: Any> Project.addContainer(name: String, crossinline make: (String) -> T)
        = makeContainer(make).apply { extensions.add(name, this) }

infix inline fun <reified T: Any> Project.makeContainer(crossinline make: (String) -> T): NamedDomainObjectContainer<T>
        = container(T::class.java) { make(it) }

internal fun Pack.joinPath(): String = LinkedList<String>().run {
    var current = project

    while (current.rootProject != current){
        addFirst(current.name)
        current = current.rootProject
    }

    add(name)
    return joinToString(separator = "-")
}