package su.jfdev.gradle.service.describe

import nebula.test.ProjectSpec
import su.jfdev.gradle.service.plugin.ServicePlugin

class ServiceDescribeSpec extends ProjectSpec {

    void setup() {
        project.plugins.apply(ServicePlugin)
    }

    def "Project should contain extension with Closure"() {
        when:
        def service = project.ext.service
        then:
        service instanceof Closure
    }

    def "should create source sets with custom names"() {
        given:
        project.service {
            api "myApi"
            spec "mySpec"
            impl "myImpl"
        }

        expect:
        containSource(known)
        !containSource(unknown)

        where:
        known << ["myApi", "mySpec", "myImpl"]
        unknown << ["api", "spec", "impl"]
    }

    private boolean containSource(String name) {
        project.sourceSets.findByName(name)
    }

    private boolean containConfiguration(String name) {
        project.configurations.findByName(name)
    }
}