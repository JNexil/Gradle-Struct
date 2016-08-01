package su.jfdev.gradle.service.spec

import nebula.test.ProjectSpec
import spock.lang.Shared as S
import su.jfdev.gradle.service.plugin.ServicePlugin
import su.jfdev.gradle.service.util.ContainsChecker

import static su.jfdev.gradle.service.util.ContainsChecker.Companion.checker

abstract class ServiceSpec extends ProjectSpec {





    void setup() {
        project.plugins.apply(ServicePlugin)
    }

    def "Project should contain extension with Closure"() {
        when:
        def service = project.ext.service
        then:
        service instanceof Closure
    }
}