package su.jfdev.gradle.service.util

import org.codehaus.groovy.runtime.*
import org.gradle.api.*
import org.gradle.api.tasks.*
import su.jfdev.gradle.service.describe.*

fun Any.getAnyProperty(property: String): Any? = InvokerHelper.getProperty(this, property)
inline fun <reified T> Any.getProperty(property: String) = getAnyProperty(property) as T
fun Any.setProperty(property: String, value: Any) = InvokerHelper.setProperty(this, property, value)

val Project.ext: ExtraExtension get() = ExtraExtension(this)
val Project.sourceSets: SourceSetContainer get() = getProperty("sourceSets")
val Project.module: Module get() = ext["serviceModule"]

class ExtraExtension(val project: Project) {
    operator fun set(property: String, value: Any) = project.extensions.extraProperties.set(property, value)

    fun getProperty(property: String): Any? = project.extensions.extraProperties.get(property)

    inline operator fun <reified T> get(property: String): T = getProperty(property) as T
}