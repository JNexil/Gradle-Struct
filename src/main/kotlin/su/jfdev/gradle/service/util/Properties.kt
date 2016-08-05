package su.jfdev.gradle.service.util

import org.codehaus.groovy.runtime.InvokerHelper.*
import org.gradle.api.*
import org.gradle.api.tasks.*

val Project.sourceSets: SourceSetContainer get() = getProperty(this, "sourceSets") as SourceSetContainer