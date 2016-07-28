package su.jfdev.gradle.service.implementation

import com.google.common.collect.*
import org.gradle.api.tasks.*
import su.jfdev.gradle.service.*
import java.io.*

internal fun ServiceExtension.writeServices(services: Table<String, String, Iterable<String>>)
        = sourceSets.writeServices(services)

private fun SourceSetContainer.writeServices(services: Table<String, String, Iterable<String>>) {
    for ((name, map) in services.rowMap()) getByName(name).output.resourcesDir.writeServices(map)
}

private fun File.writeServices(services: Map<String, Iterable<String>>) {
    for ((key, value) in services) writeServices(key, value)
}

private fun File.writeServices(service: String,
                               implementations: Iterable<String>) = resolve("META-INF/services/$service").run {
    createNewFile()
    writeServices(implementations)
}

private fun File.writeServices(services: Iterable<String>) = writer().run {
    for (value in services) appendln(value)
}