package su.jfdev.gradle.service.describe

import org.gradle.api.Project
import spock.lang.Unroll
import su.jfdev.gradle.service.spec.PluginSpec

class ImplementationSpec extends PluginSpec {

    Project getTarget() { project }

    Project getReceiver() { project }

    @Override
    void setup() {
        project.implementations {
            impl
            alt
        }
    }

    @Unroll
    def "should contains source set `#source`"() {
        when:
        def knownSources = project.sourceSets.names

        then:
        source in knownSources

        where:
        source << ["impl", "alt"]
    }

    @Unroll
    def "should contains runtime and compile configurations to `#source`"() {
        given:
        def knownConfigurations = project.configurations.getNames()

        expect: "should contains compile configurations"
        (source + "Compile") in knownConfigurations

        and: "should contains runtime configurations"
        (source + "Runtime") in knownConfigurations

        where:
        source << ["impl", "alt"]
    }

    @Unroll
    def "should has hierarchy: [#target] <- [#receiver]"() {
        expect:
        assertRequired(receiver, target)

        where:
        receiver | target
        "test"   | "impl"
        "test"   | "alt"

        "impl"   | "main"
        "alt"    | "main"
    }
}