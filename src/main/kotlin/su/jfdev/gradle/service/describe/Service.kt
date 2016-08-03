package su.jfdev.gradle.service.describe

import su.jfdev.gradle.service.describe.ServiceBuilder.*
import java.util.*

class Service(val packs: Map<String, Packs>) {
    internal constructor(vararg packs: PackBuilder): this(packs.build())

    val api: Packs by packs
    val spec: Packs by packs
    val impl: Packs by packs
    val main: Packs by packs
    val test: Packs by packs


    data class Packs(val name: String, val dummy: Pack, val packs: Set<Pack>): Set<Pack> by packs

    companion object {
        private fun Array<out PackBuilder>.build() = HashMap<String, Packs>().let { map ->
            associateTo(map) { it.main to it.build(map) }
        }
    }
}