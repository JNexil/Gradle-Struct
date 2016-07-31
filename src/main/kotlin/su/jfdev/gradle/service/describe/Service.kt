package su.jfdev.gradle.service.describe

data class Service(val api: Set<Pack>,
                   val spec: Set<Pack>,
                   val impl: Set<Pack>) {
    val packs: Map<String, Set<Pack>> = mapOf("api" to api,
                                              "spec" to spec,
                                              "impl" to impl)
}