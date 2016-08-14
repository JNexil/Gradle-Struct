package su.jfdev.gradle.struct.publish

import org.gradle.api.*
import org.gradle.api.publish.*
import org.gradle.api.publish.maven.*
import org.gradle.api.tasks.bundling.*
import su.jfdev.gradle.struct.describe.*
import su.jfdev.gradle.struct.util.*
import java.util.*

class PublishPlugin: Plugin<Project> {
    lateinit var project: Project
    val archives: Iterable<String> = Archives()

    override fun apply(project: Project) {
        this.project = project
        project.extensions.add("structArchives", archives)
    }

    private val Pack.toArchive: String? get() {
        project.onlyWith<PublishingExtension>("publishing") {
            return publications publish this@toArchive
        }
        return null
    }

    private infix fun PublicationContainer.publish(pack: Pack): String? {
        val archive = pack.archive ?: return null
        return publish(archive, pack).name
    }

    private fun PublicationContainer.publish(archive: String, pack: Pack): MavenPublication
            = maybeCreate(archive, MavenPublication::class.java)
            .apply { configure(archive, pack) }

    private fun MavenPublication.configure(archive: String, pack: Pack) {
        artifactId = archive
        groupId = project.group.toString()
        version = project.version.toString()

        artifact(pack toJar archive)
    }

    private infix fun Pack.toJar(archive: String): Jar = project.tasks.maybeCreate(archive + "Jar", Jar::class.java).apply {
        from(sourceSet.output)
    }

    private inner class Archives: Iterable<String> {
        private val archives = HashSet<String>()
        private val extension: NamedDomainObjectContainer<Pack>? get() = project.extensions["describe"]
        private var listened: Boolean = false

        override fun iterator(): Iterator<String> {
            if (!listened) extension?.listen()
            return archives.iterator()
        }

        private fun NamedDomainObjectContainer<Pack>.listen() {
            listened = true
            whenObjectAdded { pack ->
                val archive = pack.toArchive
                if(archive != null){
                    archives += archive
                }
            }
        }
    }
}
