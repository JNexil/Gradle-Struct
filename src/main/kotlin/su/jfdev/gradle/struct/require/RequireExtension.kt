package su.jfdev.gradle.struct.require

import groovy.lang.*
import org.gradle.api.*
import java.util.*

open class RequireExtension(val project: Project) {
    private val templates: MutableMap<String, Require> = HashMap()

    val required: Collection<Require> get() = templates.values

    val from: RequireExtension = this
    fun from(target: String, closure: Closure<*>) = from(target).doCall(closure)
    fun from(target: String) = get(target)
    fun template(from: String, vararg implementations: String) = get(from).template(*implementations)
    fun inherit(from: String, vararg implementations: String) = get(from).inherit(*implementations)

    @JvmName("getAt")
    operator fun get(from: String) = templates.getOrPut(from){
        Require(project, project.project(from))
    }
}