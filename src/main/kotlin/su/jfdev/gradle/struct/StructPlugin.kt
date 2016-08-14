package su.jfdev.gradle.struct

import org.gradle.api.*
import su.jfdev.gradle.struct.*
import su.jfdev.gradle.struct.describe.*
import su.jfdev.gradle.struct.require.*

class StructPlugin: Plugin<Project> {
    val plugins = listOf(
            RequirePlugin::class,
            DescribePlugin::class
                        )
    override fun apply(project: Project) = plugins.forEach {
        project.plugins.apply(it.java)
    }
}