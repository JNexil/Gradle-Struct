package su.jfdev.gradle.service.apply

import org.gradle.api.Project
import spock.lang.Unroll
import su.jfdev.gradle.service.spec.PluginSpec

class ApplySpec extends PluginSpec {
    Project getTarget() { project }

    Project getReceiver() { project }

    def "should contains default sources"() {
        when:
        def known = project.sourceSets.names

        then:
        source in known

        where:
        source << ["api", "main", "test"]
    }

    def "should contains configurations from given source"() {
        given:
        def knownConfigurations = project.configurations.getNames()

        when: "should contains compile configurations"
        def compile = source + "Compile"
        if (source == "main") compile = "compile"
        then:
        compile in knownConfigurations

        when: "should contains runtime configurations"
        def runtime = source + "Runtime"
        if (source == "main") runtime = "runtime"
        then:
        runtime in knownConfigurations

        where:
        source << ["api", "main", "test"]
    }

    @Unroll
    def "should has hierarchy: [#receiver] <- [#target]"() {
        expect:
        assertRequired(receiver, target)

        where:
        receiver | target
        "main"   | "api"
    }
}