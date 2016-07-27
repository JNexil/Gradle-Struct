package su.jfdev.gradle.service.util

import org.codehaus.groovy.runtime.*
import org.gradle.api.*
import org.gradle.api.tasks.*
import kotlin.reflect.*

private inline operator fun <reified T> Any.get(name: String): T = InvokerHelper.getProperty(this, name) as T
val Project.sourceSets: SourceSetContainer get() = get("sourceSets")

fun <T: Any> Project.extension(clazz: KClass<T>): T = extensions.getByType(clazz.java)
inline fun <reified T: Any> Project.extension(): T = extension(T::class)

fun <T: Any> Project.ext(clazz: KClass<T>) = lazy { extension(clazz) }
inline fun <reified T: Any> Project.ext() = ext(T::class)