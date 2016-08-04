package su.jfdev.gradle.service.spec

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import su.jfdev.gradle.service.dependency.PackDependency
import su.jfdev.gradle.service.describe.Scope
import su.jfdev.gradle.service.plugin.ServicePlugin

abstract class ServiceSpec extends nebula.test.ProjectSpec {
    protected abstract Project getTarget()
    protected abstract Project getReceiver()

    void setup() {
        serviceTo(project)
    }

    public void serviceTo(Project... projects) {
        for(project in projects) project.plugins.apply(ServicePlugin)
    }

    protected void assertNonRequired(Scope scope, String receiver, List<String> target) {
        for (fromEntry in target)
            assert wasNotRequired(scope, receiver, fromEntry)
    }

    protected boolean wasNotRequired(Scope scope, String receiver, String target = receiver) {
        !wasRequired(scope, receiver, target)
    }

    protected void assertRequired(Scope scope, String receiver, List<String> target) {
        for (fromEntry in target)
            assert wasRequired(scope, receiver, fromEntry)
    }

    protected boolean wasRequired(Scope scope, String receiver, String target = receiver) {
        String $receiver = scope[receiver]
        String $target = scope[target]
        def receiverConf = this.receiver.configurations.getByName($receiver)
        isRequired(receiverConf, $target)
    }

    protected boolean isRequired(Configuration receiver, String $target) {
        receiver.dependencies.any {
            if (it instanceof PackDependency && it.target.path == this.target.path) {
                it.configuration.name == $target || isRequired(it.configuration, $target)
            } else false
        }
    }
}