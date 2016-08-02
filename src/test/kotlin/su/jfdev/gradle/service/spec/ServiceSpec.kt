package su.jfdev.gradle.service.spec

import org.gradle.api.*
import org.junit.*
import su.jfdev.gradle.service.plugin.*

interface ServiceSpec {

    val project: Project
    @Before
    fun setup() {
        project.plugins.apply(ServicePlugin::class.java)
    }
}
