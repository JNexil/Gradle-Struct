package su.jfdev.gradle.service.require

import groovy.lang.*
import org.gradle.api.*
import su.jfdev.gradle.service.util.*

open class RequireExtension(val project: Project) {
    val from: RequireExtension = this
    fun from(target: String, closure: Closure<*>) = from(target).doCall(closure)
    fun from(target: String) = get(target)
    fun service(to: String, vararg implementations: String) = get(to).service(*implementations)

    @JvmName("getAt")
    operator fun get(from: String) = Require(project, project.project(from))
}