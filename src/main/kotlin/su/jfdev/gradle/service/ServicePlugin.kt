package su.jfdev.gradle.service

import org.gradle.api.*

class ServicePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            extensions.create("services", ServiceExtension::class.java, project)
            addConfigurationDummies("api", "spec", "impl")
            afterEvaluate {
                SourceSets(project)
            }
        }
    }

    fun Project.addConfigurationDummies(vararg dummies: String) = dummies.forEach { dummy ->
        for (scope in scopes) makeConfig(scope, dummy)
    }
}