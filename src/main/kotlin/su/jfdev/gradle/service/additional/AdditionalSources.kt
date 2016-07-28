package su.jfdev.gradle.service.additional

import org.gradle.api.tasks.*

enum class AdditionalSources(val repeatable: Boolean = false,
                             val alternative: String? = null,
                             val upper: Iterable<String> = emptyList(),
                             val downer: Iterable<String> = emptyList()) {
    api(
               alternative = "main",
               downer = listOf("main")
       ),
    spec(
                alternative = "test",
                downer = listOf("test"),
                upper = listOf("impl")
        ),
    impl(
                alternative = "impl",
                repeatable = true,
                upper = listOf("main")
        );

    operator fun get(sourceSets: SourceSetContainer) = AdditionalContainer.smart(sourceSets, !repeatable)

    companion object {
        operator fun get(name: String) = values().find {
            it.name.equals(name, ignoreCase = true)
        }
    }
}