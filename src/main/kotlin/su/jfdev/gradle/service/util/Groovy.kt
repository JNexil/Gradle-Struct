package su.jfdev.gradle.service.util

import org.codehaus.groovy.runtime.*
import org.gradle.api.*
import org.gradle.api.tasks.*
import su.jfdev.gradle.service.*

private inline operator fun <reified T> Any.get(name: String): T = InvokerHelper.getProperty(this, name) as T
val Project.sourceSets: SourceSetContainer get() = get("sourceSets")

val Project.extension: ServiceExtension get() = extensions.getByType(ServiceExtension::class.java)