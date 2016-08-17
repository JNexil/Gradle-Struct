package su.jfdev.gradle.struct.publish

import org.gradle.api.tasks.javadoc.*
import org.jetbrains.dokka.gradle.*
import su.jfdev.gradle.struct.describe.*

val needDokka = try {
    Class.forName("org.jetbrains.dokka.gradle.DokkaTask")
    true
} catch (e: Throwable) {
    false
}

inline fun Pack.taskJavadoc(prefix: String, configure: Javadoc.() -> Unit): Javadoc = task(prefix + "Javadoc") {
    source(sourceSet.allJava)
    configure()
}

inline fun Pack.taskDokka(prefix: String, configure: DokkaTask.() -> Unit): DokkaTask = task(prefix + "Doc") {
    moduleName = project.name
    configure()
}