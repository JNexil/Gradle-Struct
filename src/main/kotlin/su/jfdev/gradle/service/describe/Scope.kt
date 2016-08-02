package su.jfdev.gradle.service.describe

enum class Scope(val scope: String) {
    RUNTIME("runtime"),
    COMPILE("compile");

    @JvmName("getAt")
    operator fun get(name: String) = name.decapitalize() + scope.capitalize()
}