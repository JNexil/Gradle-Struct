package su.jfdev.gradle.service.describe

import su.jfdev.gradle.service.util.*
import java.util.*

class ServiceBuilder(private val module: Module) {
    private val main = dummy("main")
    private val test = dummy("test")

    private fun dummy(name: String) = PackBuilder(name).history.single()

    val api = centralized("api") {
        extend(main)
    }

    val spec = centralized("spec") {
        depend(main)
        extend(test)
    }

    val impl = centralized("impl") {
        depend(main)
        extend(test)
    }

    private fun centralized(name: String, transformer: Pack.() -> Unit) = PackBuilder(name) {
        if (dummy) Unit
        else depend(it.dummy)

        transformer()
    }

    fun build() = Service(api = api.build(),
                          spec = spec.build(),
                          impl = impl.build())

    inner class PackBuilder(val mainName: String, val transformer: Pack.(PackBuilder) -> Unit = {}) {

        private val historySet: MutableSet<Pack> = HashSet()
        val history: Set<Pack> = historySet
        val dummy = pack(null)

        @JvmName("call") @JvmOverloads
        operator fun invoke(name: String = mainName) = pack(name)

        fun pack(name: String?) = Pack(module = module,
                                       name = name ?: mainName,
                                       dummy = name == null)
                .apply {
                    transformer(this, this@PackBuilder)
                    historySet += this
                }

        fun build() = historySet
    }
}

