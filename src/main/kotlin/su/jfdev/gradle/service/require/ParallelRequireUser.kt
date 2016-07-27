package su.jfdev.gradle.service.require

import org.gradle.api.*
import su.jfdev.gradle.service.implementation.*
import su.jfdev.gradle.service.util.*

open class ParallelRequireUser(val receiver: Project) {
    val describer: ImplementDescriber by receiver.ext()

    fun services(vararg targets: String) = targets.forEach {
        service(it)
    }

    fun service(target: String) = receiver.require(target) {
        val main = describer.main
        when (main) {
            null -> service()
            else -> service(main)
        }
    }


    fun service(target: String, vararg implementations: String) = receiver.require(target) {
        service(*implementations)
    }

    @JvmOverloads fun sources(target: String, fromSet: String, toSet: String = fromSet) = receiver.require(target) {
        sources(fromSet, toSet)
    }

    companion object {
        operator fun get(project: Project): ParallelRequireUser = project.extension()
    }
}