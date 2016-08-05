package su.jfdev.gradle.service.describe

import org.gradle.api.Project
import spock.lang.Unroll
import su.jfdev.gradle.service.spec.PluginSpec
import su.jfdev.gradle.struct.describe.Pack
import su.jfdev.gradle.struct.describe.PackDependency
import su.jfdev.gradle.struct.describe.Scope

class PackSpec extends PluginSpec {
    public static final String NAME = "anySrc"
    public static final String RUNTIME = NAME + "Runtime"
    public static final String COMPILE = NAME + "Compile"

    void setup() {
        pack = new Pack(project, NAME)
    }

    Project getTarget() { project }
    Project getReceiver() { project }
    Pack pack


    def "should create sourceSet with pack's name"() {
        when:
        def source = pack.sourceSet
        then:
        source
        source.name
    }

    def "should create configurations"() {
        expect:
        pack.name == NAME
        pack.project == project

        when:
        def configurations = project.configurations

        then:
        configurations.findByName RUNTIME
        configurations.findByName COMPILE
    }

    @Unroll
    def "should depend pack to pack with scope: #scope"() {
        given:
        def target = new Pack(project, "target")

        when:
        pack.depend(target)

        and:
        def anyConf = project.configurations["anySrc" + scopeSuffix]
        def targetDependency = new PackDependency(scope, target)

        then:
        targetDependency in anyConf.dependencies

        where:
        scope         | scopeSuffix
        Scope.RUNTIME | "Runtime"
        Scope.COMPILE | "Compile"
    }

    @Unroll
    def "should extend pack to pack with scope: #scope"() {
        given:
        def target = new Pack(project, "target")

        when:
        target.extend(pack)

        and:
        def anyConf = project.configurations["anySrc" + scopeSuffix]
        def targetDependency = new PackDependency(scope, target)

        then:
        targetDependency in anyConf.dependencies

        where:
        scope         | scopeSuffix
        Scope.RUNTIME | "Runtime"
        Scope.COMPILE | "Compile"
    }

    @Unroll
    def "should extend pack to pack with scope: `#active` but not `#exclusion`"() {
        given:
        def target = new Pack(project, "target")
        target.extend(pack, active)

        when:
        def anyConf = project.configurations["anySrc" + scopeSuffix]
        def targetDependency = new PackDependency(active, target)

        then:
        targetDependency in anyConf.dependencies

        when:
        def disabledConf = project.configurations["anySrc" + exclusionSuffix]
        def disabledTarget = new PackDependency(active, target)

        then:
        !(disabledTarget in disabledConf.dependencies)

        where:
        active        | scopeSuffix | exclusion     | exclusionSuffix
        Scope.RUNTIME | "Runtime"   | Scope.COMPILE | "Compile"
        Scope.COMPILE | "Compile"   | Scope.RUNTIME | "Runtime"
    }
}