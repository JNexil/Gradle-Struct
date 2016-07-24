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
                        whenDisabled = "main",
                        enabled = apiSources)
            SourceSetEx("spec",
                        child = "test",
                        whenDisabled = "test",
                        enabled = specSources)
            for (sourceSet in services.sourceSets)
                SourceSetEx(name = sourceSet,
                            parent = "main",
                            under = under,
                            dummy = "impl")
        }
    }

    inner class SourceSetEx(val name: String,
                            val parent: String? = null,
                            val child: String? = null,
                            val whenDisabled: String? = null,
                            val enabled: Boolean = true,
                            val under: List<String> = emptyList(),
                            val dummy: String? = null,
                            addTo: MutableList<String>? = null) {
        init {
            addTo?.add(name)
            val sourceSet = if (enabled) sourceSets.maybeCreate(name) else null
            for (extract in extractors) if (enabled) sourceSet.extract().addEnabled(extract)
            optional("compile", "runtime")
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
            val config = makeConfig(it, name)
            if (!enabled && whenDisabled != null) {
                val disabledConfig = makeConfig(it, whenDisabled)
                config.extendsFrom(disabledConfig)
            }
            if(dummy != null) {
                val dummyConfig = makeConfig(it, dummy)
                config.extendsFrom(dummyConfig)
            }
        }

        private fun makeConfig(scope: String, name: String): Configuration {
            val configName = makeConfigName(name, scope)
            return configurations.maybeCreate(configName)
        }

    }
}