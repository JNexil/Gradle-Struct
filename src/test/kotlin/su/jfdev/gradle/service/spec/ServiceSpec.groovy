package su.jfdev.gradle.service.spec

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import su.jfdev.gradle.service.describe.ConfigurationDependency
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
        for (String fromEntry : target)
            assert wasNotRequired(scope, receiver, fromEntry)
    }

    protected void assertRequired(Scope scope, String receiver, List<String> target) {
        for (String fromEntry : target)
            assert wasRequired(scope, receiver, fromEntry)
    }

    protected boolean wasNotRequired(Scope scope, String receiver, String target = receiver) {
        !wasRequired(scope, receiver, target)
    }

    protected boolean wasRequired(Scope scope, String receiver, String target = receiver) {
        String $receiver = scope[receiver]
        String $target = scope[target]
        def configuration = this.receiver.configurations.getByName($receiver)
        isRequired(configuration, $target)
    }

    protected boolean isRequired(Configuration configuration, String $target) {
        configuration.dependencies.any {
            if (it instanceof ConfigurationDependency && it.target.path == this.target.path) {
                def isTarget = it.configuration.name == $target
                isTarget || isRequired(it.configuration, $target)
            } else false
        }
    }
}