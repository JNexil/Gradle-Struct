package su.jfdev.gradle.service

import javafx.collections.*
import javafx.collections.FXCollections.*
import org.gradle.api.*
import org.gradle.api.artifacts.dsl.*
import org.gradle.api.tasks.*

open class ServiceExtension(val project: Project) {
    val sourceSets: SourceSetContainer by lazy {
        project.sourceSets
    }
    val dependHandler: DependencyHandler get() = project.dependencies

    var apiSources by project.changeSourceSet("api")
    val apiScope: String get() = if (apiSources) "apiCompile" else "compile"

    var specSources by project.changeSourceSet("spec")
    val specScope: String get() = if (specSources) "specRuntime" else "testRuntime"

    var main: String? = null

    val impl: MutableMap<String, Map<String, String>> = observableHashMap<String, Map<String, String>>().apply {
        addListener(MapChangeListener {
            val add = it.wasAdded()
            val remove = it.wasRemoved()
            when {
                remove == add -> Unit
                remove        -> {
                    val serviceSet = sourceSets.findByName(it.key) ?: return@MapChangeListener
                    sourceSets.remove(serviceSet)
                }
                add           -> {
                    sourceSets.maybeCreate(it.key)
                }
            }
        })
    }

    fun putAt(implementation: String, services: Map<String, String>) = impl.put(implementation, services)

    fun defRequire(vararg names: String): Unit = names.forEach { name ->
        val impl = project.project(name).extension.main ?: return require(name)
        require(name, impl)
    }

    fun require(nameToImplMap: Map<String, String>){
        for ((key, value) in nameToImplMap) {
            require(key, value)
        }
    }

    fun require(name: String, vararg implementations: String) = project.project(name).run {
        extractDependency("main", "compile")
        if (extension.apiSources) extractDependency("api", apiScope)
        for (impl in implementations) extractDependency(impl, specScope)
    }

    private val Project.extension: ServiceExtension get() = extensions.findByType(ServiceExtension::class.java)
    private fun Project.extractDependency(source: String, conf: String) = DependencyWithSources(this, source, conf)
}