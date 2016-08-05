package su.jfdev.gradle.service.apply

import su.jfdev.gradle.service.plugin.ServicePlugin

class MultiProjectApplySpec extends ApplySpec {
    @Override
    void setup() {
        def parent = project
        project = addSubproject("sub")
        parent.subprojects {
            apply plugin: ServicePlugin
        }
    }
}