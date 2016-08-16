package su.jfdev.gradle.struct.describe

import org.gradle.api.*
import su.jfdev.gradle.struct.util.*

class DescribePlugin: Plugin<Project> {
    private lateinit var project: Project

    override fun apply(project: Project) {
        this.project = project
        improveDescribe().improveImplementations()
    }

    private fun improveDescribe() = project.addContainer("describe") {
        Pack(project, it)
    }.apply {
        this["main"] depend this["api"]
        this["test"].includeResources = true
    }

    private fun NamedDomainObjectContainer<Pack>.improveImplementations() = project.addContainer("implementations") {
        this[it] depend this["main"] extend this["test"]
    }

    private operator fun NamedDomainObjectContainer<Pack>.get(name: String) = maybeCreate(name)


}