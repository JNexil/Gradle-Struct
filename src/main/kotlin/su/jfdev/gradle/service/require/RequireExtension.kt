package su.jfdev.gradle.service.require

import org.gradle.api.*
import su.jfdev.gradle.service.util.*

open class RequireExtension(val project: Project) {
    val to: RequireExtension = this
    fun to(to: String) = get(to)
    fun service(to: String, vararg implementations: String) = get(to).service(*implementations)

    operator fun get(to: String) = Require(project.module, project.project(to).module)
}