package su.jfdev.gradle.service.plugin

import org.gradle.api.*
import su.jfdev.gradle.service.describe.*
import su.jfdev.gradle.service.require.*
import su.jfdev.gradle.service.util.*

class ServicePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        Module(project){}
        project.ext.setProperty("service", ServiceExtension(project))
        project.extensions.create("require", RequireExtension::class.java, project)
    }

    class ServiceExtension(val project: Project) {
        fun call(configure: ServiceBuilder.() -> Unit) {
            Module(project, configure)
        }
    }
}

