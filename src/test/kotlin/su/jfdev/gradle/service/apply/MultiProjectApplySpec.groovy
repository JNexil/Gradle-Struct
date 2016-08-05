package su.jfdev.gradle.service.apply

import su.jfdev.gradle.struct.Plugin

class MultiProjectApplySpec extends ApplySpec {
    @Override
    void setup() {
        def parent = project
        project = addSubproject("sub")
        parent.subprojects {
            apply plugin: Plugin
        }
    }
}