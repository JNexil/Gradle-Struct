package su.jfdev.gradle.service.spec

import org.gradle.api.Project
import su.jfdev.gradle.service.plugin.ServicePlugin

abstract class ServiceSpock extends nebula.test.ProjectSpec {
    void setup() {
        serviceTo(project)
    }

    public void serviceTo(Project... projects) {
        for(project in projects) project.plugins.apply(ServicePlugin)
    }
}