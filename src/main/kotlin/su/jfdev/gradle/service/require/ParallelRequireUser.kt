package su.jfdev.gradle.service.require

import org.gradle.api.*
import su.jfdev.gradle.service.*
import su.jfdev.gradle.service.util.*

open class ParallelRequireUser(val ext: ServiceExtension) {
    val receiver: Project get() = ext.project
    fun services(vararg targets: String) = targets.forEach {
        service(it)
    }

    fun service(target: String) = receiver.require(target) {
        val main = ext.implementations.main
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
        operator fun get(project: Project): ParallelRequireUser = project.extension.require
    }
}