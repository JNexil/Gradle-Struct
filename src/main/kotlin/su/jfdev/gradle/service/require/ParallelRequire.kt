package su.jfdev.gradle.service.require

import org.gradle.api.*
import su.jfdev.gradle.service.*
import su.jfdev.gradle.service.util.*

class ParallelRequire(val receiver: Project, val target: Project) {
    val services: ServiceExtension = receiver.extensions.findByType(ServiceExtension::class.java)
    val targetServices: ServiceExtension = target.extensions.findByType(ServiceExtension::class.java)

    fun service(vararg implementations: String) {
        sources("main")
        sourcesWhen("api", "main") { apiSources }
        sourcesWhen("spec", "test") { specSources }
        implement(implementations)
    }

    private inline fun sourcesWhen(sources: String, alternative: String, alt: ServiceExtension.() -> Boolean) = when {
        targetServices.alt() -> Unit
        services.alt()       -> sources(sources, alternative)
        else                 -> sources(sources)
    }

    private fun implement(implementations: Array<out String>) = implementations.forEach { implementation ->
        sources(implementation, "impl")
    }

    fun sources(fromSet: String, toSet: String = fromSet) {
        val dependency = pack(fromSet)
        val configuration = makeConfigName(toSet, scope)
        receiver.dependencies.add(configuration, dependency)
    }

    private fun pack(fromSet: String) = DependencyWithSources(target, fromSet)

    companion object {
        const val scope = "compile"
        operator fun get(receiver: Project, name: String) = ParallelRequire(receiver, receiver.project(name))
    }
}

inline fun Project.require(target: String, block: ParallelRequire.() -> Unit) = ParallelRequire[this, target].run(block)