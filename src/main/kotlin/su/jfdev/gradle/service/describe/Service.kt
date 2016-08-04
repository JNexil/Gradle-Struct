package su.jfdev.gradle.service.describe

import su.jfdev.gradle.service.describe.ServiceBuilder.*
import java.util.*

class Service(val packs: Map<String, Pack>) {
    internal constructor(vararg packs: PackBuilder): this(packs.build())

    val api: Pack by packs
    val spec: Pack by packs
    val impl: Pack by packs
    val main: Pack by packs
    val test: Pack by packs

    companion object {
        private fun Array<out PackBuilder>.build() = HashMap<String, Pack>().let { map ->
            associateTo(map) { it.main to it.build(map) }
        }
    }
}