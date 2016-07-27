package su.jfdev.gradle.service.implementation

import groovy.lang.*
import org.gradle.api.*
import org.gradle.api.tasks.*
import su.jfdev.gradle.service.util.*
import java.io.*
import kotlin.collections.Map.*

open class ImplementDescriber(val sourceSets: SourceSetContainer): GroovyObjectSupport() {
    var main: String? = null
    fun main(name: String) {
        main = name
    }

    fun add(name: String, vararg arguments: Pair<String, String>)
            = arguments.distinct().add(name)

    override fun invokeMethod(name: String, args: Any?): Any? {
        try {
            val argumentArray = args as Array<*>
            val argumentMap = argumentArray[0] as Map<*, *>
            argumentMap
                    .map { toStrings(it) }
                    .distinct().add(name)
            return null
        } catch (e: Exception) {
        }
        return super.invokeMethod(name, args)
    }

    private fun toStrings(it: Entry<*, Any?>) = it.key.toString() to it.value.toString()

    private fun Iterable<Pair<String, String>>.add(name: String) {
        val resourcesDir = sourceSets.getByName(name).output.resourcesDir
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
        operator fun get(project: Project): ImplementDescriber = project.extension()
    }

    @JvmName("getAt") fun getMethod(name: String) = AddMethod(name)
    inner class AddMethod(val name: String) {
        fun call(map: Map<String, String>) = map.toList().distinct().add(name)
    }
}
