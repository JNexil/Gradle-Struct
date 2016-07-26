package su.jfdev.gradle.service

import org.codehaus.groovy.runtime.*
import org.gradle.api.*
import org.gradle.api.tasks.*

inline operator fun <reified T> Any.get(name: String): T = InvokerHelper.getProperty(this, name) as T
val Project.sourceSets: SourceSetContainer get() = get("sourceSets")