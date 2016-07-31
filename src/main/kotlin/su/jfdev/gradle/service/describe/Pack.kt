package su.jfdev.gradle.service.describe

import org.gradle.api.artifacts.*
import org.gradle.api.tasks.*

class Pack(val module: Module, val name: String) {

    constructor(module: Module, name: String, dummy: Boolean): this(module, name) {
        this.dummy = dummy
    }

    var dummy: Boolean get() = sourceSet != null
        set(add) = module.sources.run {
            val source = sourceSet
            when (add) {
                (source != null) == add -> Unit
                true                    -> maybeCreate(name)
                false                   -> remove(source)
            }
        }

    val sourceSet: SourceSet? get() = module.sources.findByName(name)
    val configurations: Map<Scope, Configuration> = Scope.values().associate {
        it to it.configuration
    }

    private val Scope.configuration: Configuration get() = module.configurations.maybeCreate(this[name])

    operator fun get(scope: Scope): Configuration = configurations[scope]!!
}