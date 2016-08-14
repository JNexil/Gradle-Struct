package su.jfdev.gradle.service.require

import nebula.test.ProjectSpec
import su.jfdev.gradle.struct.StructPlugin

class ExternalSpec extends ProjectSpec {
    void setup() {
        project.plugins.apply(StructPlugin)
        project.repositories.mavenCentral()
    }

    def "should add dependency by require"() {
        given:
        def dependencies = project.configurations.compile.allDependencies
        def expect = project.dependencies.create("org.jetbrains.kotlin:kotlin-stdlib:1.0.3")

        when:
        project.require {
            external("org.jetbrains.kotlin:kotlin-%s:1.0.3") {
                compile "stdlib", "main"
            }
        }

        then:
        expect in dependencies
    }
}