package su.jfdev.gradle.service.util

import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.internal.artifacts.*
import org.gradle.api.tasks.*
import su.jfdev.gradle.service.describe.*
import java.util.*

class HierarchyChecker(val project: Project, from: String, to: String, scope: Scope) {
    val toConf = scope.configuration(to)!!
    val fromConf = scope.configuration(from)!!
    val fromSource: SourceSet? = project.sourceSets.findByName(from)

    private fun Scope.configuration(from: String): Configuration? = project.configurations.findByName(this[from])

    val result: Boolean? = toConf.isDepend

    private val Configuration.isDepend: Boolean? get() = isDependSource or fromConf.isDependConfiguration

    private infix fun Boolean?.or(other: Boolean): Boolean? = when {
        this == null -> null
        else         -> this || other
    }

    private val Configuration.isDependSource: Boolean? get() = when (fromSource) {
        null    -> false
        in this -> true
        else    -> null
    }

    private operator fun Configuration.contains(from: SourceSet) = dependencies.any {
        it is ResolvableDependency && from in it.notations
    }

    private val ResolvableDependency.notations: List<*> get() = ArrayList<Any?>().apply {
        resolve(context)
    }

    private val MutableList<Any?>.context: DependencyResolveContext get() = object: DependencyResolveContext {
        override fun add(dependency: Any?) {
            this@context.add(dependency)
        }

        override fun isTransitive() = true
    }


    private val Configuration.isDependConfiguration: Boolean get()  = extendsFrom.any {
        it == toConf || it.isDependConfiguration
    }

    companion object {
        @JvmStatic fun depend(project: Project): (Scope, String, String) -> Boolean = { scope, from, to ->
            HierarchyChecker(project, from, to, scope).result ?: error("$project | $from || $to -> fail")
        }

        @JvmStatic fun nonDepend(project: Project): (Scope, String, String) -> Boolean = { scope, from, to ->
            HierarchyChecker(project, from, to, scope).result != true
        }
    }
}
