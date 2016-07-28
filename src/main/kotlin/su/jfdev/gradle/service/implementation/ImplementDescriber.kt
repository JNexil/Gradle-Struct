package su.jfdev.gradle.service.implementation

import com.google.common.collect.*
import org.gradle.api.*
import su.jfdev.gradle.service.*
import su.jfdev.gradle.service.util.*

open class ImplementDescriber(val ext: ServiceExtension) {
    private val implementations: Table<String, String, Iterable<String>> = HashBasedTable.create()

    init {
        ext.project.afterEvaluate {
            ext.writeServices(implementations)
        }
    }

    var main: String? = null
    fun main(name: String) {
        main = name
    }

    fun add(map: Map<String, Map<String, Iterable<String>>>) {
        for ((source, values) in map.entries)
            for ((service, implementation) in values)
                implementations.put(source, service, implementation)
    }


    companion object {
        operator fun get(project: Project): ImplementDescriber = project.extension.implementations
    }
}
