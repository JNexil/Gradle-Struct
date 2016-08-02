package su.jfdev.gradle.service.spec

import su.jfdev.gradle.service.plugin.ServicePlugin

abstract class ServiceSpock extends nebula.test.ProjectSpec {
    void setup() {
        project.plugins.apply(ServicePlugin)
    }
}