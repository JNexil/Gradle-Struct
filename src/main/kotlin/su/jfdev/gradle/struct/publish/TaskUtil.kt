package su.jfdev.gradle.struct.publish

import org.gradle.api.*
import org.gradle.api.tasks.bundling.*
import su.jfdev.gradle.struct.describe.*

val Pack.classesTask: Task get() = project.tasks.getByName(sourceSet.classesTaskName)

internal inline fun Pack.taskJar(suffix: String?, configure: Jar.() -> Unit): Jar = task(suffix.jarName) {
    baseName = linePath
    classifier = suffix
    extension = "jar"
    group = "build"
    configure()
    project.artifacts.add("archives", this)
}

private val String?.jarName: String get() = (this ?: "classes") + "Jar"

inline fun <reified T: Task> Pack.task(suffix: String, configure: T.() -> Unit): T {
    val name = sourceSet.getTaskName(null, suffix)
    return project.task(name, configure)
}

inline fun <reified T: Task> Project.task(name: String, configure: T.() -> Unit): T {
    val task = tasks.withType(T::class.java).findByName(name)
    return task ?: tasks.create(name, T::class.java).apply(configure)
}