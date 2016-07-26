package su.jfdev.gradle.service

import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.tasks.*
import java.util.*

//TODO: Full evaluate-time realisation
class SourceSets(val project: Project) {
    val extractors = project.extractors
    val sourceSets: SourceSetContainer = project.sourceSets
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
            for (sourceSet in impl.keys)
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
            project.optional()
        }

        private fun Configuration?.addEnabled(extractor: SourceSet?.() -> Configuration?) = this?.run {
            val toExtend = under + parent + child
            extend(extractor, *toExtend.toTypedArray())
        }

        private fun Configuration.extend(extract: SourceSet?.() -> Configuration?,
                                         vararg under: String?) = under.forEach {
            if(it != null) {
                val configuration = sourceSets.findByName(it).extract()
                configuration?.extendsFrom(this)
            }
        }

        private fun Project.optional() = scopes.forEach {
            val sourceName = this@SourceSetEx.name
            val config = makeConfig(it, sourceName)
            if (!enabled && whenDisabled != null) {
                val disabledConfig = makeConfig(it, whenDisabled)
                config.extendsFrom(disabledConfig)
            }
            if(dummy != null && dummy != sourceName) {
                val dummyConfig = makeConfig(it, dummy)
                config.extendsFrom(dummyConfig)
            }
        }

    }
}