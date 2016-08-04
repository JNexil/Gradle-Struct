package su.jfdev.gradle.service.spec

import org.gradle.api.Project
import su.jfdev.gradle.service.dependency.PackDependency
import su.jfdev.gradle.service.describe.Pack
import su.jfdev.gradle.service.describe.Scope
import su.jfdev.gradle.service.plugin.ServicePlugin

import static su.jfdev.gradle.service.util.PackKt.get

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
        !isRequired(scope, receiver, target)
    }

    protected void assertRequired(Scope scope, String receiver, List<String> target) {
        for (fromEntry in target)
            assert isRequired(scope, receiver, fromEntry)
    }

    protected boolean isRequired(Scope scope, String receiver, String target = receiver) {
        Pack $receiver = get(this.receiver, receiver)
        Pack $target = get(this.target, target)
        isRequired(scope, $receiver, $target)
    }

    protected static boolean isRequired(Scope scope, Pack receiver, Pack target) {
        def $receiver = receiver[scope]
        def $target = target[scope]
        isRequired($receiver, $target)
    }

    public static boolean isRequired(PackDependency $receiver, PackDependency $target) {
        $receiver.configuration.allDependencies.any {
            $target.contentEquals(it) || (it instanceof PackDependency && isRequired(it, $receiver))
        }
    }
}