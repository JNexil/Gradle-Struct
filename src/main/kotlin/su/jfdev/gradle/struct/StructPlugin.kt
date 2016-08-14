package su.jfdev.gradle.struct

import org.gradle.api.*
import org.gradle.api.plugins.*
import su.jfdev.gradle.struct.describe.*
import su.jfdev.gradle.struct.publish.*
import su.jfdev.gradle.struct.require.*

class StructPlugin: Plugin<Project> {
    private val plugins = listOf(
            JavaPlugin::class,
            RequirePlugin::class,
            DescribePlugin::class,
            PublishPlugin::class
                        )
    override fun apply(project: Project) = plugins.forEach {
        project.plugins.apply(it.java)
    }
}