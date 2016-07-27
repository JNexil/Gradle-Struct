package su.jfdev.gradle.service.additional

import org.gradle.api.*
import su.jfdev.gradle.service.additional.AdditionalSources.*
import su.jfdev.gradle.service.util.*
import java.util.*

open class AdditionalDescriber(val project: Project) {
    val sourceSets = project.sourceSets
    private val additionalSources = EnumMap(AdditionalSources.values().associate {
        it to it[project]
    })

    operator fun get(additional: AdditionalSources): AdditionalContainer
            = additionalSources[additional]!!

    operator fun set(additional: AdditionalSources, alias: String) = get(additional).add(alias).apply {
        //TODO: setting sourceSets for extending
    }

    fun api(alias: String) = set(API, alias)
    fun spec(alias: String) = set(SPEC, alias)
    fun impl(vararg aliases: String) = aliases.forEach {
        set(IMPL, it)
    }
}