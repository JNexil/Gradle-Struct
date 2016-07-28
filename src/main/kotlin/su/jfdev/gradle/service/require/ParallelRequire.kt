package su.jfdev.gradle.service.require

import org.gradle.api.*
import su.jfdev.gradle.service.additional.*
import su.jfdev.gradle.service.additional.AdditionalSources.*
import su.jfdev.gradle.service.util.*

class ParallelRequire(val receiver: Project, val target: Project) {
    val receiverAdditional = AdditionalDescriber[receiver]
    val targetAdditional = AdditionalDescriber[target]

    fun service(vararg implementations: String) {
        sources("main")
        implementation(*implementations)
        for (value in values()) value.additional()
    }

    private fun AdditionalSources.additional() = when (this) {
        impl                   -> Unit
        !in targetAdditional   -> Unit
        !in receiverAdditional -> sources(name, alternative!!)
        else                   -> sources(name)
    }

    fun implementation(vararg implementations: String) = implementations.forEach { implementation ->
        sources(implementation, "impl")
    }

    fun sources(fromSet: String, toSet: String = fromSet, scope: String = "compile") {
        val configuration = makeConfigName(toSet, scope)
        val dependency = DependencyWithSources(target, fromSet, scope)
        receiver.dependencies.add(configuration, dependency)
        receiver.logger.debug("Add sources: $configuration $dependency from $fromSet to $toSet by $scope")
    }

    companion object {
        operator fun get(receiver: Project, name: String) = ParallelRequire(receiver, receiver.project(name))
    }
}

inline fun Project.require(target: String, block: ParallelRequire.() -> Unit) = ParallelRequire[this, target].run(block)