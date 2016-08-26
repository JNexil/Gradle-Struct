package su.jfdev.gradle.struct.publish

import org.gradle.api.publish.maven.*
import org.gradle.api.tasks.bundling.*
import su.jfdev.gradle.struct.describe.*
import su.jfdev.gradle.struct.publish.xml.*
import java.io.*

data class Publisher(val owner: Pack) {

    val publication: MavenPublication = owner.publication.apply {
        owner.project.plugins
                .getPlugin(PublishPlugin::class.java)
                .archives.add(name)
    }


    val full: Publisher get() = apply {
        source; docs; out
    }

    val out: Publisher get() = apply {
        dependencies; classes
    }

    val docs: Publisher get() = apply {
        kdoc; javadoc
    }

    val source: Publisher by lazy {
        publish("sources") {
            from(owner.sourceSet.allSource)
            dependsOn(owner.classesTask)
        }
    }

    val dependencies: Publisher by lazy {
        publication.improveDependencies(owner)
        this
    }

    val classes: Publisher by lazy {
        publish {
            owner.run {
                from(sourceSet.output)
                dependsOn(classesTask)
            }
        }
    }

    val kdoc: Publisher by lazy {
        if (needDokka) publishDokka(name = "kdoc", format = "html")
        else this
    }

    val javadoc: Publisher by lazy {
        when {
            needDokka -> publishDokka(name = "javadoc", format = "javadoc")
            else      -> publish("javadoc") {
                val javadoc = owner.taskJavadoc("") {}
                from(javadoc.destinationDir)
                dependsOn(javadoc)
            }
        }
    }

    private fun Publisher.publishDokka(name: String, format: String) = publish(name) {
        owner.apply {
            val destination = File(project.buildDir, name)
            from(destination.path)
            val kdoc = taskDokka("K") {
                outputFormat = format
                moduleName = owner.camelPath
                outputDirectory = destination.path
            }
            kdoc.sourceDirs += sourceSet.allSource.srcDirs
            dependsOn(kdoc)
        }
    }


    private inline fun publish(name: String? = null, block: Jar.() -> Unit) = apply {
        val artifact = owner.taskJar(name, block)
        publication.artifact(artifact)
    }
}