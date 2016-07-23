package su.jfdev.gradle.service

import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.tasks.*
import java.util.*

class SourceSets(val project: Project) {
    val extractors = project.extractors
    val sourceSets: SourceSetContainer get() = project.find("sourceSets") as SourceSetContainer
    val configurations: ConfigurationContainer get() = project.configurations

    init {
        project.extensions.getByType(ServiceExtension::class.java).run {
            val under = ArrayList<String>()
            SourceSetEx("api",
                        child = "main",
                        enabled = apiSources,
                        whenDisabled = "main")
            SourceSetEx("spec",
                        child = "test",
                        enabled = specSources,
                        addTo = under)
            for (sourceSet in services.sourceSets)
                SourceSetEx(name = sourceSet,
                            parent = "main",
                            under = under,
                            isImpl = true)
        }
    }

    inner class SourceSetEx(val name: String,
                            val parent: String? = null,
                            val child: String? = null,
                            val whenDisabled: String? = null,
                            val enabled: Boolean = true,
                            val isImpl: Boolean = false,
                            val under: List<String> = emptyList(),
                            addTo: MutableList<String>? = null) {
        init {
            addTo?.add(name)
            val sourceSet = if (enabled) sourceSets.maybeCreate(name) else null
            for (extract in extractors) {
                if (enabled) sourceSet.extract() addEnabled extract
                optional("compile", "runtime")
            }

        }

        private infix fun Configuration?.addEnabled(extractor: (SourceSet) -> Configuration?) = this?.run {
            val toExtend = under + parent + child
            extend(extractor, *toExtend.toTypedArray())
        }

        private fun Configuration.extend(extract: SourceSet.() -> Configuration?,
                                         vararg under: String?) = under.forEach {
            val name = it ?: return
            sourceSets.findByName(name)?.extract()?.extendsFrom(this)
        }

        private fun optional(vararg scopes: String) = scopes.forEach {
            val config = makeConfig(it)
            if (!enabled && whenDisabled != null) {
                val disabledConfig = configurations.findByName(whenDisabled) ?: return@forEach
                config.extendsFrom(disabledConfig)
            }
        }

        private fun makeConfig(scope: String): Configuration {
            val configName = makeConfigName(name, scope)
            return configurations.maybeCreate(configName)
        }

    }
}