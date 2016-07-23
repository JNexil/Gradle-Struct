package su.jfdev.gradle.service

import org.gradle.api.*

class ServicePlugin: Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            extensions.create("services", ServiceExtension::class.java, project)
            afterEvaluate {
                SourceSets(project)
            }
        }
    }
}