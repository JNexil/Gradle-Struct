package su.jfdev.gradle.service.describe

import groovy.lang.*
import su.jfdev.gradle.service.util.*
import java.util.*

class ServiceBuilder(private val module: Module): GroovyObjectSupport() {
    val api = PackBuilder("api")
    val main = PackBuilder("main") depend api
    val impl = PackBuilder("impl") depend main
    val spec = PackBuilder("spec") depend main
    val test = PackBuilder("test") depend impl depend spec

    fun build() = Service(api, main, impl, spec, test)

    inner class PackBuilder(val main: String): Closure<Pack>(Unit, Unit) {
        val dependencies: MutableSet<PackBuilder> = HashSet()
        internal val users: MutableSet<PackBuilder> = HashSet()

        infix fun depend(pack: PackBuilder) = apply {
            pack.users += this
            for (user in users) users += user

            dependencies += pack
            for (child in pack.dependencies) dependencies += child
        }

        internal fun build(map: Map<String, Pack>) = Pack(module, main).apply {
            for (dependency in dependencies) {
                val pack = map[dependency.main] ?: error("${dependency.main} should be registered before $main")
                this depend pack
            }
        }
    }
}

