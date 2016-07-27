package su.jfdev.gradle.service.additional

import org.gradle.api.*
import org.gradle.api.tasks.*
import su.jfdev.gradle.service.additional.AdditionalSources.*
import su.jfdev.gradle.service.util.*
import java.util.*

open class AdditionalDescriber(val sourceSets: SourceSetContainer) {
    private val additionalSources = EnumMap(AdditionalSources.values().associate {
        it to it[sourceSets]
    })

    operator fun contains(additional: AdditionalSources) = !get(additional).isEmpty

    operator fun get(additional: AdditionalSources): AdditionalContainer
            = additionalSources[additional]!!

    operator fun set(additional: AdditionalSources, alias: String) = get(additional).add(alias).apply {
        //TODO: setting sourceSets for extending
    }

    @JvmOverloads fun api(alias: String = "api") = set(api, alias)
    @JvmOverloads fun spec(alias: String = "spec") = set(spec, alias)

    fun impl() = impl("impl")
    fun impl(vararg aliases: String) = aliases.forEach {
        set(impl, it)
    }

    companion object {
        operator fun get(project: Project): AdditionalDescriber = project.extension()
    }
}