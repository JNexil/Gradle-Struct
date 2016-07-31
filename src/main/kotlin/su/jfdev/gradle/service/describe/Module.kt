package su.jfdev.gradle.service.describe

import org.gradle.api.*
import org.gradle.api.tasks.*
import su.jfdev.gradle.service.util.*

class Module(project: Project, configure: ServiceBuilder.() -> Unit): Project by project {
    init {
        project.ext.setProperty("serviceModule", this)
    }

    val sources: SourceSetContainer = project.sourceSets

    val service: Service = ServiceBuilder(this).apply(configure).build()
}