package su.jfdev.gradle.service.describe

import groovy.lang.*
import su.jfdev.gradle.service.describe.Service.*
import su.jfdev.gradle.service.util.*
import java.util.*

class ServiceBuilder(private val module: Module): GroovyObjectSupport() {
    private val main = dummy("main")
    private val test = dummy("test")

    private fun dummy(name: String) = PackCollector(name).dummy

    val api = PackCollector("api") {
        extend(main)
    }

    val spec = PackCollector("spec") {
        depend(main)
        extend(test)
    }

    val impl = PackCollector("impl") {
        depend(main)
        extend(test)
    }

    fun build() = Service(api = api.packs,
                          spec = spec.packs,
                          impl = impl.packs)

    inner class PackCollector(val mainName: String,
                              val transformer: Pack.() -> Unit = {}): Closure<Pack>(this, this) {

        private val history: MutableSet<Pack> = HashSet()

        val packs: Packs = Packs(dummy = make(null), packs = history)

        val dummy: Pack get() = packs.dummy

        @JvmOverloads @JvmName("doCall")
        operator fun invoke(name: String = mainName) = when (name) {
            mainName -> makeMain()
            else     -> makePack(name)
        }.addToHistory()


        private fun makeMain() = dummy.apply {
            isDummy = false
        }

        private fun makePack(name: String?) = make(name).apply {
            depend(dummy)
        }

        private fun make(name: String?) = Pack(module = module,
                                               name = name ?: mainName,
                                               isDummy = name == null).apply(transformer)

        private fun Pack.addToHistory() = apply {
            history += this
        }
    }
}

