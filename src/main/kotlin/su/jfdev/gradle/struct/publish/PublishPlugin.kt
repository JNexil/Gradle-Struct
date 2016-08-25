package su.jfdev.gradle.struct.publish

import org.gradle.api.*
import org.gradle.api.publish.maven.plugins.*
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
            addContainer<Pack>("publish") {
                val pack = packs.getByName(it)
                pack.apply {
                    PublicationBuilder(pack).publish()
                }
            }
        }
    }

    fun PublicationBuilder.publish() {
        output@ publish(owner.name) {
            owner {
                from(sourceSet.output)
                dependsOn(classesTask)
            }
        }
        `when main` {
            source@ publish("sources") {
                `with api` {
                    from(sourceSet.allSource)
                    dependsOn(classesTask)
                }
            }
            kotlindoc@ if (needDokka) publishDokka(name = "kdoc", format = "html")
            javadoc@ if (needDokka) publishDokka(name = "javadoc", format = "javadoc") else publish("javadoc") {
                `when main` {
                    `with api` {
                        val javadoc = taskJavadoc("") {}
                        from(javadoc.destinationDir)
                        dependsOn(javadoc)
                    }
                }
            }
        }
    }

    private fun PublicationBuilder.publishDokka(name: String, format: String) = publish(name) {
        `when main` {
            val destination = File(project.buildDir, name)
            from(destination.path)
            val kdoc = taskDokka("K") {
                outputFormat = format
                moduleName = owner.camelPath
                outputDirectory = destination.path
            }
            `with api` {
                kdoc.sourceDirs += sourceSet.allSource.srcDirs
                dependsOn(kdoc)
            }
        }
    }
}
