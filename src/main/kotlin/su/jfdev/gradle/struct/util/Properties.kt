package su.jfdev.gradle.struct.util

import org.codehaus.groovy.runtime.InvokerHelper.*
import org.gradle.api.*
import org.gradle.api.tasks.*

val Project.sourceSets: SourceSetContainer get() = getProperty(this, "sourceSets") as SourceSetContainer