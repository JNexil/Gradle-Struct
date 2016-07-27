package su.jfdev.gradle.service.additional

import org.gradle.api.*
import su.jfdev.gradle.service.util.*

enum class AdditionalSources(val repeatable: Boolean = false) {
    API(),
    SPEC(),
    IMPL(repeatable = true);

    operator fun get(project: Project) = AdditionalContainer.smart(project.sourceSets, !repeatable)

    companion object {
        operator fun get(name: String) = values().find {
            it.name.equals(name, ignoreCase = true)
        }
    }
}