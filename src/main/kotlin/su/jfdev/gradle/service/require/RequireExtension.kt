package su.jfdev.gradle.service.require

import org.gradle.api.*
import su.jfdev.gradle.service.util.*

open class RequireExtension(val project: Project) {
    val from: RequireExtension = this
    fun from(target: String) = get(target)
    fun service(to: String, vararg implementations: String) = get(to).service(*implementations)

    operator fun get(from: String) = Require(project.module, project.project(from).module)
}