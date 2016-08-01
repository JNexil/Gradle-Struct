package su.jfdev.gradle.service.describe

data class Service(val api: Packs,
                   val spec: Packs,
                   val impl: Packs) {
    val packs: Map<String, Packs> = mapOf("api" to api,
                                              "spec" to spec,
                                              "impl" to impl)

    class Packs(val dummy: Pack, packs: Set<Pack>): Set<Pack> by packs
}