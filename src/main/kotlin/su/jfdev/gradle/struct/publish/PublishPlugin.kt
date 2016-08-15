package su.jfdev.gradle.struct.publish

import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.publish.*
import org.gradle.api.publish.maven.*
import org.gradle.api.publish.maven.plugins.*
import org.gradle.api.tasks.bundling.*
import su.jfdev.gradle.struct.describe.*
import su.jfdev.gradle.struct.util.*
import java.util.*

class PublishPlugin: Plugin<Project> {

    val archives: MutableList<String> = ArrayList()

    override fun apply(project: Project) {
        project.run {
            extensions.extraProperties["described"] = archives
            plugins.apply(MavenPublishPlugin::class.java)
            plugins.apply(DescribePlugin::class.java)
            publishPacks()
        }
    }
    private fun Project.publishPacks() = publish { publications ->
        whenObjectAdded {
            it publishTo publications
        }
    }

    private fun Project.publishAll() = publish { publications ->
        forEach {
            it publishTo publications
        }
    }

    private fun Project.publish(action: NamedDomainObjectContainer<Pack>.(PublicationContainer) -> Unit) {
        val publishing: PublishingExtension = extOrWarn("publishing") ?: return
        val packs: NamedDomainObjectContainer<Pack> = extOrWarn("describe") ?: return

        action(packs, publishing.publications)
    }

    infix fun publish(pack: Pack) {
        val publishing: PublishingExtension = pack.project.extOrWarn("publishing") ?: return
        pack publishTo publishing.publications
    }

    private infix fun Pack.publishTo(publications: PublicationContainer) {
        val archive = archive ?: return
        if (archive !in archives)
            publications.maybeCreate(archive, MavenPublication::class.java).apply {
                archives += archive
                artifactId = archive

                groupId = project.group.toString()
                version = project.version.toString()

                publish(this)
            }
    }

    private fun Pack.publish(publication: MavenPublication) {
        publication.artifact(artifact)
    }

    private val Pack.artifact: PublishArtifact get() = project
            .artifacts
            .add("archives", jar)

    private val Pack.jar: Jar get() = project
            .tasks
            .maybeCreate(archive, Jar::class.java)
            .apply { from(sourceSet.output) }

}
