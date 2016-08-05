package su.jfdev.gradle.struct.require

import groovy.lang.*
import org.gradle.api.*

open class RequireExtension(val project: Project) {
    val from: RequireExtension = this
    fun from(target: String, closure: Closure<*>) = from(target).doCall(closure)
    fun from(target: String) = get(target)
    fun template(to: String, vararg implementations: String) = get(to).template(*implementations)

    @JvmName("getAt")
    operator fun get(from: String) = Require(project, project.project(from))
}