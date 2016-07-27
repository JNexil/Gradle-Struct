package su.jfdev.gradle.service.util

import org.codehaus.groovy.runtime.*
import org.gradle.api.*
import org.gradle.api.tasks.*

private inline operator fun <reified T> Any.get(name: String): T = InvokerHelper.getProperty(this, name) as T
val Project.sourceSets: SourceSetContainer get() = get("sourceSets")
inline fun <reified T: Any> Project.extension(): T = extensions.getByType(T::class.java)