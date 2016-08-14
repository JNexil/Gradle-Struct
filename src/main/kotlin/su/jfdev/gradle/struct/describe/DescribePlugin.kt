package su.jfdev.gradle.struct.describe

import org.gradle.api.*

class DescribePlugin: Plugin<Project> {
    private lateinit var project: Project

    override fun apply(project: Project) {
        this.project = project
        improveDescribe().improveImplementations()
    }

    private fun improveDescribe() = container("describe") {
        Pack(project, it)
    }.apply {
        this["main"] depend this["api"]
        this["test"].includeResources = true
    }

    private fun NamedDomainObjectContainer<Pack>.improveImplementations() = container("implementations") {
        this[it] depend this["main"] extend this["test"]
    }

    private operator fun NamedDomainObjectContainer<Pack>.get(name: String) = maybeCreate(name)

    private fun container(name: String, factory: (String) -> Pack) = project
            .container(Pack::class.java, NamedDomainObjectFactory(factory))
            .apply { project.extensions.add(name, this) }
}