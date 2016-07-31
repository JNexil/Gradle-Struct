package su.jfdev.gradle.service.util

import org.codehaus.groovy.runtime.*
import org.gradle.api.*
import org.gradle.api.tasks.*
import su.jfdev.gradle.service.describe.*

fun Any.getAnyProperty(property: String): Any? = InvokerHelper.getProperty(this, property)
inline fun <reified T> Any.getProperty(property: String) = getAnyProperty(property) as T
fun Any.setProperty(property: String, value: Any) = InvokerHelper.setProperty(this, property, value)

val Project.ext: Any get() = getProperty("ext")
val Project.sourceSets: SourceSetContainer get() = getProperty("sourceSets")
val Project.module: Module get() = ext.getProperty("serviceModule")