package su.jfdev.gradle.service.util

import nebula.test.ProjectSpec
import su.jfdev.gradle.service.plugin.ServicePlugin

import static su.jfdev.gradle.service.util.ContainsChecker.*

class ServiceSpec extends ProjectSpec {
    Collection<String> knownSources
    Collection<String> knownConfigurations

    Collection<String> unknownSources
    Collection<String> unknownConfigurations

    void setup() {
        project.plugins.apply(ServicePlugin)
        knownSources = checker(project.sourceSets)
        knownConfigurations = checker(project.configurations)

        unknownSources = knownSources.reverse
        unknownConfigurations = knownSources.reverse
    }
}