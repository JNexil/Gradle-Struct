package su.jfdev.gradle.service

import org.gradle.api.*
import kotlin.properties.*

fun Project.changeSourceSet(sources: String): ReadWriteProperty<Any?, Boolean> {
    val property = Delegates.observable(false) { property, old, new ->
        if (old != new) change(sources, new)
    }
    change(sources)
    return property
}

fun Project.change(sources: String, create: Boolean = true) {
    val sourceSet = sourceSets.findByName(sources)
    if (create) sourceSet ?: sourceSets.create(sources) else
        if (sourceSet != null) sourceSets.remove(sourceSet)
}