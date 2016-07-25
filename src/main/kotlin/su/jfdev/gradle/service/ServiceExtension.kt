package su.jfdev.gradle.service

import javafx.collections.*
import javafx.collections.FXCollections.*
import org.codehaus.groovy.runtime.*
import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.artifacts.dsl.*
import org.gradle.api.tasks.*

open class ServiceExtension(val project: Project) {
    val sourceSets: SourceSetContainer by lazy {
        project.get<SourceSetContainer>("sourceSets")
    }
    val dependHandler: DependencyHandler get() = project.dependencies
    var apiSources = true
    var specSources = true
    var default: String? = null

    val services: MutableMap<String, Map<String, String>> = observableHashMap<String, Map<String, String>>().apply {
        addListener(MapChangeListener {
            val add = it.wasAdded()
            val remove = it.wasRemoved()
            when {
                remove && add -> Unit
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

    fun use(vararg names: String) = names.forEach { name ->
        val impl = project.project(name).extension.default ?: return require(name)
        require(name, impl)
    }

    fun require(nameToImplMap: Map<String, String>){
        for ((key, value) in nameToImplMap) {
            require(key, value)
        }
    }

    fun require(name: String, vararg implementations: String) {
        val project = project.project(name)
        if(project.extension.apiSources) {
            val target = if(apiSources) "apiCompile" else "compile"
            project.extractDependency("api", target)
        }
        project.extractDependency("main", "compile")
        for(impl in implementations) {
            val target = if(specSources) "specRuntime" else "testRuntime"
            project.extractDependency(impl, target)
        }
    }

    private val Project.extension: ServiceExtension
        get() = extensions.findByType(ServiceExtension::class.java)

    private fun Project.extractDependency(source: String, conf: String){
        dependHandler.add(conf, extractProjectOut(conf))
        dependHandler.add(conf, extractSourceOut(source))
    }

    private fun Project.extractProjectOut(conf: String): Dependency {
        val arguments = mapOf("path" to path, "configuration" to conf)
        return dependHandler.project(arguments)
    }

    private fun Project.extractSourceOut(source: String): SourceSetOutput {
        val sources = InvokerHelper.getProperty(this, "sourceSets") as SourceSetContainer
        return sources.findByName(source).output
    }
}