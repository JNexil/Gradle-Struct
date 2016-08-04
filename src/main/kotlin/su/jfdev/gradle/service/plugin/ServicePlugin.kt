package su.jfdev.gradle.service.plugin

import groovy.lang.*
import org.gradle.api.*
import su.jfdev.gradle.service.describe.*
import su.jfdev.gradle.service.require.*
import su.jfdev.gradle.service.util.*

class ServicePlugin: Plugin<Project> {

    override fun apply(project: Project) = project.run {
        plugins.apply("java")
        improvePacks()
        extensions.create("require", RequireExtension::class.java, project)
        ext["service"] = ServiceSetup(project)
    }

    inner class ServiceSetup(val project: Project): Closure<Any>(Unit) {
        @JvmName("doCall")
        operator fun invoke(vararg implementations: String) = project.improvePacks(*implementations)
    }

    private fun Project.improvePacks(vararg implementations: String) = PackLinker(this, implementations.toSet())

}
