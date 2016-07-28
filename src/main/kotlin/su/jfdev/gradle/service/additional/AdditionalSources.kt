package su.jfdev.gradle.service.additional

import org.gradle.api.tasks.*

enum class AdditionalSources(val repeatable: Boolean = false,
                             val alternative: String? = null,
                             val upper: Map<String, String> = emptyMap(),
                             val downer: Map<String, String> = emptyMap()) {
    api(
               alternative = "main",
               downer = mapOf("compile" to "main")
       ),
    spec(
                alternative = "test",
                downer =  mapOf("compile" to "test")
        ),
    impl(
                alternative = "impl",
                repeatable = true,
                downer = mapOf("runtime" to "impl"),
                upper = mapOf("runtime" to "main")
        );

    operator fun get(sourceSets: SourceSetContainer) = AdditionalContainer.smart(sourceSets, !repeatable)

    companion object {
        operator fun get(name: String) = values().find {
            it.name.equals(name, ignoreCase = true)
        }
    }
}