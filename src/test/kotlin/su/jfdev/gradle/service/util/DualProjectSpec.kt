package su.jfdev.gradle.service.util

import org.gradle.api.*
import org.gradle.testfixtures.*
import org.junit.Rule
import org.junit.rules.*
import java.io.*

abstract class DualProjectSpec {
    @Rule val folder = TemporaryFolder()

    val rootDir: File = folder.newFile("root")

    val root = build("root") {
        withProjectDir(rootDir)
    }

    val first = build("first") {
        withProjectDir(File(rootDir, "first"))
        withParent(root)
    }

    val second = build("second") {
        withProjectDir(File(rootDir, "second"))
        withParent(root)
    }

    private fun build(name: String, block: ProjectBuilder.() -> Unit): Project = ProjectBuilder
            .builder()
            .apply(block)
            .apply { withName(name) }
            .build()
}