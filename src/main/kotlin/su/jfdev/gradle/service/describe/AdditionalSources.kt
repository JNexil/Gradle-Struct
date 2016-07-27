package su.jfdev.gradle.service.describe

import org.gradle.api.*
import su.jfdev.gradle.service.util.*

enum class AdditionalSources(method: String? = null , val repeatable: Boolean = false) {
    API("api"),
    SPEC(),
    IMPL(repeatable = true);

    val method: String = method ?: name.toLowerCase()

    operator fun get(project: Project) = AdditionalContainer.smart(project.sourceSets, !repeatable)

    companion object {
        operator fun get(name: String) = values().find {
            it.name.equals(name, ignoreCase = true)
        }
    }
}