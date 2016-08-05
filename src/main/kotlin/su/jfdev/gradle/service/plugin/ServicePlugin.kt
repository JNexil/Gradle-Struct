package su.jfdev.gradle.service.plugin

import org.gradle.api.*
import su.jfdev.gradle.service.describe.*
import su.jfdev.gradle.service.require.*

class ServicePlugin: Plugin<Project> {
    private lateinit var project: Project

    override fun apply(project: Project) {
        this.project = project
        project.plugins.apply("java")
        improveDescribe().improveImplementations()
        improveRequire()
    }

    private fun improveDescribe() = project.container(Pack::class.java){
        Pack[project, it]
    }.apply {
        maybeCreate("api") extend maybeCreate("main")
        project.extensions.add("describe", this)
    }

    private fun NamedDomainObjectContainer<Pack>.improveImplementations() = project.container(Pack::class.java){
        maybeCreate(it) depend maybeCreate("main") extend maybeCreate("test")
    }

    private fun improveRequire() = project.extensions.create("require", RequireExtension::class.java, project)
}