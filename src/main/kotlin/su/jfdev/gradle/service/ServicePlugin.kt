package su.jfdev.gradle.service

import org.gradle.api.*
import su.jfdev.gradle.service.util.*

class ServicePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            addConfigurationDummies("api", "spec", "impl")
            extensions.create("services", ServiceExtension::class.java, project)
        }
    }

    fun Project.addConfigurationDummies(vararg dummies: String) = dummies.forEach { dummy ->
        for (scope in scopes) makeConfig(scope, dummy)
    }
}