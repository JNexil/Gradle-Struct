package su.jfdev.gradle.service.additional

import org.gradle.api.*
import su.jfdev.gradle.service.*
import su.jfdev.gradle.service.additional.AdditionalSources.*
import su.jfdev.gradle.service.require.*
import su.jfdev.gradle.service.util.*
import java.util.*

open class AdditionalDescriber(val ext: ServiceExtension) {
    private val additionalSources = EnumMap(AdditionalSources.values().associate {
        it to it[ext.sourceSets]
    })

    operator fun contains(additional: AdditionalSources) = !get(additional).isEmpty

    operator fun get(additional: AdditionalSources): AdditionalContainer
            = additionalSources[additional]!!

    operator fun set(additional: AdditionalSources, alias: String) = get(additional).add(alias).apply {
        ext.project.require(ext.project.path) {
            for (upper in additional.upper)
                for (fromSet in replace(upper))
                    sources(fromSet, alias)

            for (downer in additional.downer)
                for (toSet in replace(downer))
                    sources(alias, toSet)
        }
    }

    private fun replace(name: String): Iterable<String> {
        val sources = AdditionalSources[name] ?: return listOf(name)
        val container = get(sources)
        return when {
            container.isEmpty -> emptyList()
            else              -> container.map { it.name }
        }
    }

    @JvmOverloads fun api(alias: String = "api") = set(api, alias)

    @JvmOverloads fun spec(alias: String = "spec") = set(spec, alias)

    fun impl(map: Map<String, Map<String, Iterable<String>>>) {
        ext.implementations.add(map)
        for ((key, value) in map) impl(key)
    }
    fun impl() = impl("impl")

    fun impl(vararg aliases: String) = aliases.forEach {
        set(impl, it)
    }

    companion object {
        operator fun get(project: Project): AdditionalDescriber = project.extension.describe
    }
}