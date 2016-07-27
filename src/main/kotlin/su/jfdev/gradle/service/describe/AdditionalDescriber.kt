package su.jfdev.gradle.service.describe

import org.gradle.api.*
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
}