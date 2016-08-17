package su.jfdev.gradle.struct.publish

import org.gradle.api.*
import org.gradle.api.publish.*
import org.gradle.api.publish.maven.*
import org.gradle.api.publish.maven.plugins.*
import org.gradle.api.tasks.bundling.*
import org.gradle.api.tasks.javadoc.*
import org.jetbrains.dokka.gradle.*
import su.jfdev.gradle.struct.describe.*
import su.jfdev.gradle.struct.util.*
import java.io.*
import java.util.*

class PublishPlugin: Plugin<Project> {

    val archives: MutableList<String> = ArrayList()

    override fun apply(project: Project) {
        project.run {
            plugins.apply(MavenPublishPlugin::class.java)
            plugins.apply(DescribePlugin::class.java)
            extensions.extraProperties["described"] = archives
            val packs: NamedDomainObjectContainer<Pack> = extOrWarn("describe") ?: return
            addContainer("publish") {
                packs.getByName(it).publish()
            }
        }
    }

    private fun Pack.publish() = apply {
        val publishing: PublishingExtension = project.extOrWarn("publishing") ?: return this
        this publishTo publishing.publications
    }

    private infix fun Pack.publishTo(publications: PublicationContainer) {
        val archive = path.joinToString(transform = String::capitalize)
        val publication = publications.maybeCreate(archive, MavenPublication::class.java).apply {
            if (archives.add(name)) {
                artifactId = path.joinToString(separator = "-")
                groupId = project.group.toString()
                version = project.version.toString()
            }
        }
        publication publish artifactBy(name) {
            dependsOn(classesTask)
            from(sourceSet.output)
        }
        if (name == "main") {
            fun withApi(name: String, apply: Pack.(Jar) -> Unit) = publication publish artifactBy(name) {
                apply(this)
            }.apply {
                project["api"].apply(this)
            }

            withApi("sources") {
                it.dependsOn(classesTask)
                it.from(sourceSet.allSource)
            }
            withApi("javadoc") {
                val javadoc = javadocTask
                it.dependsOn(javadoc)
                it.from(javadoc.destinationDir)

                it addDokka dokka("J", format = "javadoc")
            }
            withApi("kdoc") {
                it addDokka dokka("K", format = "html")
            }
        }
    }

    private infix fun Jar.addDokka(dokka: DokkaTask) {
        dependsOn(dokka)
        from(dokka.getOutputDirectoryAsFile())
    }

    private infix fun MavenPublication.publish(any: Any) = artifact(any)

    private inline fun Pack.artifactBy(suffix: String, configure: Jar.() -> Unit) = task(suffix + "Jar", configure).apply {
        classifier = suffix
        extension = "jar"
        group = "build"
        project.artifacts.add("archives", this)
    }

    private val Pack.classesTask: Task get() = project.tasks
            .getByName(sourceSet.classesTaskName)

    private val Pack.javadocTask: Javadoc get() = task("Javadoc") {
        source(sourceSet.allJava)
    }

    private fun Pack.dokka(prefix: String, format: String): DokkaTask = task(prefix + "Doc") {
        outputFormat = format
        sourceDirs = sourceSet.allJava
        outputDirectory = File(project.buildDir, name).path
    }


    private inline fun <reified T: Task> Pack.task(suffix: String, configure: T.() -> Unit): T {
        val name = sourceSet.getTaskName(null, suffix)
        return project.task(name, configure)
    }

    private inline fun <reified T: Task> Project.task(name: String, configure: T.() -> Unit): T {
        val task = tasks.withType(T::class.java).findByName(name)
        return task ?: tasks.create(name, T::class.java).apply(configure)
    }
}
