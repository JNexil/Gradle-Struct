package su.jfdev.gradle.struct.publish

import org.gradle.api.*
import org.gradle.api.publish.*
import org.gradle.api.publish.maven.*
import org.gradle.api.tasks.bundling.*
import su.jfdev.gradle.struct.describe.*
import su.jfdev.gradle.struct.util.*

class PublishPlugin: Plugin<Project> {
    lateinit var project: Project
    override fun apply(project: Project) {
        this.project = project
        with(project) {
            task("structUpload").doFirst {
                onlyWith<PublishingExtension>("publishing") {
                    onlyWith<NamedDomainObjectContainer<Pack>>("describe") {
                        forEach {
                            publications.publish(it)
                        }
                    }
                }
            }
        }
    }

    fun PublicationContainer.publish(pack: Pack) {
        val archive = pack.archive ?: return
        val task = project.tasks.maybeCreate(archive + "Jar", Jar::class.java)
        maybeCreate(archive, MavenPublication::class.java).apply {
            artifactId = archive
            groupId = project.group.toString()
            version = project.version.toString()

            artifact(task)
        }
    }

}
