package su.jfdev.gradle.struct.publish

import org.gradle.api.publish.maven.*
import org.gradle.api.tasks.bundling.*
import su.jfdev.gradle.struct.describe.*
import su.jfdev.gradle.struct.util.*

data class PublicationBuilder(val owner: Pack) {

    val publication: MavenPublication = owner.publication.apply {
        owner.project.plugins
                .getPlugin(PublishPlugin::class.java)
                .archives.add(name)
    }

    inline fun publish(name: String, block: Jar.() -> Unit) {
        val artifact = owner.taskJar(name + "Jar", block)
        publication.artifact(artifact)
    }

    inline fun owner(block: Pack.() -> Unit) {
        owner.block()
    }

    inline fun with(vararg packs: String, block: Pack.() -> Unit) {
        owner.block(); by(packs = *packs, block = block)
    }

    inline fun by(vararg packs: String, block: Pack.() -> Unit) = packs.map { owner.project[it] }.forEach(block)
    inline fun whenever(name: String, block: Pack.() -> Unit) = if (owner.name == name) owner.block() else Unit


    inline fun `when main`(block: Pack.() -> Unit) = whenever("main", block)
    inline fun `with api`(block: Pack.() -> Unit) = with("api", block = block)
}