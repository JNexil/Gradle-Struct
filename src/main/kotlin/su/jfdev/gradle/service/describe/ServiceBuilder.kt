package su.jfdev.gradle.service.describe

import groovy.lang.*
import su.jfdev.gradle.service.describe.Service.*
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
        val registry: MutableSet<String> = HashSet()
        val dependencies: MutableSet<PackBuilder> = HashSet()
        internal val users: MutableSet<PackBuilder> = HashSet()

        @JvmOverloads @JvmName("doCall")
        operator fun invoke(name: String = main) = registry.add(name)

        infix fun depend(pack: PackBuilder) = apply {
            pack.users += this
            for (user in users) users += user

            dependencies += pack
            for (child in pack.dependencies) dependencies += child
        }

        fun build(map: Map<String, Packs>) = Packs(main, dummy = buildDummy(), packs = buildPacks()).apply {
            if (dummy.isDummy) forEach {
                it depend dummy
            }
            for (dependency in dependencies) {
                val dependencyPack = map[dependency.main] ?: error("${dependency.main} should be registered before $main")
                dummy depend dependencyPack
            }
        }

        private fun buildDummy() = Pack(module, main, main in registry)

        private fun buildPacks(): Set<Pack> = HashSet<Pack>().let {
            registry.mapTo(it) { Pack(module, it, real = true) }
        }
    }
}

