package su.jfdev.gradle.service.util

import nebula.test.ProjectSpec
import su.jfdev.gradle.service.plugin.ServicePlugin
import spock.lang.Shared as S

import static su.jfdev.gradle.service.util.ContainsChecker.checker

class ServiceSpec extends ProjectSpec {
    @S Collection<String> knownSources
    @S Collection<String> knownConfigurations

    @S Collection<String> unknownSources
    @S Collection<String> unknownConfigurations

    void setup() {
        project.plugins.apply(ServicePlugin)

        knownSources = checker(project.sourceSets)
        knownConfigurations = checker(project.configurations)

        unknownSources = knownSources.reverse
        unknownConfigurations = knownSources.reverse
    }
}