package su.jfdev.gradle.struct.publish

import org.gradle.api.publish.*
import org.gradle.api.publish.maven.*
import su.jfdev.gradle.struct.describe.*
import su.jfdev.gradle.struct.util.*


val Pack.publication: MavenPublication get() = publications
        .maybeCreate(camelPath, MavenPublication::class.java)
        .apply {
            artifactId = path.joinToString(separator = "-")
            groupId = project.group.toString()
            version = project.version.toString()
        }

val Pack.publications: PublicationContainer get() = publishing.publications
val Pack.publishing: PublishingExtension get() = project.extensions.getByType(PublishingExtension::class.java)
val Pack.camelPath: String get() = path.joinToString(transform = String::capitalize, separator = "")