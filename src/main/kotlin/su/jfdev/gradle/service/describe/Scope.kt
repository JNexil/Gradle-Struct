package su.jfdev.gradle.service.describe

enum class Scope(val scope: String) {
    RUNTIME("runtime"),
    COMPILE("compile");

    @JvmName("getAt")
    operator fun get(name: String) = name.decapitalize() + scope.capitalize()

    companion object {
        operator fun get(name: String): Scope {
            val scope = values().firstOrNull { it.name.equals(name, ignoreCase = true) }
            return requireNotNull(scope) { "Scope \"$name\" not found" }
        }
    }
}