package su.jfdev.gradle.struct.plugins

import org.gradle.api.*
import su.jfdev.gradle.struct.require.*

class RequirePlugin: Plugin<Project> {
    override fun apply(project: Project) = project.improveRequire()


    private fun Project.improveRequire() {
        project.extensions.create("require", RequireExtension::class.java, project)
    }
}