package su.jfdev.gradle.service.describe

enum class Scope(val scope: String) {
    RUNTIME("runtime"),
    COMPILE("compile");

    @JvmName("getAt")
    operator fun get(name: String) = when (name) {
        "main" -> scope
        else   -> name.decapitalize() + scope.capitalize()
    }
}