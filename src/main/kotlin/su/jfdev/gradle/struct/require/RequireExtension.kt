package su.jfdev.gradle.struct.require

import groovy.lang.*
import org.gradle.api.*
import su.jfdev.gradle.struct.require.Requirement.*

open class RequireExtension(val project: Project) {
    val from: RequireExtension = this
    fun from(target: String, closure: Closure<*>) = from(target) doCall closure
    fun from(target: String) = get(target)
    fun template(from: String, vararg implementations: String) = get(from).template(*implementations)
    fun inherit(from: String, vararg implementations: String) = get(from).inherit(*implementations)

    @JvmName("getAt")
    operator fun get(from: String) = project require Module(project.project(from))

    fun external(pattern: String, closure: Closure<*>) = external(pattern) doCall closure
    fun external(closure: Closure<*>) = external() doCall closure
    @JvmOverloads fun external(pattern: String = "%s") = project require External(pattern)


    infix fun Project.require(requirement: Requirement) = Require(this, requirement)
}