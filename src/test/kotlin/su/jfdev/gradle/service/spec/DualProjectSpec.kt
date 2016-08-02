package su.jfdev.gradle.service.spec

import java.io.*

abstract class DualProjectSpec: ProjectSpec() {
    val first = build("first") {
        withProjectDir(File(rootDir, "first"))
        withParent(project)
    }

    val second = build("second") {
        withProjectDir(File(rootDir, "second"))
        withParent(project)
    }
}

