package su.jfdev.gradle.service.spec

import org.gradle.api.*
import org.gradle.testfixtures.*
import org.junit.*
import org.junit.Rule
import org.junit.rules.*
import java.io.*

abstract class ProjectSpec {
    @Rule val folder = TemporaryFolder()

    val rootDir: File = folder.newFile("root")

    val project = build("root") {
        withProjectDir(rootDir)
    }

    protected inline fun build(name: String, block: ProjectBuilder.() -> Unit): Project = ProjectBuilder
            .builder()
            .apply(block)
            .apply { withName(name) }
            .build()
}