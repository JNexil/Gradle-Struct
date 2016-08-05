package su.jfdev.gradle.service.spec

import org.gradle.api.Project
import su.jfdev.gradle.service.plugin.ServicePlugin
import su.jfdev.gradle.service.require.Requiring

abstract class ServiceSpec extends nebula.test.ProjectSpec {
    abstract Project getTarget()

    abstract Project getReceiver()

    void setup() {
        serviceTo(project)
    }

    void serviceTo(Project... projects) {
        for(project in projects) project.plugins.apply(ServicePlugin)
    }

    Requiring requiring(Map<String, Object> args) {
        args.receiver = receiver
        args.target = target
        new Requiring(args)
    }
}