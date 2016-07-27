package su.jfdev.gradle.service.require

import org.gradle.api.*

open class ParallelRequireUser(val receiver: Project) {

    fun services(vararg targets: String) = targets.forEach {
        service(it)
    }

    fun service(target: String) = receiver.require(target) {
        val main = targetServices.main
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
}