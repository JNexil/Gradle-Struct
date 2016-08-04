package su.jfdev.gradle.service.describe

import org.gradle.api.*
import su.jfdev.gradle.service.util.*
import java.util.*

class ServiceBuilder(private val project: Project, container: Iterable<String> = emptyList()) {
    private val api = UnlinkedPack("api")
    private val test = UnlinkedPack("test")
    private val main = UnlinkedPack("main") depend api
    private val other = container.map { UnlinkedPack(it) depend main extend test }

    init {
        val all = other + arrayOf(api, main, test)
        for (builder in all) builder.improveDependencies()
    }

    private fun UnlinkedPack.improveDependencies() {
        for (dependency in dependencies) pack depend dependency.pack
    }

    private inner class UnlinkedPack(name: String) {
        val pack = Pack[project, name]

        val dependencies: MutableSet<UnlinkedPack> = HashSet()

        infix fun extend(pack: UnlinkedPack) = apply {
            pack depend this
        }
        infix fun depend(pack: UnlinkedPack) = apply {
            dependencies += pack
        }
    }
}

