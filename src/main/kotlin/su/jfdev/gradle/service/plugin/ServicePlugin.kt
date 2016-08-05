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
    }.add("describe")

    private fun NamedDomainObjectContainer<Pack>.improveImplementations() = project.container(Pack::class.java){
        maybeCreate(it) depend maybeCreate("main") extend maybeCreate("test")
    }.add("implementations")

    private fun <T> T.add(name: String) = apply {
        project.extensions.add(name, this)
    }

    private fun improveRequire() = project.extensions.create("require", RequireExtension::class.java, project)
}