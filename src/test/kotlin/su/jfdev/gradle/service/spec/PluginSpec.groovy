package su.jfdev.gradle.service.spec

import nebula.test.ProjectSpec
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import su.jfdev.gradle.struct.plugins.StructPlugin
import su.jfdev.gradle.struct.describe.Pack
import su.jfdev.gradle.struct.describe.PackDependency
import su.jfdev.gradle.struct.describe.Scope

abstract class PluginSpec extends ProjectSpec {
    protected abstract Project getTarget()

    protected abstract Project getReceiver()

    void setup() {
        applyTo(project)
    }

    void applyTo(Project... projects) {
        for (project in projects) project.plugins.apply(StructPlugin)
    }

    void assertRequired(String receiver, String target = receiver) {
        assertRequired(Scope.COMPILE, receiver, target)
        assertRequired(Scope.RUNTIME, receiver, target)
    }

    void assertRequired(Scope scope, String $receiver, String $target = $receiver) {
        def receiverPack = new Pack(receiver, $receiver)
        def targetPack = new Pack(target, $target)

        assert isRequired(scope, receiverPack, targetPack)
    }

    boolean isRequired(Scope scope, Pack receiverPack, Pack targetPack) {
        def receiverConfiguration = receiverPack.get(scope)
        def targetDependency = new PackDependency(scope, targetPack)
        isRequired(receiverConfiguration, targetDependency)
    }

    static boolean isRequired(Configuration $receiver, PackDependency $target) {
        $receiver.allDependencies.any {
            it == $target
        }
    }
}