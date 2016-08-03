package su.jfdev.gradle.service.describe

import su.jfdev.gradle.service.plugin.ServicePlugin

class MultiProjectComponentsSpec extends ComponentsSpec {
    @Override
    void setup() {
        def parent = project
        project = addSubproject("sub")
        parent.subprojects {
            plugins.apply(ServicePlugin)
        }
    }
}