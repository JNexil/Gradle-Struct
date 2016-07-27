package su.jfdev.gradle.service.additional

import org.gradle.api.*
import su.jfdev.gradle.service.additional.AdditionalSources.*
import java.util.*

open class AdditionalDescriber(val project: Project) {
    private val additionalSources = EnumMap(AdditionalSources.values().associate {
        it to it[project]
    })

    operator fun contains(additional: AdditionalSources) = !get(additional).isEmpty

    operator fun get(additional: AdditionalSources): AdditionalContainer
            = additionalSources[additional]!!

    operator fun set(additional: AdditionalSources, alias: String) = get(additional).add(alias).apply {
        //TODO: setting sourceSets for extending
    }

    fun api(alias: String) = set(api, alias)
    fun spec(alias: String) = set(spec, alias)
    fun impl(vararg aliases: String) = aliases.forEach {
        set(impl, it)
    }

    companion object {
        operator fun get(project: Project): AdditionalDescriber
                = project.extensions.getByType(AdditionalDescriber::class.java)
    }
}