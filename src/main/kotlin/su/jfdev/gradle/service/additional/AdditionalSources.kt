package su.jfdev.gradle.service.additional

import org.gradle.api.tasks.*

enum class AdditionalSources(val repeatable: Boolean = false,
                             val alternative: String? = null) {
    api(alternative = "main"),
    spec(alternative = "test"),
    impl(repeatable = true);

    operator fun get(sourceSets: SourceSetContainer) = AdditionalContainer.smart(sourceSets, !repeatable)

    companion object {
        operator fun get(name: String) = values().find {
            it.name.equals(name, ignoreCase = true)
        }
    }
}