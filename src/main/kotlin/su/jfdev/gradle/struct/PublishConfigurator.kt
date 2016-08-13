package su.jfdev.gradle.struct

import org.codehaus.groovy.runtime.*
import org.gradle.api.*
import org.gradle.api.artifacts.*
import org.gradle.api.artifacts.maven.*
import org.gradle.api.plugins.MavenRepositoryHandlerConvention.*
import org.gradle.api.tasks.*
import org.gradle.jvm.tasks.*
import su.jfdev.gradle.struct.describe.*
import java.io.*

internal class PublishConfigurator(val pack: Pack, val name: String) {
    val project: Project get() = pack.project

    fun configure() = installer
            .addFilter(name, artifact.created)
            .configurePom()

    private val installer: MavenResolver get() = project
            .tasks
            .maybeCreate("install", Upload::class.java)
            .repositories.withType(MavenResolver::class.java)
            .getByName(DEFAULT_MAVEN_INSTALLER_NAME)

    private val artifact: PublishArtifact get() {
        val configuration = project.configurations.maybeCreate("archives").name
        return project.artifacts.add(configuration, task)
    }

    private val task: Jar get() = project
            .tasks
            .maybeCreate(name + "Jar", Jar::class.java)
            .apply { from(pack.sourceSet.output) }

    private fun MavenPom.configurePom() {
        groupId = project.group.toString()
        artifactId = name
        version = project.version.toString()
    }

    private val PublishArtifact.created: (Any, File) -> Boolean get() = { art, file ->
        art["ext"] == extension
        && art["name"] == name
        && art["type"] == type
        && art["publicationDate"] == date
    }

    private operator fun Any.get(name: String) = InvokerHelper.getProperty(this, name)
}