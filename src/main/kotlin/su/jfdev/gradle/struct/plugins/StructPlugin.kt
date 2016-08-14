package su.jfdev.gradle.struct.plugins

import org.gradle.api.*
import su.jfdev.gradle.struct.*

class StructPlugin: Plugin<Project> {
    val plugins = listOf(
            RequirePlugin::class,
            DescribePlugin::class
                        )
    override fun apply(project: Project) = plugins.forEach {
        project.plugins.apply(it.java)
    }
}