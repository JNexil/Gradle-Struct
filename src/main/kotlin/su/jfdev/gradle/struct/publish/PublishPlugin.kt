package su.jfdev.gradle.struct.publish

import org.gradle.api.*
import org.gradle.api.publish.maven.plugins.*
import su.jfdev.gradle.struct.describe.*
import su.jfdev.gradle.struct.util.*
import java.util.*

class PublishPlugin: Plugin<Project> {
    val archives: MutableList<String> = ArrayList()

    override fun apply(project: Project) {
        project.run {
            plugins.apply(MavenPublishPlugin::class.java)
            plugins.apply(DescribePlugin::class.java)
            extensions.extraProperties["described"] = archives
            addPublisher("publish")
        }
    }

    private fun Project.addPublisher(name: String) {
        val packs: NamedDomainObjectContainer<Pack> = extOrWarn("describe") ?: return
        addContainer(name) {
            val pack = packs.getByName(it)
            Publisher(pack)
        }
    }

}

