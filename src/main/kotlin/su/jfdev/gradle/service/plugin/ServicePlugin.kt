package su.jfdev.gradle.service.plugin

import groovy.lang.*
import org.gradle.api.*
import su.jfdev.gradle.service.describe.*
import su.jfdev.gradle.service.require.*
import su.jfdev.gradle.service.util.*

class ServicePlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply("java")
        project.ext["service"] = ServiceSetup(project)
        project.extensions.create("require", RequireExtension::class.java, project)
    }

    inner class ServiceSetup(val project: Project): Closure<Any>(Unit) {
        @JvmName("doCall")
        operator fun invoke(vararg implementations: String) = ServiceBuilder(project, implementations.toSet())
    }

}
