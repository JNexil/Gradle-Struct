package su.jfdev.gradle.service.plugin

import groovy.lang.*
import org.gradle.api.*
import su.jfdev.gradle.service.describe.*
import su.jfdev.gradle.service.require.*
import su.jfdev.gradle.service.util.*

class ServicePlugin: Plugin<Project> {

    override fun apply(project: Project) {
        project.plugins.apply("java")
        Module(project){}
        project.ext["service"] = closure<Closure<*>, Module> {
            Module(project){
                val closure = it.clone() as Closure<*>
                closure.delegate = this
                closure.call()
            }
        }
        project.extensions.create("require", RequireExtension::class.java, project)
    }

}
