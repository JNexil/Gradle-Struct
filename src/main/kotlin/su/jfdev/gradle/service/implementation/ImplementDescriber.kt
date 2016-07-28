package su.jfdev.gradle.service.implementation

import groovy.lang.*
import org.gradle.api.*
import su.jfdev.gradle.service.*
import su.jfdev.gradle.service.util.*
import java.io.*

open class ImplementDescriber(val ext: ServiceExtension): GroovyObjectSupport() {
    var main: String? = null
    fun main(name: String) {
        main = name
    }

    fun add(map: Map<String, Map<String, String>>) = map.toList().forEach {
        add(it.first, it.second)
    }


    fun add(name: String, map: Map<String, String>)
            = map.toList().distinct().add(name)

    fun add(name: String, vararg arguments: Pair<String, String>)
            = arguments.distinct().add(name)

    private fun Iterable<Pair<String, String>>.add(name: String) {
        val resourcesDir = ext.sourceSets.getByName(name).output.resourcesDir
        val argumentGroups = groupBy { it.first }
        for ((key, value) in argumentGroups) resourcesDir.resolve("META-INF/services/$key").apply {
            createNewFile()
            writeServices(value)
        }
    }

    private fun File.writeServices(services: List<Pair<String, String>>) = writer().run {
        for ((key, value) in services) appendln(value)
    }

    companion object {
        operator fun get(project: Project): ImplementDescriber = project.extension.implementations
    }

    fun getAt(name: String) = AddMethod(name)
    inner class AddMethod(val name: String) {
        fun call(map: Map<String, String>) = map.toList().distinct().add(name)
    }
}
